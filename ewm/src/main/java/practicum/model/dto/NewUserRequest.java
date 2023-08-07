package practicum.model.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewUserRequest {

    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    String email;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 250)
    String name;

}
