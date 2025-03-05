package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.MentorDto;
import com.dekhokaun.mindarobackend.payload.response.MentorResponse;
import com.dekhokaun.mindarobackend.service.MentorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<MentorResponse>> getMentors() {
//        return ResponseEntity.ok(mentorService.getMentors());
//    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MentorDto>> getMentorsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(mentorService.getMentorsByCategory(category));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMentor(@PathVariable String name) {
        mentorService.deleteMentor(name);
        return ResponseEntity.noContent().build();
    }
}
