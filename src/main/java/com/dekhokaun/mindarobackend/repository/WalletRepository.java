package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends BaseRepository<Wallet> {

    // Find wallet by User
    Optional<Wallet> findByUser(User user);

    // Check if wallet exists for a user
    boolean existsByUser(User user);
}
