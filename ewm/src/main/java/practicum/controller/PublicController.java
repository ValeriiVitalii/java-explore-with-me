package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practicum.exception.ValidationException;
import practicum.model.CategoryDto;
import practicum.model.CompilationDto;
import practicum.model.EventFullDto;
import practicum.service.publicService.PublicCategoryService;
import practicum.service.publicService.PublicCompilationService;
import practicum.service.publicService.PublicEventService;
import practicum.utility.EWMDateTimePattern;
import practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class PublicController {

    private final PublicCategoryService publicCategoryService;

    private final PublicCompilationService publicCompilationService;

    private PublicEventService publicEventService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategory(@RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return publicCategoryService.getCategory(PageableMaker.makePageable(from, size));
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable @NotNull Long catId) {
        return publicCategoryService.getCategoryById(catId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam (required = false) Boolean pinned,
                                                @RequestParam (required = false, defaultValue = "0") Integer from,
                                                @RequestParam (required = false, defaultValue = "10") Integer size) {
        return publicCompilationService.getCompilations(pinned, PageableMaker.makePageable(from, size));
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return publicCompilationService.getCompilationById(compId);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return publicEventService.getEventById(httpServletRequest, id);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsFiltered(HttpServletRequest httpServletRequest,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        if (httpServletRequest.getParameter("rangeStart") == null && httpServletRequest.getParameter("rangeEnd") == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        if (httpServletRequest.getParameter("rangeStart") == null)
            rangeStart = LocalDateTime.now().minusYears(10);
        if (httpServletRequest.getParameter("rangeEnd") == null)
            rangeEnd = LocalDateTime.now().plusYears(10);

        if (rangeEnd.isBefore(LocalDateTime.now()))
            throw new ValidationException("Дата события не может быть в прошлом");

        return publicEventService.getEventsFiltered(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                PageableMaker.makePageable(from, size), httpServletRequest);
    }
}

