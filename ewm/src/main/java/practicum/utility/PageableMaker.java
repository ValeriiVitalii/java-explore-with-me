package practicum.utility;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PageableMaker {
    public Pageable makePageable(Integer from, Integer size) {
        if (from == null || size == null) {
            return null;
        }

        if (from < 0 || size <= 0) {
            //throw new ValidationException("Неправильно указанны параметры для просмотра!");
        }

        return PageRequest.of(from / size, size);
    }

}
