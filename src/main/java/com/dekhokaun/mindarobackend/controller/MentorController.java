package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.MentorDto;
import com.dekhokaun.mindarobackend.payload.response.MentorResponse;
import com.dekhokaun.mindarobackend.service.MentorService;
import org.springframework.data.domain.Page;
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

//    @GetMapping("/category/{category}")
//    public ResponseEntity<Page<MentorDto>> getMentorsByCategory(
//            @PathVariable String category,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Page<MentorDto> mentorPage = mentorService.getMentorsByCategory(category, page, size);
//        return ResponseEntity.ok(mentorPage);
//    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMentor(@PathVariable String name) {
        mentorService.deleteMentor(name);
        return ResponseEntity.noContent().build();
    }
}
