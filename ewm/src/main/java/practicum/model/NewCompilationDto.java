package practicum.model;

import lombok.Builder;
import lombok.Data;
import practicum.utility.Create;
import practicum.utility.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private Boolean pinned;
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    private String title;
    private List<Long> events;
}
