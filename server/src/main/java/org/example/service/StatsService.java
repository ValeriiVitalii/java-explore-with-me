package org.example.service;

import org.example.model.HitDto;
import org.example.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void addStats(HitDto dto);

    List<Stats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean isUnique);
}
