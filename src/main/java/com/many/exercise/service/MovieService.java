package com.many.exercise.service;
import com.many.exercise.entity.Track;

import java.util.List;

public interface MovieService {
    List<Track> findAll();
    Track findById(Long id);
    Track save(Track track);
    Boolean deleteById(Long id);
}
