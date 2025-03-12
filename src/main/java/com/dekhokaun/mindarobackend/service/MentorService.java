package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.CategoryNotFoundException;
import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.Rating;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.MentorRequest;
import com.dekhokaun.mindarobackend.payload.response.MentorResponse;
import com.dekhokaun.mindarobackend.repository.CategoryRepository;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.RatingRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final CategoryRepository  categoryRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    /**
     * Add a new mentor
     */
    public MentorResponse addMentor(MentorRequest request) {
        Mentor mentor = ObjectMapperUtils.map(request, Mentor.class);
        mentorRepository.save(mentor);
        return ObjectMapperUtils.map(mentor, MentorResponse.class);
    }

    /**
     * Get paginated list of mentors by category
     */
    public Page<MentorResponse> getMentorsByCategory(String language, UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        Set<Category> categories = Set.of(category);

        Page<Mentor> mentorPage = mentorRepository.findByCategoriesContainingAndMainlanguage(categories, language, pageable);

        return mentorPage.map(mentor -> ObjectMapperUtils.map(mentor, MentorResponse.class));
    }

    /**
     * Get mentor details by name
     */
    public MentorResponse getMentorDetails(String name) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + name));
        return ObjectMapperUtils.map(mentor, MentorResponse.class);
    }

    /**
     * Update mentor rating
     */
    @Transactional
    public void updateMentorRating(String mentorName, Integer ratingValue, UUID userId, String review) {
        Mentor mentor = mentorRepository.findByName(mentorName)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + mentorName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Save new rating entry
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMentor(mentor);
        rating.setRating(ratingValue);
        rating.setReview(review);
        ratingRepository.save(rating);

        // Recalculate mentor's average rating
        Double avgRating = ratingRepository.getAverageRatingByMentor(mentor.getId());
        Integer totalRatings = ratingRepository.countRatingsByMentor(mentor.getId());

        mentor.setRating(avgRating);
        mentor.setRatingCount(totalRatings);

        mentorRepository.save(mentor);
    }

    /**
     * Delete a mentor by name
     */
    public void deleteMentor(String name) {
        Mentor mentor = mentorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with name: " + name));

        mentorRepository.delete(mentor);
    }
}
