package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for compilation object
 */
public class CompilationMapper {

    /**
     * Transform compilation to compilationDto object
     *
     * @param compilation to transform
     * @return compilationDto object
     */
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                .build();
    }

    /**
     * Transform newCompilationDto to compilation object
     *
     * @param newCompilation to transform
     * @param events         list of event objects
     * @return compilation object
     */
    public static Compilation toCompilation(NewCompilationDto newCompilation, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilation.isPinned())
                .title(newCompilation.getTitle())
                .events(events)
                .build();
    }


    /**
     * Transform newCompilationDto to compilation object
     *
     * @param newCompilation to transform
     * @param oldCompilation compilation object
     * @param events         list of event objects
     * @return compilation object
     */
    public static Compilation toCompilation(UpdateCompilationRequestDto newCompilation, Compilation oldCompilation, List<Event> events) {
        return Compilation.builder()
                .id(oldCompilation.getId())
                .pinned(newCompilation.getPinned() == null
                        ? oldCompilation.isPinned() : newCompilation.getPinned())
                .title(newCompilation.getTitle() == null
                        ? oldCompilation.getTitle() : newCompilation.getTitle())
                .events(events)
                .build();
    }


    /**
     * Transform list of compilations to list of compilationDto objects
     *
     * @param compilationList to transform
     * @return list of compilationDto objects
     */
    public static List<CompilationDto> toListCompilationDto(List<Compilation> compilationList) {
        return compilationList.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

}
