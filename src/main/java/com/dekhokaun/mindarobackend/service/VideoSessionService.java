package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Mentor;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.VideoSession;
import com.dekhokaun.mindarobackend.model.VideoSessionStatus;
import com.dekhokaun.mindarobackend.payload.request.VideoSessionCreateRequest;
import com.dekhokaun.mindarobackend.repository.MentorRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.VideoSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoSessionService {

    private final VideoSessionRepository videoSessionRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public VideoSession create(VideoSessionCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

        String roomId = request.getRoomId();
        if (roomId == null || roomId.isBlank()) {
            roomId = user.getId() + "_" + mentor.getId();
        }

        VideoSession vs = videoSessionRepository.findByRoomId(roomId).orElseGet(VideoSession::new);
        vs.setRoomId(roomId);
        vs.setUser(user);
        vs.setMentor(mentor);
        vs.setStatus(VideoSessionStatus.CREATED);
        vs.setMeta(request.getMeta());
        return videoSessionRepository.save(vs);
    }

    public VideoSession join(String roomId) {
        VideoSession vs = videoSessionRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
        vs.setStatus(VideoSessionStatus.JOINED);
        if (vs.getStartedAt() == null) {
            vs.setStartedAt(LocalDateTime.now());
        }
        return videoSessionRepository.save(vs);
    }

    public VideoSession end(String roomId) {
        VideoSession vs = videoSessionRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
        vs.setStatus(VideoSessionStatus.ENDED);
        vs.setEndedAt(LocalDateTime.now());
        return videoSessionRepository.save(vs);
    }

    public List<VideoSession> byUser(UUID userId) {
        return videoSessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public VideoSession getById(UUID id) {
        return videoSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video session not found"));
    }
}
