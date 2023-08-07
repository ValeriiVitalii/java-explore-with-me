package practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.Comment;
import practicum.utility.CommentStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(long eventId);

    List<Comment> findAllByEventIdAndCommentStatus(long eventId, CommentStatus commentStatus);

    List<Comment> findAllByIdIn(List<Long> commentIds);

    List<Comment> findByCommenterId(Long userId);

}
