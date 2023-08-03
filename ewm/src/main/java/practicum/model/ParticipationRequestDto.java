package practicum.model;

import lombok.Builder;
import lombok.Data;
import practicum.utility.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ParticipationRequestDto {
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestStatus status;
}
