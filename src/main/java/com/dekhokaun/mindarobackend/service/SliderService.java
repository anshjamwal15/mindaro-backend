package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Slider;
import com.dekhokaun.mindarobackend.payload.request.SliderRequest;
import com.dekhokaun.mindarobackend.payload.response.SliderResponse;
import com.dekhokaun.mindarobackend.repository.SliderRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SliderService {

    private final SliderRepository sliderRepository;

    public SliderService(SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    public SliderResponse addSlider(SliderRequest request) {
        Slider slider = ObjectMapperUtils.map(request, Slider.class);
        sliderRepository.save(slider);
        return ObjectMapperUtils.map(slider, SliderResponse.class);
    }

    public List<SliderResponse> getAllSliders() {
        return sliderRepository.findAll().stream()
                .map(slider -> ObjectMapperUtils.map(slider, SliderResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteSlider(String id) {
        sliderRepository.deleteById(UUID.fromString(id));
    }

    public SliderResponse updateSlider(SliderRequest request) {
//        Slider slider = sliderRepository.findById(request.ge())
//                .orElseThrow(() -> new InvalidRequestException("Slider not found"));
//
//        slider.setTitle(request.getTitle());
//        slider.setImageUrl(request.getImageUrl());
//
//        sliderRepository.save(slider);
//
//        return new SliderResponse(slider.getId(), slider.getTitle(), slider.getImageUrl());
        return new SliderResponse();
    }
}
