package com.many.exercise.repository;

import com.many.exercise.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Track, Long> {

    Page<Track> findAll(Pageable pageable);

    @Query(
            value = "SELECT * FROM track order by id desc limit :size",
            nativeQuery = true
    )
    List<Track> getLatest(@Param("size") Long size);

    @Query(
            value = "SELECT * FROM track order by view desc limit :size",
            nativeQuery = true
    )
    List<Track> getPopular(@Param("size") Long size);

    @Query(
            value = "SELECT * FROM track where artist_id=:id order by id desc limit :size",
            nativeQuery = true
    )
    List<Track> getByArtist(@Param("size") Long size, @Param("id") Long id);

    @Query(
            value = "SELECT * FROM track where category_id=:id order by id desc limit :size",
            nativeQuery = true
    )
    List<Track> getByCategory(@Param("size") Long size, @Param("id") Long id);

    @Query(
            value = "SELECT * FROM track where album_id=:id order by id desc limit :size",
            nativeQuery = true
    )
    List<Track> getByAlbum(@Param("size") Long size, @Param("id") Long id);


    Optional<Track> findByOldId(Long id);
}
