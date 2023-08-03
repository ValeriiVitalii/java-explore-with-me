package practicum.model;

import lombok.Builder;
import lombok.Data;
import practicum.utility.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, message = "Аннотация слишком короткая (мин 20)")
    @Size(max = 2000, message = "Аннотация слишком длинная (макс 2000)")
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, message = "Описание слишком короткое (мин 20)")
    @Size(max = 7000, message = "Описание слишком длинное (макс 7000)")
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private boolean paid;

    private int participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @Size(min = 3, message = "Название слишком короткое (мин 3)")
    @Size(max = 120, message = "Название слишком длинное (макс 120)")
    private String title;
}
