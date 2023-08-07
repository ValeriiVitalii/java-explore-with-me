package practicum.utility.mappers;

import lombok.experimental.UtilityClass;
import practicum.model.Compilation;
import practicum.model.dto.CompilationDto;
import practicum.model.dto.NewCompilationDto;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .id(compilation.getId())
                .build();

        Optional.ofNullable(compilation.getEvents()).ifPresent(
                list -> compilationDto.setEvents(list.stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList())));

        return compilationDto;
    }

    public Compilation fromNewCompilationDto(@NotNull NewCompilationDto newCompilationDto) {
        Compilation compilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .build();

        Optional.ofNullable(newCompilationDto.getPinned()).ifPresent(compilation::setPinned);

        return compilation;
    }

}
