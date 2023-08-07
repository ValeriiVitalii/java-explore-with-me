package practicum.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.CommentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentStatusUpdateRequest {

    @NotEmpty
    private List<Long> commentIds;
    @NotBlank
    private CommentStatus status;

}
