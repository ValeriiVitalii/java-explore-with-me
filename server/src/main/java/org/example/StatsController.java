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

    StatsService service;

    @PostMapping("/hit")
    public void postHit(@RequestBody HitDto hitDto) {
        service.addStats(hitDto);
    }

    @GetMapping("/stats")
    public List<Stats> getStats(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                @RequestParam(required = false) String[] uris,
                                @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
