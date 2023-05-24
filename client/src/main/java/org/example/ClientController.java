package org.example;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/hit")
@AllArgsConstructor
@Slf4j
public class ClientController {

    private Client client;


    /*@PostMapping("/hit")
    public Hit handleHitRequest(@RequestBody Hit hit)
        return hit;
    }*/

    @PostMapping
    public ResponseEntity<Object> postBooking(@Valid @RequestBody Hit hit) {
        log.info("ClientController отправил post");
        return client.postHit(hit);
    }
}
