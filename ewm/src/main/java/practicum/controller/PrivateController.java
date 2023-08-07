package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import practicum.model.EventFullDto;
import practicum.model.EventRequestStatusUpdateRequest;
import practicum.model.EventRequestStatusUpdateResult;
import practicum.model.EventShortDto;
import practicum.model.NewEventDto;
import practicum.model.ParticipationRequestDto;
import practicum.model.UpdateEventUserRequest;
import practicum.service.privateService.PrivateEventServiceDao;
import practicum.service.privateService.PrivateRequestService;
import practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class PrivateController {

    private final PrivateEventServiceDao privateEventsService;

    private final PrivateRequestService privateRequestService;
    private final String USERS_USER_ID = "/users/{userId}";
    private final String EVENTS = "/events";
    private final String EVENT_ID = "/{eventId}";
    private final String REQUESTS = "/requests";

    @PostMapping(USERS_USER_ID + EVENTS)
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventsService.postEvent(userId, newEventDto);
    }

    @GetMapping(USERS_USER_ID + EVENTS)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         HttpServletRequest httpServletRequest) {
        return privateEventsService.getEvents(userId, PageableMaker.makePageable(from, size), httpServletRequest);
    }

    @GetMapping(USERS_USER_ID + EVENTS + EVENT_ID)
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     HttpServletRequest httpServletRequest) {
        return privateEventsService.getEventById(userId, eventId, httpServletRequest);
    }

    @PatchMapping(USERS_USER_ID + EVENTS + EVENT_ID + REQUESTS)
    public EventRequestStatusUpdateResult patchRequests(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @RequestBody(required = false) EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                        HttpServletRequest httpServletRequest) {
        return privateEventsService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest, httpServletRequest);
    }

    @PatchMapping(USERS_USER_ID + EVENTS + EVENT_ID)
    public EventFullDto patchEvent(HttpServletRequest httpServletRequest,
                                   @PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return privateEventsService.patchEvent(userId, eventId, updateEventUserRequest, httpServletRequest);
    }

    @GetMapping(USERS_USER_ID + EVENTS + EVENT_ID + REQUESTS)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventsService.getRequestsForEvent(userId, eventId);
    }

    @PostMapping(USERS_USER_ID + REQUESTS)
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return privateRequestService.postRequest(userId, eventId);
    }

    @GetMapping(USERS_USER_ID + REQUESTS)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        return privateRequestService.getRequests(userId);
    }

    @PatchMapping(USERS_USER_ID + REQUESTS + "/{requestId}/cancel")
    public ParticipationRequestDto patchRequestStateToCancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return privateRequestService.patchRequestStateToCancel(userId, requestId);
    }
}
