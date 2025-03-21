package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {

//    Page<User> findAll(Pageable pageable);

    List<User> findByStatus(String status);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findByIds(@Param("ids") List<Integer> ids);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
