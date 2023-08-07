package practicum.service.publicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practicum.exception.NotFoundException;
import practicum.model.Comment;
import practicum.model.dto.CommentDtoResponse;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.UserRepository;
import practicum.utility.mappers.CommentMapper;
import practicum.utility.mappers.EventMapper;
import practicum.utility.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCommentServiceDao implements PublicCommentService {

    private final EventRepository eventRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    @Override
    public List<CommentDtoResponse> getComments(Long eventId) {

        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        List<CommentDtoResponse> commentDtoResponseList = new ArrayList<>();

        for (Comment c : comments) {
            CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
            commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(c.getCommenter().getId()).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден!")
            )));
            commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(eventId).orElseThrow(
                    () -> new NotFoundException("Событие не найдено!")
            )));
            commentDtoResponseList.add(commentDtoResponse);
        }
        return commentDtoResponseList;
    }

    @Override
    public CommentDtoResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден!")
        );
        CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(comment);
        commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(comment.getEvent().getId()).orElseThrow(
                () -> new NotFoundException("Событие не найдено!")
        )));
        commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(comment.getCommenter().getId()).orElseThrow(
                () -> new NotFoundException("Пользователь не найден!")
        )));
        return commentDtoResponse;
    }
}
