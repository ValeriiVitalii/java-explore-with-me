package practicum.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, message = "Аннотация слишком короткая (мин 20)")
    @Size(max = 2000, message = "Аннотация слишком длинная (макс 2000)")
    String annotation;

    @NotNull
    Long category;

    @NotBlank
    @Size(min = 20, message = "Описание слишком короткое (мин 20)")
    @Size(max = 7000, message = "Описание слишком длинное (макс 7000)")
    String description;

    @NotNull
    String eventDate;

    @NotNull
    Location location;

    boolean paid;

    int participantLimit;

    Boolean requestModeration;

    @NotBlank
    @Size(min = 3, message = "Название слишком короткое (мин 3)")
    @Size(max = 120, message = "Название слишком длинное (макс 120)")
    String title;
}
