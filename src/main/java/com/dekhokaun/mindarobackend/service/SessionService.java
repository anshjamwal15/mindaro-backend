package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.SessionDto;
import com.dekhokaun.mindarobackend.model.Session;
import com.dekhokaun.mindarobackend.payload.request.SessionRequest;
import com.dekhokaun.mindarobackend.repository.SessionRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionDto createSession(SessionRequest request) {
        Session session = ObjectMapperUtils.map(request, Session.class);
        sessionRepository.save(session);
        return ObjectMapperUtils.map(session, SessionDto.class);
    }

    public List<SessionDto> getUserSessions(String userId) {
        return sessionRepository.findByUserid(userId).stream()
                .map(session -> ObjectMapperUtils.map(session, SessionDto.class))
                .collect(Collectors.toList());
    }

    public void endSession(String sessionId) {
        sessionRepository.deleteById(UUID.fromString(sessionId));
    }
}
