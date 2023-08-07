package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.example.client.WebClientService;
import org.example.model.HitDto;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.model.CommentDtoShort;
import practicum.model.EventFullDto;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.service.publicService.PublicEventService;
import practicum.utility.CommentStatus;
import practicum.utility.EWMDateTimePattern;
import practicum.utility.EventState;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventServiceDao implements PublicEventService {

    private  WebClientService baseClient;

    private final EventRepository eventRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public EventFullDto getEventById(HttpServletRequest httpServletRequest, Long id) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Событие не найдено")));

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
            eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
            eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
            return eventFullDto;
        }

        throw new NotFoundException("Нет подходящих опубликованных событий");
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsFiltered(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                String sort,
                                                Pageable pageable,
                                                HttpServletRequest httpServletRequest) {

        List<EventFullDto> eventFullDtos = (BooleanUtils.isTrue(onlyAvailable)) ?
                eventRepository.findAllByParamsPublic(text,
                                categories,
                                paid,
                                rangeStart,
                                rangeEnd,
                                sort,
                                pageable)
                        .stream()
                        .map(EventMapper::toEventFullDto)
                        .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                        .filter(e -> e.getState() == EventState.PUBLISHED)
                        .collect(Collectors.toList())
                :
                eventRepository.findAllByParamsPublic(text,
                                categories,
                                paid,
                                rangeStart,
                                rangeEnd,
                                sort,
                                pageable)
                        .stream()
                        .map(EventMapper::toEventFullDto)
                        .filter(e -> e.getState() == EventState.PUBLISHED)
                        .collect(Collectors.toList());


        for (EventFullDto e : eventFullDtos) {
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
            e.setViews(setViewsToEventFullDtoList(e, httpServletRequest));
            e.setComments(setCommentsToEventFullDto(e));
        }

        return eventFullDtos;
    }

    private long setViewsToEventFullDto(HttpServletRequest httpServletRequest) {

        return baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(httpServletRequest.getRequestURI()),
                true).get(0).getHits();

    }

    private long setViewsToEventFullDtoList(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) {
        return baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(httpServletRequest.getRequestURI() + "/" + eventFullDto.getId()),
                true).get(0).getHits();
    }

    private List<CommentDtoShort> setCommentsToEventFullDto(EventFullDto eventFullDto) {
        return commentRepository.findAllByEventIdAndCommentStatus(eventFullDto.getId(), CommentStatus.CONFIRMED).stream()
                .map(CommentMapper::toCommentDtoShort).collect(Collectors.toList());
    }

}
