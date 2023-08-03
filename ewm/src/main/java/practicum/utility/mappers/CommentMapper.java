package practicum.utility.mappers;

import lombok.experimental.UtilityClass;
import practicum.model.Comment;
import practicum.model.CommentDtoResponse;
import practicum.model.CommentDtoShort;
import practicum.model.NewCommentDto;
import practicum.utility.CommentStatus;
import practicum.utility.EWMDateTimePattern;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER);

    public Comment fromNewCommentDto(@NotNull NewCommentDto newCommentDto) {
        return Comment.builder()
                .comment(newCommentDto.getComment())
                .commentStatus(CommentStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDtoResponse toCommentDtoResponse(Comment comment) {
        return CommentDtoResponse.builder()
                .commentStatus(comment.getCommentStatus())
                .id(comment.getId())
                .comment(comment.getComment())
                .created(comment.getCreated().format(FORMATTER))
                .build();
    }

    public CommentDtoShort toCommentDtoShort(Comment comment) {
        return CommentDtoShort.builder()
                .commentStatus(comment.getCommentStatus())
                .id(comment.getId())
                .comment(comment.getComment())
                .created(comment.getCreated().format(FORMATTER))
                .commenterName(comment.getCommenter().getName())
                .build();
    }

}
