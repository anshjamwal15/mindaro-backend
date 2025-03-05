package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.SliderDto;
import com.dekhokaun.mindarobackend.payload.request.SliderRequest;
import com.dekhokaun.mindarobackend.payload.response.SliderResponse;
import com.dekhokaun.mindarobackend.service.SliderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slider")
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @PostMapping("/update")
    public ResponseEntity<SliderResponse> updateSlider(@RequestBody SliderRequest request) {
        return ResponseEntity.ok(sliderService.updateSlider(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SliderDto>> getSliders() {
        return ResponseEntity.ok(sliderService.getAllSliders());
    }
}
