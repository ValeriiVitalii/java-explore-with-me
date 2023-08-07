package practicum.service.publicService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.model.CompilationDto;
import practicum.repository.CompilationRepository;
import practicum.utility.mappers.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceDao implements PublicCompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("По данному id подборка не найдена!")
        ));
    }
}
