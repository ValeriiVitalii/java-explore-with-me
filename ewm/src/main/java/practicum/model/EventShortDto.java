package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class EventShortDto {

    Long id;

    @NotNull
    String annotation;

    @NotNull
    CategoryDto category;

    int confirmedRequests;

    @NotNull
    String eventDate;

    @NotNull
    UserShortDto initiator;

    @NotNull
    boolean paid;

    @NotNull
    String title;

    Long views;
}
