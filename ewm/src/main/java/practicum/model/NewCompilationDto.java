package practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import practicum.utility.Create;
import practicum.utility.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewCompilationDto {
    Boolean pinned;
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    String title;
    List<Long> events;
}
