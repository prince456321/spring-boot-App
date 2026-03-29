package com.many.exercise.controller;
import com.many.exercise.entity.Track;
import com.many.exercise.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getMovies() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Track> categories = movieService.findAll();
            map.put("data", categories);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found movies");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMovieById(@PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track track = movieService.findById(id);
            if(track.getId()>0){
                map.put("data", track);
                map.put("status", true);
            }else{
                map.put("error", "Not found movie");
                map.put("status", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Not found movie");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Map<String, Object>> saveMovie(@RequestBody Track track) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {


        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "Create Failed");
            map.put("status", false);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMovie(@PathVariable("id") long id, @RequestBody Track track) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track findById = movieService.findById(id);
            if(findById.getId()>0){
                findById.setName(track.getName());
                findById.setCategory(track.getCategory());
            }else{
                map.put("error", "Not found movie");
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
    public ResponseEntity<Map<String, Object>> deleteMovie(@PathVariable("id") long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Track findById = movieService.findById(id);
            if(findById.getId()>0){
                Boolean deleted = movieService.deleteById(findById.getId());
                map.put("status", deleted);
            }else{
                map.put("error", "Not found movie");
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
