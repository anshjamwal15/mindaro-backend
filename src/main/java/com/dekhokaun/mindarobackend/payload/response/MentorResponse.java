package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Contract aligned with admin-dashboard Mentor type.
 */
@Data
public class MentorResponse {
    /**
     * UUID of the mentor (primary identifier)
     */
    private UUID id;

    private String name;
    private String email;
    /** Dashboard expects a string mobile number */
    private String mobile;
    private String country;

    /** ACTIVE | INACTIVE */
    private String status;

    /** Short profile/summary */
    private String about;

    /** List of expertise/category names */
    private List<String> expertise;

    private Double rating;
    private Integer ratingCount;

    /** ISO string */
    private String createdAt;

    /** JWT token for authentication */
    private String jwtToken;

    /** User ID associated with this mentor */
    private UUID userId;
}
