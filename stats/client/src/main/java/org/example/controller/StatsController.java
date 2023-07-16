package main.java.org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.java.org.example.client.StClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class StatsController {

    private StClient stClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam("start") String start,
                                           @RequestParam("end") String end,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return stClient.getStats(start, end, uris, unique);
    }
}
