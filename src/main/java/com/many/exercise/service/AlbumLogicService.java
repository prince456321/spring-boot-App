package com.many.exercise.service;

import com.many.exercise.entity.Album;

public class AlbumLogicService {

    // Règle 1 : un album est populaire si views >= 1000
    public boolean isPopular(Album album) {
        if (album == null) {
            return false;
        }
        return album.getView() >= 1000;
    }

    // Règle 2 : un album est valide si le titre n'est pas vide
    public boolean hasValidTitle(Album album) {
        if (album == null) {
            return false;
        }
        String title = album.getTitle();
        return title != null && !title.trim().isEmpty();
    }
}
