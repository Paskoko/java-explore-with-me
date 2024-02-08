package ru.practicum.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

/**
 * Class controller for compilations for public
 */
@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /**
     * GET request handler to get list of compilations
     * with pagination
     *
     * @param pinned to find by
     * @param from   index of the first element
     *               default value = 0
     * @param size   number of elements to return
     *               default value = 10
     * @return list of compilations
     */
    @GetMapping(value = "/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(
            @RequestParam(value = "pinned", required = false) Boolean pinned,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * GET request handler to get compilation by id
     *
     * @param compId of the compilation
     * @return compilation
     */
    @GetMapping(value = "/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilation(@PathVariable int compId) {
        return compilationService.getCompilationById(compId);
    }


}
