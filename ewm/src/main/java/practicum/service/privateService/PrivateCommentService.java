package practicum.service.privateService;

import practicum.model.dto.CommentDtoResponse;
import practicum.model.dto.CommentStatusUpdateRequest;
import practicum.model.dto.CommentStatusUpdateResult;
import practicum.model.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {

    List<CommentDtoResponse> getCommentByUserId(long userId);


    CommentDtoResponse patchCancelComment(long userId, long commentId);

    void deleteComment(long userId, long commentId);

    CommentDtoResponse postNewComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentStatusUpdateResult patchStatusComments(long userId,
                                                  long eventId,
                                                  CommentStatusUpdateRequest commentStatusUpdateRequest);
}
