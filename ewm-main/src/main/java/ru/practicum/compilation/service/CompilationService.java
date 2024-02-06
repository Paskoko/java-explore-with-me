package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;

import java.util.List;

/**
 * Interface for compilation service
 */
public interface CompilationService {

    /**
     * Add new compilation
     *
     * @param compilation to add
     * @return added compilation
     */
    CompilationDto createCompilation(NewCompilationDto compilation);

    /**
     * Delete compilation by id
     *
     * @param id of compilation
     */
    void deleteCompilation(int id);

    /**
     * Update compilation by id
     *
     * @param compilation to update
     * @param id          of the compilation
     * @return updated compilation
     */
    CompilationDto updateCompilation(UpdateCompilationRequestDto compilation, int id);

    /**
     * Get all compilations
     * with pagination
     *
     * @param pinned to find by
     * @param from   index of the first element
     * @param size   number of elements to return
     * @return list of compilations
     */
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Get compilation by id
     *
     * @param id of the compilation
     * @return compilation
     */
    CompilationDto getCompilationById(int id);
}
