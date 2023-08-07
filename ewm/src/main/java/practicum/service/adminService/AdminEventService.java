package practicum.service.adminService;

import org.springframework.data.domain.Pageable;
import practicum.model.dto.EventFullDto;
import practicum.model.dto.UpdateEventAdminRequest;
import practicum.utility.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Pageable pageable, HttpServletRequest httpServletRequest);

    EventFullDto patchEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest,
                                 HttpServletRequest httpServletRequest);
}
