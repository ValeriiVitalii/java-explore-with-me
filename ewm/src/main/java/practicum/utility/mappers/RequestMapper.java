package practicum.utility.mappers;


import lombok.experimental.UtilityClass;
import practicum.model.dto.ParticipationRequestDto;
import practicum.model.Request;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .build();
    }
}
