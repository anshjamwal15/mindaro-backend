package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends BaseRepository<Mentor, Integer> {

    List<Mentor> findByStatus(String status);

    Optional<Mentor> findByName(String name);

    Page<Mentor> finaAll(Pageable pageable);

    void deleteByName(String name);

}
