package practicum.service.privateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.exception.ValidationException;
import practicum.model.Comment;
import practicum.model.dto.CommentDtoResponse;
import practicum.model.dto.CommentStatusUpdateRequest;
import practicum.model.dto.CommentStatusUpdateResult;
import practicum.model.Event;
import practicum.model.dto.NewCommentDto;
import practicum.model.User;
import practicum.model.dto.UserShortDto;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.UserRepository;
import practicum.utility.CommentStatus;
import practicum.utility.EventState;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;
import practicum.utility.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateCommentServiceDao implements PrivateCommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDtoResponse> getCommentByUserId(long userId) {

        List<Comment> comments = commentRepository.findByCommenterId(userId);
        List<CommentDtoResponse> commentDtoResponseList = new ArrayList<>();
        UserShortDto commenter = UserMapper.toUserShortDto(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id:" + userId + "не найден!")
        ));

        for (Comment c : comments) {
            CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
            commentDtoResponse.setCommenter(commenter);
            commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(c.getEvent().getId()).orElseThrow(
                    () -> new NotFoundException("Событие с id:" + c.getEvent().getId() + "не найдено!")
            )));
            commentDtoResponseList.add(commentDtoResponse);
        }
        return commentDtoResponseList;

    }

    @Override
    public CommentDtoResponse patchCancelComment(long userId, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id:" + commentId + "не найден!")
        );

        if (comment.getCommenter().getId() != userId) {
            throw new ValidationException("Отменить можно только свой комментарий");
        }

        if (comment.getCommentStatus() == CommentStatus.CANCELED || comment.getCommentStatus() == CommentStatus.REJECTED) {
            throw new ValidationException("Нельзя отменить отвергнутые или уже отмененные комментарии!");
        }

        comment.setCommentStatus(CommentStatus.CANCELED);
        commentRepository.save(comment);
        CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(comment);
        commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(comment.getCommenter().getId()).orElseThrow(
                () -> new NotFoundException("Пользователь с id:" + userId + "не найден!")
        )));
        commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(comment.getEvent().getId()).orElseThrow(
                () -> new NotFoundException("Событие с id:" + comment.getEvent().getId() + "не найдено!")
        )));

        return commentDtoResponse;
    }

    @Override
    public void deleteComment(long userId, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id:" + commentId + "не найден!")
        );

        if (comment.getCommenter().getId() != userId) {
            throw new ValidationException("Удалить можно только свой комментарий");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDtoResponse postNewComment(long userId, long eventId, NewCommentDto newCommentDto) {
        User commenter = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id:" + userId + "не найден!")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id:" + eventId + "не найдено!")
        );

        if (event.getState() != EventState.PUBLISHED)
            throw new ValidationException("Комментарии можно оставлять только к опубликованным событиям!");

        Comment comment = CommentMapper.fromNewCommentDto(newCommentDto);
        comment.setEvent(event);
        comment.setCommenter(commenter);
        CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(commentRepository.save(comment));
        commentDtoResponse.setCommenter(UserMapper.toUserShortDto(commenter));
        commentDtoResponse.setEvent(EventMapper.toEventShortDto(event));
        return commentDtoResponse;
    }


    @Override
    @Transactional
    public CommentStatusUpdateResult patchStatusComments(long userId,
                                                         long eventId,
                                                         CommentStatusUpdateRequest commentStatusUpdateRequest) {
        if (eventRepository.findById(eventId).orElseThrow(
                (() -> new NotFoundException("Событие не найдено!"))).getInitiator().getId() != userId) {
            throw new ValidationException("Модерировать можно только комментарии к событиям, созданным вами!");
        }

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
                        () -> new NotFoundException("Пользователь с id:" + userId + "не найден!")
                )));
                commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(eventId).orElseThrow(
                        () -> new NotFoundException("Событие с id:" + eventId + "не найдено!")
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
