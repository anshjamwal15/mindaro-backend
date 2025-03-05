package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends BaseRepository<Session, Integer> {

    List<Session> findByUserid(String userId);

//    Page<Session> findAll(Pageable pageable);
}