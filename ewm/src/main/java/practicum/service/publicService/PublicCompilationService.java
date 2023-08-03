package practicum.service.publicService;


import org.springframework.data.domain.Pageable;
import practicum.model.CompilationDto;


import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);

}
