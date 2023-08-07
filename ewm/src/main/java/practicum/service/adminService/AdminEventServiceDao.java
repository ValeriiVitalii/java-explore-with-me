package practicum.service.adminService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.WebClientService;
import org.example.model.Stats;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.model.dto.CommentDtoShort;
import practicum.model.Event;
import practicum.model.dto.EventFullDto;
import practicum.model.dto.UpdateEventAdminRequest;
import practicum.repository.CategoryRepository;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.RequestRepository;
import practicum.utility.EventState;
import practicum.utility.RequestStatus;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceDao implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private WebClientService baseClient;
    private static final String URI_PREFIX = "/events";

    @Override
    @Transactional
    public List<EventFullDto> getEventsAdmin(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Pageable pageable,
                                             HttpServletRequest httpServletRequest) {

        List<Event> events = eventRepository.findAllByParams(users,
                states,
                categories,
                pageable);

        List<EventFullDto> eventFullDtoList;

        if (Optional.ofNullable(rangeStart).isPresent() && Optional.ofNullable(rangeEnd).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeStart).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeEnd).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else {
            eventFullDtoList = events.stream()
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        }
        for (EventFullDto efd : eventFullDtoList) {
            efd.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(efd.getId(), RequestStatus.CONFIRMED));
            efd.setViews(setViewsToEventFullDtoList(efd, httpServletRequest));
            efd.setComments(setCommentsToEventFullDto(efd));
        }
        return eventFullDtoList;
    }

    @Override
    @Transactional
    public EventFullDto patchEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest httpServletRequest) {
        Event event = EventMapper.updateEventAdminRequest(eventRepository.findById(eventId)
                .orElseThrow(()
                        -> new NotFoundException("Событие не найдено!")), updateEventAdminRequest);

        if (updateEventAdminRequest.getCategory() > 0)
            event.setCategory(categoryRepository.findCategoryById(updateEventAdminRequest.getCategory()));

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
        eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
        return eventFullDto;
    }

    private List<CommentDtoShort> setCommentsToEventFullDto(EventFullDto eventFullDto) {
        return commentRepository.findAllByEventId(eventFullDto.getId()).stream()
                .map(CommentMapper::toCommentDtoShort).collect(Collectors.toList());
    }

    private long setViewsToEventFullDto(HttpServletRequest httpServletRequest) {
        String path = (httpServletRequest.getRequestURI());
        return viewsFormer(path);
    }

    private long setViewsToEventFullDtoList(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) {
        String path = (httpServletRequest.getRequestURI() + "/" + eventFullDto.getId());
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

}
