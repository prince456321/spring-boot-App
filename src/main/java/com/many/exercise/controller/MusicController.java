package com.many.exercise.controller;

import com.many.exercise.entity.Artist;
import com.many.exercise.entity.Track;
import com.many.exercise.repository.ArtistRepository;
import com.many.exercise.repository.MusicRepository;
import com.many.exercise.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/track")
public class MusicController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    MusicRepository musicRepository;


    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getCategories(@RequestHeader(value="size") Long size) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> tracks = musicRepository.getLatest(size);
            map.put("data", tracks);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopular(@RequestHeader(value="size") Long size) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> tracks = musicRepository.getPopular(size);
            map.put("data", tracks);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/artist/{id}")
    public ResponseEntity<Map<String, Object>> getByArtist(@RequestHeader(value="size") Long size, @PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> tracks = musicRepository.getByArtist(size, id);
            map.put("data", tracks);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> getByCategory(@RequestHeader(value="size") Long size, @PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> tracks = musicRepository.getByCategory(size, id);
            map.put("data", tracks);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/album/{id}")
    public ResponseEntity<Map<String, Object>> getByAlbum(@RequestHeader(value="size") Long size, @PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> tracks = musicRepository.getByAlbum(size, id);
            map.put("data", tracks);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
