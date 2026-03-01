package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.TransactionType;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import com.dekhokaun.mindarobackend.payload.request.WalletAddMoneyRequest;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.repository.WalletRepository;
import com.dekhokaun.mindarobackend.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletApiService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Wallet getOrCreate(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Try to find existing wallet first
        return walletRepository.findByUser(user)
                .orElseGet(() -> {
                    try {
                        // Create new wallet
                        Wallet w = new Wallet();
                        w.setUser(user);
                        w.setBalance(BigDecimal.ZERO);
                        return walletRepository.save(w);
                    } catch (DataIntegrityViolationException e) {
                        // Race condition: wallet was created by another thread
                        // Fetch it again
                        log.warn("Wallet already exists for user {}, fetching existing wallet", userId);
                        return walletRepository.findByUser(user)
                                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found after creation attempt"));
                    }
                });
    }

    @Transactional
    public Wallet addMoney(WalletAddMoneyRequest request) {
        Wallet wallet = getOrCreate(request.getUserId());
        wallet.addBalance(request.getAmount());
        Wallet saved = walletRepository.save(wallet);

        WalletTransaction tx = new WalletTransaction();
        tx.setWallet(saved);
        tx.setTransactionType(TransactionType.CREDIT);
        tx.setAmount(request.getAmount());
        tx.setStatus("SUCCESS");
        tx.setPaymentGatewayReference(request.getMethod());
        walletTransactionRepository.save(tx);
        return saved;
    }

    public List<WalletTransaction> transactions(UUID userId) {
        Wallet wallet = getOrCreate(userId);
        return walletTransactionRepository.findByWalletOrderByCreatedAtDesc(wallet);
    }


    @Transactional
    public Wallet updateWalletAmount(UUID userId, BigDecimal newAmount, String reason) {
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }

        Wallet wallet = getOrCreate(userId);
        BigDecimal oldBalance = wallet.getBalance();
        wallet.setBalance(newAmount);
        Wallet saved = walletRepository.save(wallet);

        // Record transaction for audit trail
        WalletTransaction tx = new WalletTransaction();
        tx.setWallet(saved);
        tx.setAmount(newAmount.subtract(oldBalance));
        tx.setTransactionType(newAmount.compareTo(oldBalance) >= 0 ? TransactionType.CREDIT : TransactionType.DEBIT);
        tx.setStatus("SUCCESS");
        tx.setPaymentGatewayReference(reason != null ? reason : "WALLET_UPDATE");
        walletTransactionRepository.save(tx);

        return saved;
    }

}
