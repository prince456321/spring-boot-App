package com.many.exercise.repository;

import com.many.exercise.entity.Artist;
import com.many.exercise.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
