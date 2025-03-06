package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.MentorDto;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.payload.request.MentorRequest;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // TODO: userid, language and category use case as no relations in table
    public Page<MentorDto> getMentorsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mentor> mentorPage = mentorRepository.finaAll(pageable);

        return mentorPage.map(mentor -> ObjectMapperUtils.map(mentor, MentorDto.class));
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
