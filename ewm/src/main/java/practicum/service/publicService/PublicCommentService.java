package practicum.service.publicService;

import practicum.model.dto.CommentDtoResponse;

import java.util.List;

public interface PublicCommentService {

    List<CommentDtoResponse> getComments(Long eventId);

    CommentDtoResponse getCommentById(Long commentId);

}
