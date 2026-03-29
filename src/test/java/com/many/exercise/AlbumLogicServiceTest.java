package com.many.exercise.service;

import com.many.exercise.entity.Album;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlbumLogicServiceTest {

    private final AlbumLogicService service = new AlbumLogicService();

    @Test
    void shouldReturnTrueWhenAlbumIsPopular() {
        Album album = new Album();
        album.setView(1500L);

        boolean result = service.isPopular(album);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenTitleIsEmpty() {
        Album album = new Album();
        album.setTitle("   ");

        boolean result = service.hasValidTitle(album);

        assertFalse(result);
    }
}
