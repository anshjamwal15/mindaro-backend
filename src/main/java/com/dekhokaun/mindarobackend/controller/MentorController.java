package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentor")
@Tag(name = "Mentor Controller", description = "APIs for managing mentors")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

//    @Operation(summary = "Get all mentors", description = "Retrieves a list of all mentors")
//    @GetMapping("/list")
//    public ResponseEntity<List<MentorResponse>> getMentors() {
//        return ResponseEntity.ok(mentorService.getMentors());
//    }

//    @Operation(summary = "Get mentors by category", description = "Retrieves a paginated list of mentors filtered by category")
//    @GetMapping("/category/{category}")
//    public ResponseEntity<Page<MentorDto>> getMentorsByCategory(
//            @Parameter(description = "Category of the mentor", required = true) @PathVariable String category,
//            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
//            @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int size) {
//
//        Page<MentorDto> mentorPage = mentorService.getMentorsByCategory(category, page, size);
//        return ResponseEntity.ok(mentorPage);
//    }

    @Operation(summary = "Delete a mentor", description = "Deletes a mentor by their name")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteMentor(
            @Parameter(description = "Name of the mentor to delete", required = true) @PathVariable String name) {
        mentorService.deleteMentor(name);
        return ResponseEntity.noContent().build();
    }
}
