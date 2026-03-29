package com.many.exercise.controller;

import com.many.exercise.entity.Album;
import com.many.exercise.entity.Artist;
import com.many.exercise.repository.AlbumRepository;
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
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/app")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Album> albums = albumRepository.findAll();
            map.put("data", albums);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found data");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
