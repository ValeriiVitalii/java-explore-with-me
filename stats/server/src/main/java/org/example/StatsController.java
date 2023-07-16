package org.example;

import lombok.AllArgsConstructor;
import org.example.model.HitDto;
import org.example.model.Stats;
import org.example.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class StatsController {

    private StatsService service;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public void postHit(@RequestBody HitDto hitDto) {
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
