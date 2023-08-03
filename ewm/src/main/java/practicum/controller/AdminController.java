package practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import practicum.model.CategoryDto;
import practicum.model.CompilationDto;
import practicum.model.EventFullDto;
import practicum.model.NewCategoryDto;
import practicum.model.NewCompilationDto;
import practicum.model.NewUserRequest;
import practicum.model.UpdateEventAdminRequest;
import practicum.model.UserDto;
import practicum.service.adminService.AdminCategoryService;
import practicum.service.adminService.AdminCompilationsService;
import practicum.service.adminService.AdminEventService;
import practicum.service.adminService.AdminUserService;
import practicum.utility.Create;
import practicum.utility.EWMDateTimePattern;
import practicum.utility.EventState;
import practicum.utility.PageableMaker;
import practicum.utility.Update;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminCategoryService adminCategoryService;

    private final AdminUserService adminUserService;

    private final AdminEventService adminEventService;

    private final AdminCompilationsService adminCompilationsService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoryService.postCategory(newCategoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId, @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoryService.patchCategory(catId, newCategoryDto);
    }

    @DeleteMapping ("/categories/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        adminCategoryService.deleteCategory(catId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUserService.postUser(newUserRequest);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(HttpServletRequest request,
                                  @RequestParam(required = false) List<Long> ids,
                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (request.getParameter("ids") == null)
            ids = new ArrayList<>();
        return adminUserService.getUsers(ids, PageableMaker.makePageable(from, size));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<EventState> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER)
                                             LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER)
                                             LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HttpServletRequest httpServletRequest) {
        return adminEventService.getEventsAdmin(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageableMaker.makePageable(from, size),
                httpServletRequest);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEventAdmin(@PathVariable Long eventId,
                                        @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                                        HttpServletRequest httpServletRequest) {
        return adminEventService.patchEventAdmin(eventId, updateEventAdminRequest, httpServletRequest);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postNewCompilation(@RequestBody
                                             @Validated({Create.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationsService.postNewCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @RequestBody @Validated({Update.class}) NewCompilationDto newCompilationDto) {
        return adminCompilationsService.patchCompilation(compId, newCompilationDto);
    }

    @DeleteMapping("(/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postNewCompilation(@PathVariable Long compId) {
        adminCompilationsService.deleteCompilation(compId);
    }
}
