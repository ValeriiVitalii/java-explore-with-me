package practicum.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;

    private HttpStatus status;

    private String timestamp;
}
