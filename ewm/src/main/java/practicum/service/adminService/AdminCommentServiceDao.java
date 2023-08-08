package practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.model.Comment;
import practicum.model.dto.CommentDtoResponse;
import practicum.model.dto.CommentStatusUpdateRequest;
import practicum.model.dto.CommentStatusUpdateResult;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.UserRepository;
import practicum.utility.CommentStatus;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;
import practicum.utility.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentServiceDao implements AdminCommentService{

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id:" + commentId + "не найден!")
        );

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentStatusUpdateResult patchStatusComments(CommentStatusUpdateRequest commentStatusUpdateRequest) {
        List<Comment> comments = commentRepository.findAllByIdIn(commentStatusUpdateRequest.getCommentIds());
        List<CommentDtoResponse> commentDtoResponseConfirmedList = new ArrayList<>();
        List<CommentDtoResponse> commentDtoResponseRejectedList = new ArrayList<>();
        for (Comment c : comments) {
            if (c.getCommentStatus() == commentStatusUpdateRequest.getStatus()) {
                log.error("У комментария с id {} установлен такой же статус!", c.getId());
            } else {
                c.setCommentStatus(commentStatusUpdateRequest.getStatus());
                CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
                commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(c.getCommenter().getId()).orElseThrow(
                        () -> new NotFoundException("Пользователь с id:" + c.getCommenter().getId() + "не найден!")
                )));
                commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(c.getEvent().getId()).orElseThrow(
                        () -> new NotFoundException("Событие с id:" + c.getEvent().getId() + "не найдено!")
                )));
                if (c.getCommentStatus() == CommentStatus.CONFIRMED) {
                    commentDtoResponseConfirmedList.add(commentDtoResponse);
                } else if (c.getCommentStatus() == CommentStatus.REJECTED) {
                    commentDtoResponseRejectedList.add(commentDtoResponse);
                }
                commentRepository.save(c);
            }
        }
        return CommentStatusUpdateResult.builder()
                .confirmedRequests(commentDtoResponseConfirmedList)
                .rejectedRequests(commentDtoResponseRejectedList)
                .build();
    }
}
