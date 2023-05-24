package org.example;

import lombok.AllArgsConstructor;
import org.example.model.Hit;
import org.example.service.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/hit")
public class StatsController {

    StatsService service;

    @PostMapping
    public Hit postHit(@RequestBody Hit hit) {
        return service.addStats(hit);
    }
}
