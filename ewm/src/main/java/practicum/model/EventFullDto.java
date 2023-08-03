package practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import practicum.utility.Create;
import practicum.utility.EventState;
import practicum.utility.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class EventFullDto {

    private long id;

    @NotNull(groups = Create.class)
    @Size(min = 20, message = "Аннотация слишком короткая (мин 20)")
    @Size(max = 2000, message = "Аннотация слишком длинная (макс 2000)")
    private String annotation;

    @NotNull(groups = Create.class)
    private CategoryDto category;

    private Integer confirmedRequests;

    private String createdOn;

    @Size(min = 20, message = "Описание слишком короткое (мин 20)")
    @Size(max = 7000, message = "Описание слишком длинное (макс 7000)")
    private String description;

    @NotNull(groups = Create.class)
    private String eventDate;

    @NotNull(groups = Create.class)
    private UserShortDto initiator;

    @NotNull(groups = Create.class)
    private Location location;

    @NotNull(groups = Create.class)
    private boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private EventState state;

    @NotNull(groups = Create.class)
    @Size(min = 3, message = "Название слишком короткое (мин 3)")
    @Size(max = 120, message = "Название слишком длинное (макс 120)")
    private String title;

    private Long views;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDtoShort> comments;

}
