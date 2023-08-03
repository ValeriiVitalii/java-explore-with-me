package practicum.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

}
