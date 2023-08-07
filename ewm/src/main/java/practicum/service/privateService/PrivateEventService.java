package practicum.service.privateService;

import org.springframework.data.domain.Pageable;
import practicum.model.dto.EventFullDto;
import practicum.model.dto.EventRequestStatusUpdateRequest;
import practicum.model.dto.EventRequestStatusUpdateResult;
import practicum.model.dto.EventShortDto;
import practicum.model.dto.NewEventDto;
import practicum.model.dto.ParticipationRequestDto;
import practicum.model.dto.UpdateEventUserRequest;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface PrivateEventService {

    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, Pageable pageable, HttpServletRequest httpServletRequest);

    EventFullDto getEventById(Long userId, Long eventId, HttpServletRequest httpServletRequest);

    EventRequestStatusUpdateResult patchRequests(Long userId,
                                                 Long eventId,
                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                 HttpServletRequest httpServletRequest);

    EventFullDto patchEvent(Long userId,
                            Long eventId,
                            UpdateEventUserRequest updateEventUserRequest,
                            HttpServletRequest httpServletRequest);

    List<ParticipationRequestDto> getRequestsForEvent(Long userId, Long eventId);

}
