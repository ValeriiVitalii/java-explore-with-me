package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import practicum.model.dto.CommentDtoResponse;
import practicum.model.dto.CommentStatusUpdateRequest;
import practicum.model.dto.CommentStatusUpdateResult;
import practicum.model.dto.EventFullDto;
import practicum.model.dto.EventRequestStatusUpdateRequest;
import practicum.model.dto.EventRequestStatusUpdateResult;
import practicum.model.dto.EventShortDto;
import practicum.model.dto.NewCommentDto;
import practicum.model.dto.NewEventDto;
import practicum.model.dto.ParticipationRequestDto;
import practicum.model.dto.UpdateEventUserRequest;
import practicum.service.privateService.PrivateCommentService;
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

    private final PrivateCommentService privateCommentsService;
    private final String USER_ID = "/{userId}";
    private final String EVENTS = "/events";
    private final String EVENT_ID = "/{eventId}";
    private final String REQUESTS = "/requests";
    private final String COMMENTS = "/comments";
    private final String COMMENT_ID = "/{commentId}";
    private final String EVENTS_EVENTS_ID = "/events/{eventId}";

    @PostMapping(USER_ID + EVENTS)
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventsService.postEvent(userId, newEventDto);
    }

    @GetMapping(USER_ID + EVENTS)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         HttpServletRequest httpServletRequest) {
        return privateEventsService.getEvents(userId, PageableMaker.makePageable(from, size), httpServletRequest);
    }

    @GetMapping(USER_ID + EVENTS + EVENT_ID)
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     HttpServletRequest httpServletRequest) {
        return privateEventsService.getEventById(userId, eventId, httpServletRequest);
    }

    @PatchMapping(USER_ID + EVENTS + EVENT_ID + REQUESTS)
    public EventRequestStatusUpdateResult patchRequests(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @RequestBody(required = false) EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                        HttpServletRequest httpServletRequest) {
        return privateEventsService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest, httpServletRequest);
    }

    @PatchMapping(USER_ID + EVENTS + EVENT_ID)
    public EventFullDto patchEvent(HttpServletRequest httpServletRequest,
                                   @PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return privateEventsService.patchEvent(userId, eventId, updateEventUserRequest, httpServletRequest);
    }

    @GetMapping(USER_ID + EVENTS + EVENT_ID + REQUESTS)
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventsService.getRequestsForEvent(userId, eventId);
    }

    @PostMapping(USER_ID + REQUESTS)
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return privateRequestService.postRequest(userId, eventId);
    }

    @GetMapping(USER_ID + REQUESTS)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        return privateRequestService.getRequests(userId);
    }

    @PatchMapping(USER_ID + REQUESTS + "/{requestId}/cancel")
    public ParticipationRequestDto patchRequestStateToCancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return privateRequestService.patchRequestStateToCancel(userId, requestId);
    }

    @GetMapping(USER_ID + COMMENTS)
    public List<CommentDtoResponse> getCommentByUserId(@PathVariable long userId) {
        return privateCommentsService.getCommentByUserId(userId);
    }

    @PatchMapping(USER_ID + COMMENTS + COMMENT_ID + "/cancel")
    public CommentDtoResponse patchCancelComment(@PathVariable long userId,
                                                 @PathVariable long commentId) {
        return privateCommentsService.patchCancelComment(userId, commentId);
    }

    @DeleteMapping(USER_ID + COMMENTS + COMMENT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        privateCommentsService.deleteComment(userId, commentId);
    }

    @PostMapping(USER_ID + COMMENTS + EVENTS_EVENTS_ID)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse postNewComment(@PathVariable long userId,
                                             @PathVariable long eventId,
                                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return privateCommentsService.postNewComment(userId, eventId, newCommentDto);
    }

    @PatchMapping(USER_ID + COMMENTS + EVENTS_EVENTS_ID)
    public CommentStatusUpdateResult patchStatusComments(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody CommentStatusUpdateRequest commentStatusUpdateRequest) {
        return privateCommentsService.patchStatusComments(userId, eventId, commentStatusUpdateRequest);
    }
}
