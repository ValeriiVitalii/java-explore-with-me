package practicum.service.privateService;

import practicum.model.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {

    ParticipationRequestDto postRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto patchRequestStateToCancel(Long userId, Long requestId);
}
