package practicum.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.CommentStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CommentDtoShort {

    Long  id;

    String comment;

    String created;

    CommentStatus commentStatus;

    String commenterName;
}
