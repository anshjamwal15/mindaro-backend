package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.MentorDto;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.payload.request.MentorRequest;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MentorService {

    private final MentorRepository mentorRepository;

    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    public MentorDto addMentor(MentorRequest request) {
        Mentor mentor = ObjectMapperUtils.map(request, Mentor.class);
        mentorRepository.save(mentor);
        return ObjectMapperUtils.map(mentor, MentorDto.class);
    }

    public List<MentorDto> getMentorsByCategory(String category) {
        return mentorRepository.findByName(category).stream()
                .map(mentor -> ObjectMapperUtils.map(mentor, MentorDto.class))
                .collect(Collectors.toList());
    }

    public MentorDto getMentorDetails(String name) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        return ObjectMapperUtils.map(mentor, MentorDto.class);
    }

    public void updateMentorRating(String name, double rating) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        mentor.setRating((int) rating);
        mentorRepository.save(mentor);
    }

    public void deleteMentor(String name) {
        mentorRepository.deleteByName(name);
    }
}
