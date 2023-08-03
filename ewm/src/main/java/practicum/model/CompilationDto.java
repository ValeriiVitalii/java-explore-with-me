package practicum.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class CompilationDto {

    @NotNull
    private Long id;

    @NotNull
    private boolean pinned;

    @NotNull
    private String title;

    private List<EventShortDto> events;

}
