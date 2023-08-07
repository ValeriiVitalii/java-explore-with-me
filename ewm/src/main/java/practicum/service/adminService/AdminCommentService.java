package practicum.service.adminService;

import practicum.model.dto.CommentStatusUpdateRequest;
import practicum.model.dto.CommentStatusUpdateResult;

public interface AdminCommentService {

    void deleteComment(Long commentId);

    CommentStatusUpdateResult patchStatusComments(CommentStatusUpdateRequest commentStatusUpdateRequest);
}
