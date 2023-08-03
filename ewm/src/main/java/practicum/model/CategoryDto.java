package practicum.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CategoryDto {

    private Long id;

    @NotNull
    private String name;
}
