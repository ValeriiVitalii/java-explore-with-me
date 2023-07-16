package org.example;

import org.example.model.Hit;

import org.example.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query("SELECT new org.example.model.Stats(h.app, h.uri, COUNT(h.ip)) FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findAllByCreatedBetweenWithoutUniqueIpIsInUris(@Param("start") LocalDateTime start,
                                                               @Param("end") LocalDateTime end,
                                                               @Param("uris") String[] uris);

    @Query("SELECT new org.example.model.Stats(h.app, h.uri, COUNT(DISTINCT h.ip))" +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findByCreatedBetweenWithUniqueIpIsInUris(@Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end,
                                                         @Param("uris") String[] uris);

    @Query("SELECT new org.example.model.Stats(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findAllByCreatedBetweenWithUnique(@Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);

    @Query("SELECT new org.example.model.Stats(h.app, h.uri, COUNT(h.ip)) FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findAllByCreatedBetweenWithoutUnique(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);
}
