package practicum.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentStatusUpdateResult {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDtoResponse> confirmedRequests;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDtoResponse> rejectedRequests;

}
