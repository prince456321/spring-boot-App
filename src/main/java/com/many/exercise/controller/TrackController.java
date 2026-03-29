package com.many.exercise.controller;

import com.many.exercise.entity.Artist;
import com.many.exercise.entity.Category;
import com.many.exercise.entity.Track;
import com.many.exercise.repository.ArtistRepository;
import com.many.exercise.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
public class TrackController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    TrackRepository trackRepository;


    @GetMapping()
    public ResponseEntity<Map<String, Object>> geLatest() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> artists = trackRepository.findAll();
            map.put("data", artists);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/get-by-artist/{id}")
    public ResponseEntity<Map<String, Object>> getTrackByArtist(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            final String uri = "http://188.166.244.137/music/getListSongBySinger/"+id+"/1000000/0";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            System.out.println(result);
            map.put("data", result);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/get-by-category/{id}")
    public ResponseEntity<Map<String, Object>> getTrackByCategory(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            final String uri = "http://188.166.244.137/music/getListSongByCategoryId/"+id+"/1000000/0";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            System.out.println(result);
            map.put("data", result);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/get-by-album/{id}")
    public ResponseEntity<Map<String, Object>> getTrackByAlbum(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            final String uri = "http://188.166.244.137/music/getListSongByAlbum/"+id+"/1000000/0";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            System.out.println(result);
            map.put("data", result);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Track track) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track saved = trackRepository.save(track);
            map.put("data", saved);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Create Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PostMapping("/save-by-category")
    public ResponseEntity<Map<String, Object>> saveByCatetory(@RequestBody Track track) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track findById = trackRepository.findByOldId(track.getOldId()).orElse(null);
            if(findById==null){
                Track saved = trackRepository.save(track);
                map.put("data", saved);
                map.put("action", "create");
            }else{
                findById.setCategory(track.getCategory());
                Track saved = trackRepository.save(findById);
                map.put("data", saved);
                map.put("action", "update");
            }
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Create Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PostMapping("/save-by-album")
    public ResponseEntity<Map<String, Object>> saveByAlbum(@RequestBody Track track) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track findById = trackRepository.findByOldId(track.getOldId()).orElse(null);
            if(findById==null){
                Track saved = trackRepository.save(track);
                map.put("data", saved);
                map.put("action", "create");
            }else{
                findById.setAlbum(track.getAlbum());
                Track saved = trackRepository.save(findById);
                map.put("data", saved);
                map.put("action", "update");
            }
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Create Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable("id") long id, @RequestBody Artist artist) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Artist findById = artistRepository.findById(id).orElse(null);
            if(findById.getId()>0){
                Artist updated = artistRepository.save(findById);
                map.put("data", updated);
                map.put("status", true);
            }else{
                map.put("error", "Not found category");
                map.put("status", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Update Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Artist findById = artistRepository.findById(id).orElse(null);
            if(findById.getId()>0){
//                Boolean deleted = artistRepository.deleteById(findById.getId());
//                map.put("status", deleted);
            }else{
                map.put("error", "Not found category");
                map.put("status", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Delete Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
