package practicum.service.publicService;


import org.springframework.data.domain.Pageable;
import practicum.model.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    EventFullDto getEventById(HttpServletRequest httpServletRequest, Long id);

    List<EventFullDto> getEventsFiltered(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Pageable pageable,
                                         HttpServletRequest httpServletRequest);

}
