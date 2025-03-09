package com.dekhokaun.mindarobackend.model;

public enum MentorType {
    BASIC("B"),
    PREMIUM("P"),
    ELITE("E");

    private final String code;

    MentorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
