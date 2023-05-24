package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Hit;
import org.example.StatsRepository;
import org.example.model.Stats;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServiceDao implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public Hit addStats(Hit hit) {
        statsRepository.save(hit);
        return hit;
    }

    private Stats toStats(Hit hit) {
        return Stats.builder()
                .uri(hit.getUri())
                .ip(hit.getIp())
                .build();
    }
}
