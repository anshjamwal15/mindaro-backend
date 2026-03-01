package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletUpdateRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private BigDecimal amount;
    private String reason; // Optional reason for the update
}
