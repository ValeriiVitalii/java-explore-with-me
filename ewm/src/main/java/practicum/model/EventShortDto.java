package practicum.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class EventShortDto {

    private Long id;

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    private int confirmedRequests;

    @NotNull
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private boolean paid;

    @NotNull
    private String title;

    private Long views;
}
