package practicum.model;

import lombok.Builder;
import lombok.Data;
import practicum.utility.CommentStatus;

@Data
@Builder
public class CommentDtoResponse {

    private Long id;

    private String comment;

    private String created;

    private CommentStatus commentStatus;

    private EventShortDto event;

    private UserShortDto commenter;
}
