package practicum.model;

import lombok.Builder;
import lombok.Data;
import practicum.utility.RequestStatus;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;
    private RequestStatus status;

}
