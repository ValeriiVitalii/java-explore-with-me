package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.Location;
import practicum.utility.mappers.EventUserState;

import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class UpdateEventUserRequest {

    @Size(max = 2000)
    @Size(min = 20)
    String annotation;

    Long category;
    @Size(max = 7000)
    @Size(min = 20)

    String description;

    String eventDate;

    Location location;

    Boolean paid;

    int participantLimit;

    Boolean requestModeration;

    EventUserState stateAction;

    @Size(max = 120)
    @Size(min = 3)
    String title;
}
