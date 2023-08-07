package practicum.service.adminService;

import practicum.model.CompilationDto;
import practicum.model.NewCompilationDto;

public interface AdminCompilationsService {
    CompilationDto postNewCompilation(NewCompilationDto newCompilationDto);

    CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);
}
