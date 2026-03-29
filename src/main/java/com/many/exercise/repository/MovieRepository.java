package com.many.exercise.repository;
import com.many.exercise.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByName(String name);
}
