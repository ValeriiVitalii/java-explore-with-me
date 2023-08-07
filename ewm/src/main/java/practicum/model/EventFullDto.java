package practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.Create;
import practicum.utility.EventState;
import practicum.utility.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class EventFullDto {

    Long id;

    @NotNull(groups = Create.class)
    @Size(min = 20, message = "Аннотация слишком короткая (мин 20)")
    @Size(max = 2000, message = "Аннотация слишком длинная (макс 2000)")
    String annotation;

    @NotNull(groups = Create.class)
    CategoryDto category;

    Integer confirmedRequests;

    String createdOn;

    @Size(min = 20, message = "Описание слишком короткое (мин 20)")
    @Size(max = 7000, message = "Описание слишком длинное (макс 7000)")
    String description;

    @NotNull(groups = Create.class)
    String eventDate;

    @NotNull(groups = Create.class)
    UserShortDto initiator;

    @NotNull(groups = Create.class)
    Location location;

    @NotNull(groups = Create.class)
    boolean paid;

    Integer participantLimit;

    String publishedOn;

    boolean requestModeration;

    EventState state;

    @NotNull(groups = Create.class)
    @Size(min = 3, message = "Название слишком короткое (мин 3)")
    @Size(max = 120, message = "Название слишком длинное (макс 120)")
    String title;

    Long views;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<CommentDtoShort> comments;

}
