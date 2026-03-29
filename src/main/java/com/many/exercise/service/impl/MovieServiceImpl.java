package com.many.exercise.service.impl;

import com.many.exercise.entity.Track;
import com.many.exercise.repository.MovieRepository;
import com.many.exercise.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Override
    public List<Track> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Track findById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public Track save(Track track) {
        return movieRepository.save(track);
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            movieRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
