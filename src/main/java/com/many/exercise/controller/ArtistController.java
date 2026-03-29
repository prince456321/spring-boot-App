package com.many.exercise.controller;

import com.many.exercise.entity.Artist;
import com.many.exercise.entity.Category;
import com.many.exercise.repository.ArtistRepository;
import com.many.exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/app")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Artist> artists = artistRepository.findAll();
            map.put("data", artists);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Artist artist) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Artist saved = artistRepository.save(artist);
            map.put("data", saved);
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
