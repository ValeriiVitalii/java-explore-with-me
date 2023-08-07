package practicum.service.adminService;

import practicum.model.dto.CompilationDto;
import practicum.model.dto.NewCompilationDto;

public interface AdminCompilationsService {
    CompilationDto postNewCompilation(NewCompilationDto newCompilationDto);

    CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);
}
