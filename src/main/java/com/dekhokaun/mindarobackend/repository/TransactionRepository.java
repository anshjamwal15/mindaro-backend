package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

    List<Transaction> findByUserId(Integer userId);

    List<Transaction> findByMentorId(Integer mentorId);

    List<Transaction> findByTransactionType(String transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.isScheduled = true")
    List<Transaction> findScheduledTransactions();

    @Query("SELECT t FROM Transaction t WHERE t.scheduledTime BETWEEN :start AND :end")
    List<Transaction> findTransactionsBetweenTimes(@Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);
}
