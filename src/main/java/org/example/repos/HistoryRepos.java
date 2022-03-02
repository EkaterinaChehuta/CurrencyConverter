package org.example.repos;

import org.example.entities.Conversion;
import org.example.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepos extends JpaRepository<History, Integer> {
    History findByConversion(Conversion conversion);
}
