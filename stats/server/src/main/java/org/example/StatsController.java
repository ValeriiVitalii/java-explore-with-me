package org.example;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.HitDto;
import org.example.model.Stats;
import org.example.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
@Slf4j
public class StatsController {

    private final StatsService service;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public void postEndpointHit(HttpServletRequest request,
                                                  @RequestBody HitDto hitDto) {
        log.info("client ip: {}, endpoint path: {}", request.getRemoteAddr(), request.getRequestURI());
        service.addStats(hitDto);
    }

    @GetMapping("/stats")
    public List<Stats> getStats(@RequestParam("start") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                @RequestParam("end") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                @RequestParam(required = false) String[] uris,
                                @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
