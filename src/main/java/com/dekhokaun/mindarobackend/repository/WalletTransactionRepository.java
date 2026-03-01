package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.TransactionType;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends BaseRepository<WalletTransaction> {

    // Get all transactions for a specific wallet, ordered by creation date (newest first)
    List<WalletTransaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);

    // Get transactions by status
    List<WalletTransaction> findByStatus(String status);

    // Get transactions by type (CREDIT or DEBIT)
    List<WalletTransaction> findByTransactionType(TransactionType transactionType);

    // Find a transaction by payment gateway reference
    Optional<WalletTransaction> findByPaymentGatewayReference(String reference);
}
