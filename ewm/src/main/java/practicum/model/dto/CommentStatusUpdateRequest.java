package practicum.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.CommentStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentStatusUpdateRequest {

    @NotEmpty
    List<Long> commentIds;
    @NotNull
    CommentStatus status;

}
