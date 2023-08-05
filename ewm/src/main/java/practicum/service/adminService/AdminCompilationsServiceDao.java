package practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.NotFoundException;
import practicum.model.Compilation;
import practicum.model.CompilationDto;
import practicum.model.NewCompilationDto;
import practicum.repository.CompilationRepository;
import practicum.repository.EventRepository;
import practicum.utility.mappers.CompilationMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceDao implements AdminCompilationsService{

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto postNewCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.fromNewCompilationDto(newCompilationDto);

        if (Optional.ofNullable(newCompilationDto.getEvents()).isPresent())
            compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с айди:" + compId + "не найдена!")
        );

        Optional.ofNullable(newCompilationDto.getEvents()).ifPresent(ge -> compilation.setEvents(eventRepository.findAllById(ge)));

        Optional.ofNullable(newCompilationDto.getTitle()).ifPresent(compilation::setTitle);

        Optional.ofNullable(newCompilationDto.getPinned()).ifPresent(compilation::setPinned);

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }
}
