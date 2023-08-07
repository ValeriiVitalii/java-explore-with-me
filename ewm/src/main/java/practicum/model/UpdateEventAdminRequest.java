package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.EventAdminState;
import practicum.utility.Location;

import javax.validation.constraints.Size;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class UpdateEventAdminRequest {

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

    EventAdminState stateAction;

    @Size(max = 120)
    @Size(min = 3)
    String title;

}
