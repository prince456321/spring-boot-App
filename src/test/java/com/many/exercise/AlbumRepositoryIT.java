package com.many.exercise;

import com.many.exercise.entity.Album;
import com.many.exercise.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class AlbumRepositoryIT {

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    void testSaveAndFindAlbum() {
        Album album = new Album();
        album.setTitle("Test Album");
        album.setView(100L);
        album.setCover("cover.jpg");
        album.setSong(10L);

        Album saved = albumRepository.save(album);
        Album found = albumRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Test Album", found.getTitle());
    }

    @Test
    void testFindAllAlbums() {
        Album album = new Album();
        album.setTitle("Second Album");
        album.setView(200L);
        album.setCover("second.jpg");
        album.setSong(12L);

        albumRepository.save(album);

        List<Album> albums = albumRepository.findAll();

        assertNotNull(albums);
        assertFalse(albums.isEmpty());
    }

    @Test
    void testDeleteAlbum() {
        Album album = new Album();
        album.setTitle("Delete Album");
        album.setView(300L);
        album.setCover("delete.jpg");
        album.setSong(8L);

        Album saved = albumRepository.save(album);
        Long albumId = saved.getId();

        albumRepository.deleteById(albumId);

        Album deleted = albumRepository.findById(albumId).orElse(null);

        assertNull(deleted);
    
    }

    @Test
    void shouldSaveAndRetrieveAlbumCorrectly() {

    // GIVEN
    Album album = new Album();
    album.setTitle("Mon Album Test");
    album.setView(100L);
    album.setCover("cover.jpg");
    album.setSong(10L);

    // WHEN
    Album savedAlbum = albumRepository.save(album);

    Optional<Album> foundAlbumOpt = albumRepository.findById(savedAlbum.getId());

    // THEN
    assertTrue(foundAlbumOpt.isPresent(), "Album should be present");

    Album foundAlbum = foundAlbumOpt.get();

    assertEquals("Mon Album Test", foundAlbum.getTitle());
    assertEquals(100L, foundAlbum.getView());
  } 





}
