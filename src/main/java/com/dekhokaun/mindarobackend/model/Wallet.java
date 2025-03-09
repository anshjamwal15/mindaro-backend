package com.dekhokaun.mindarobackend.model;

import com.dekhokaun.mindarobackend.exception.InsufficientBalanceException;
import com.dekhokaun.mindarobackend.exception.InvalidTransactionException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "a1_wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount to add must be greater than zero.");
        }
        this.balance = this.balance.add(amount);
    }

    public void deductBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount to deduct must be greater than zero.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in wallet.");
        }
        this.balance = this.balance.subtract(amount);
    }

}
