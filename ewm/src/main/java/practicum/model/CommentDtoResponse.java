package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.CommentStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CommentDtoResponse {

    Long id;

    String comment;

    String created;

    CommentStatus commentStatus;

    EventShortDto event;

    UserShortDto commenter;
}
