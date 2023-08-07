package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Mapper;
import org.example.StatsRepository;
import org.example.model.HitDto;
import org.example.model.Stats;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServiceDao implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void addStats(HitDto hitDto) {
        statsRepository.save(Mapper.toHit(hitDto));
    }

    @Override
    public List<Stats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique) {
        List<Stats> stats = new ArrayList<>();

        if (uris == null || uris.length == 0) {
            if (isUnique) {
                stats.addAll(statsRepository.findAllByCreatedBetweenWithUnique(start, end));
            } else {
                stats.addAll(statsRepository.findAllByCreatedBetweenWithoutUnique(start, end));
            }
        } else {
            if (isUnique) {
                stats.addAll(statsRepository.findByCreatedBetweenWithUniqueIpIsInUris(start, end, uris));
            } else {
                stats.addAll(statsRepository.findAllByCreatedBetweenWithoutUniqueIpIsInUris(start, end, uris));
            }
        }
        return stats;
    }
}


