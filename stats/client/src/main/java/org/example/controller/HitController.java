package main.java.org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.java.org.example.client.HitClient;
import main.java.org.example.model.HitDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class HitController {

    private HitClient hitClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> postHit(@Valid @RequestBody HitDto hitDto) {
        return hitClient.postHit(hitDto);
    }
}
