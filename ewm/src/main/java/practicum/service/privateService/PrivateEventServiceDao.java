package practicum.service.privateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.WebClientService;
import org.example.model.HitDto;
import org.example.model.Stats;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.ForbiddenException;
import practicum.exception.NotFoundException;
import practicum.model.dto.CommentDtoShort;
import practicum.model.Event;
import practicum.model.dto.EventFullDto;
import practicum.model.dto.EventRequestStatusUpdateRequest;
import practicum.model.dto.EventRequestStatusUpdateResult;
import practicum.model.dto.EventShortDto;
import practicum.model.dto.NewEventDto;
import practicum.model.dto.ParticipationRequestDto;
import practicum.model.Request;
import practicum.model.dto.UpdateEventUserRequest;
import practicum.repository.CategoryRepository;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.RequestRepository;
import practicum.repository.UserRepository;
import practicum.utility.CommentStatus;
import practicum.utility.EWMDateTimePattern;
import practicum.utility.EventState;
import practicum.utility.RequestStatus;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;
import practicum.utility.mappers.RequestMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventServiceDao implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    private WebClientService baseClient;
    private static final String URI_PREFIX = "/events";

    @Override
    @Transactional
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        log.info("Создается Event {} от User {}", newEventDto, userId);
        Event newEvent = EventMapper.fromNewEventDto(newEventDto);
        newEvent.setInitiator(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден!")
        ));
        newEvent.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория не найдена!")
        ));
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    @Transactional
    public List<EventShortDto> getEvents(Long userId, Pageable pageable, HttpServletRequest httpServletRequest) {
        log.info("Выводим список Event от User {}", userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);

        if (events.isEmpty()) {
            throw new NotFoundException("Не найдено событий от указанного пользователя");
        }

        for (Event e : events) {
            if (e.getState() == EventState.PUBLISHED) {
                HitDto hitDto = HitDto.builder()
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                        .uri(httpServletRequest.getRequestURI() + "/" + e.getId())
                        .app("ewm-main-service")
                        .ip(httpServletRequest.getRemoteAddr()).build();
                try {
                    baseClient.postHit(hitDto.getApp(),
                            hitDto.getUri(),
                            hitDto.getIp(),
                            hitDto.getTimestamp());
                } catch (DecodingException ignored) {
                    log.warn("Произошла ошибка кодировки при обращении к клиенту статистики!");
                }
            }
        }
        List<EventShortDto> eventShortDtoList = events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        for (EventShortDto esd : eventShortDtoList) {
            esd.setViews(setViewsToEventShortDtoList(esd, httpServletRequest));
        }
        return eventShortDtoList;
    }

    @Override
    @Transactional
    public EventFullDto getEventById(Long userId, Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Выводим Event {} от User {}", eventId, userId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(
                        () -> new NotFoundException("Событие не найдено")
                ));

        if (eventFullDto.getState() == EventState.PUBLISHED) {
            HitDto hitDto = HitDto.builder()
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                    .uri(httpServletRequest.getRequestURI())
                    .app("ewm-main-service")
                    .ip(httpServletRequest.getRemoteAddr()).build();
            try {
                baseClient.postHit(hitDto.getApp(),
                        hitDto.getUri(),
                        hitDto.getIp(),
                        hitDto.getTimestamp());
            } catch (DecodingException ignored) {
                log.warn("Произошла ошибка кодировки при обращении к клиенту статистики!");
            }
        }

        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
        eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchRequests(Long userId,
                                                        Long eventId,
                                                        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                        HttpServletRequest httpServletRequest) {
        EventFullDto eventFullDto = getEventById(userId, eventId, httpServletRequest);
        List<Request> requestStatusUpdateList = requestRepository.findAllByEventIdAndIdIn(eventId,
                eventRequestStatusUpdateRequest.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (eventFullDto.getParticipantLimit() > 0) {
            for (Request r : requestStatusUpdateList) {
                if (eventFullDto.getConfirmedRequests() >= eventFullDto.getParticipantLimit())
                    throw new ForbiddenException("Невозможно подтвердить новую заявку – достигнут лимит!");

                Request request = requestRepository.findById(r.getId()).orElseThrow(
                        () -> new NotFoundException("Запрос не найден!")
                );

                if (r.getStatus() != RequestStatus.PENDING)
                    throw new ForbiddenException("Сменить статус можно только у заявок в статусе PENDING!");

                request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                requestRepository.save(request);

                if (request.getStatus() == RequestStatus.CONFIRMED) {
                    confirmedRequests.add(RequestMapper.toParticipationRequestDto(request));
                    eventFullDto.setConfirmedRequests(eventFullDto.getConfirmedRequests() + 1);
                }

                if (request.getStatus() == RequestStatus.REJECTED)
                    rejectedRequests.add(RequestMapper.toParticipationRequestDto(request));
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest,
                                   HttpServletRequest httpServletRequest) {
        Event event = EventMapper.updateEventUserRequest(eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие не найдено")
        ), updateEventUserRequest);

        if (event.getInitiator().getId() != userId)
            throw new ForbiddenException("Нельзя менять чужие события!");

        if (updateEventUserRequest.getCategory() > 0)
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
        eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
        return eventFullDto;
    }


    private long setViewsToEventFullDto(HttpServletRequest httpServletRequest) {
        String path = (httpServletRequest.getRequestURI());
        return viewsFormer(path);
    }

    private long setViewsToEventShortDtoList(EventShortDto eventShortDto, HttpServletRequest httpServletRequest) {
        String path = (httpServletRequest.getRequestURI() + "/" + eventShortDto.getId());
        return viewsFormer(path);
    }

    private long viewsFormer(String path) {
        int charIndex = path.indexOf(URI_PREFIX);
        long views = 0;

        List<Stats> list = baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(path.substring(charIndex)), true);

        for (Stats v : list) {
            views = v.getHits();
        }

        return views;
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getRequestsForEvent(Long userId, Long eventId) {
        if (!eventRepository.findAllByInitiatorId(userId, Pageable.unpaged()).contains(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("У данного пользователя не инициировано событий"))))
            throw new NotFoundException("Данное событие инициировано другим пользователем");
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private List<CommentDtoShort> setCommentsToEventFullDto(EventFullDto eventFullDto) {
        return commentRepository.findAllByEventIdAndCommentStatus(
                        eventFullDto.getId(), CommentStatus.CONFIRMED
                ).stream()
                .map(CommentMapper::toCommentDtoShort).collect(Collectors.toList());
    }

}
