package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class CompilationDto {

    @NotNull
    Long id;

    @NotNull
    boolean pinned;

    @NotNull
    String title;

    List<EventShortDto> events;
}
