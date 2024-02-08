package ru.practicum.compilation.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.CompilationMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.QCompilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.util.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class service for operations with compilations storage
 */
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }


    /**
     * Add new compilation
     *
     * @param compilation to add
     * @return added compilation
     */
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilation) {
        List<Event> events = new ArrayList<>();
        if (compilation.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilation.getEvents());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(
                CompilationMapper.toCompilation(compilation, events)));
    }

    /**
     * Delete compilation by id
     *
     * @param id of compilation
     */
    @Override
    public void deleteCompilation(int id) {
        if (compilationRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Category with id=" + id + " was not found.");
        }
        compilationRepository.deleteById(id);
    }

    /**
     * Update compilation by id
     *
     * @param compilation to update
     * @param id          of the compilation
     * @return updated compilation
     */
    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequestDto compilation, int id) {
        Compilation compilationToUpd = compilationRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Category with id=" + id + " was not found."));


        List<Event> events = new ArrayList<>();
        if (compilation.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilation.getEvents());
        }
        compilationToUpd = CompilationMapper.toCompilation(compilation, compilationToUpd, events);

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilationToUpd));
    }

    /**
     * Get all compilations
     * with pagination
     *
     * @param pinned to find by
     * @param from   index of the first element
     * @param size   number of elements to return
     * @return list of compilations
     */
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned != null) {
            BooleanExpression byPinned = QCompilation.compilation.pinned.eq(pinned);

            return CompilationMapper.toListCompilationDto(compilationRepository.findAll(byPinned, page).getContent());
        }
        return CompilationMapper.toListCompilationDto(compilationRepository.findAll(page).getContent());
    }

    /**
     * Get compilation by id
     *
     * @param id of the compilation
     * @return compilation
     */
    @Override
    public CompilationDto getCompilationById(int id) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compilation with id=" + id + " was not found.")));
    }
}
