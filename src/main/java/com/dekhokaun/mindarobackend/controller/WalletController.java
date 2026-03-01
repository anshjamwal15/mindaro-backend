package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.exception.InvalidAuthException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.Wallet;
import com.dekhokaun.mindarobackend.model.WalletTransaction;
import com.dekhokaun.mindarobackend.payload.request.WalletAddMoneyRequest;
import com.dekhokaun.mindarobackend.payload.request.WalletUpdateRequest;
import com.dekhokaun.mindarobackend.payload.response.WalletBalanceResponse;
import com.dekhokaun.mindarobackend.payload.response.WalletTransactionResponse;
import com.dekhokaun.mindarobackend.payload.response.WalletTransactionsResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.service.WalletApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet", description = "Wallet balance, add-money, and transaction history")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletApiService walletApiService;
    private final UserRepository userRepository;

    @Operation(summary = "Get wallet balance", description = "Returns current wallet balance for a user")
    @GetMapping("/balance/{userId}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(@PathVariable UUID userId) {
        Wallet wallet = walletApiService.getOrCreate(userId);
        return ResponseEntity.ok(WalletBalanceResponse.from(wallet));
    }

    @Operation(summary = "Add money to wallet", description = "Credits wallet balance for a user and records a wallet transaction. Provide userId in request body OR send JWT token in Authorization header.")
    @PostMapping("/add-money")
    public ResponseEntity<WalletBalanceResponse> addMoney(@Valid @RequestBody WalletAddMoneyRequest request) {
        // Extract userId from request body or JWT token
        if (request.getUserId() == null) {
            try {
                request.setUserId(getAuthenticatedUserId());
            } catch (InvalidAuthException e) {
                throw new InvalidAuthException("Please provide userId in request body or send a valid JWT token in Authorization header");
            }
        }
        Wallet wallet = walletApiService.addMoney(request);
        return ResponseEntity.ok(WalletBalanceResponse.from(wallet));
    }

    @Operation(summary = "Wallet transaction history", description = "Returns wallet transaction history for a user. Provide userId in request body OR send JWT token in Authorization header.")
    @PostMapping("/transactions")
    public ResponseEntity<WalletTransactionsResponse> transactions(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody(required = false) WalletAddMoneyRequest request) {
        
        log.debug("Transactions endpoint called. Auth header present: {}, Request body: {}", 
                  authHeader != null, request);
        
        // Extract userId from request body or JWT token
        UUID userId = null;
        
        // Try to get userId from request body first
        if (request != null && request.getUserId() != null) {
            userId = request.getUserId();
            log.debug("Using userId from request body: {}", userId);
        } 
        // Try to get from JWT token
        else {
            try {
                userId = getAuthenticatedUserId();
                log.debug("Using userId from JWT authentication: {}", userId);
            } catch (InvalidAuthException e) {
                log.error("Failed to get userId from authentication: {}", e.getMessage());
                throw new InvalidAuthException("Please provide userId in request body or send a valid JWT token in Authorization header. Error: " + e.getMessage());
            }
        }
        
        List<WalletTransaction> txs = walletApiService.transactions(userId);
        List<WalletTransactionResponse> mapped = txs.stream().map(WalletTransactionResponse::from).toList();
        return ResponseEntity.ok(new WalletTransactionsResponse(userId, mapped));
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        log.debug("Getting authenticated userId. Authentication: {}, Principal type: {}", 
                  authentication != null ? "present" : "null",
                  authentication != null && authentication.getPrincipal() != null ? 
                      authentication.getPrincipal().getClass().getSimpleName() : "null");
        
        if (authentication == null) {
            throw new InvalidAuthException("No authentication found. Please provide a valid JWT token in the Authorization header.");
        }
        
        if (!authentication.isAuthenticated()) {
            throw new InvalidAuthException("User is not authenticated. Please login again.");
        }
        
        Object principal = authentication.getPrincipal();
        
        // Check if it's an anonymous user
        if ("anonymousUser".equals(principal)) {
            throw new InvalidAuthException("Anonymous user detected. Please provide a valid JWT token in the Authorization header.");
        }
        
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            log.debug("UserDetails found with username: {}", userDetails.getUsername());
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new InvalidAuthException("User not found with email: " + userDetails.getUsername()));
            return user.getId();
        }
        
        if (principal instanceof String) {
            // Sometimes principal is just the username string
            String email = (String) principal;
            log.debug("String principal found: {}", email);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new InvalidAuthException("User not found with email: " + email));
            return user.getId();
        }
        
        throw new InvalidAuthException("Invalid authentication principal type: " + principal.getClass().getName());
    }


    @Operation(summary = "Update wallet amount", description = "Sets wallet balance to a specific amount for a user")
    @PutMapping("/update")
    public ResponseEntity<WalletBalanceResponse> updateWalletAmount(@Valid @RequestBody WalletUpdateRequest request) {
        Wallet wallet = walletApiService.updateWalletAmount(request.getUserId(), request.getAmount(), request.getReason());
        return ResponseEntity.ok(WalletBalanceResponse.from(wallet));
    }

}
