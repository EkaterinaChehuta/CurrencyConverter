package org.example.repos;

import org.example.entities.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepos extends JpaRepository<Conversion, Integer> {
}
