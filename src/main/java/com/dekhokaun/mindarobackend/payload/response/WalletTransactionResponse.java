package com.dekhokaun.mindarobackend.payload.response;

import com.dekhokaun.mindarobackend.model.TransactionType;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletTransactionResponse {

    private UUID id;
    private UUID walletId;
    private String transactionType; // CREDIT or DEBIT
    private BigDecimal amount;
    private String status;
    private String paymentGatewayReference;
    private LocalDateTime createdAt;

    public static WalletTransactionResponse from(WalletTransaction tx) {
        return new WalletTransactionResponse(
                tx.getId(),
                tx.getWallet() != null ? tx.getWallet().getId() : null,
                tx.getTransactionType() != null ? tx.getTransactionType().name() : null,
                tx.getAmount(),
                tx.getStatus(),
                tx.getPaymentGatewayReference(),
                tx.getCreatedAt()
        );
    }
}
