package org.example;

import org.example.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Hit, Integer> {
}
