package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.SliderRequest;
import com.dekhokaun.mindarobackend.payload.response.SliderResponse;
import com.dekhokaun.mindarobackend.service.SliderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slider")
@Tag(name = "Slider Controller", description = "APIs for managing sliders")
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @Operation(summary = "Update slider", description = "Update an existing slider or create a new one")
    @PostMapping("/update")
    public ResponseEntity<SliderResponse> updateSlider(@RequestBody SliderRequest request) {
        return ResponseEntity.ok(sliderService.updateSlider(request));
    }

    @Operation(summary = "Get all sliders", description = "Retrieve a list of all sliders")
    @GetMapping("/list")
    public ResponseEntity<List<SliderResponse>> getSliders() {
        return ResponseEntity.ok(sliderService.getAllSliders());
    }
}
