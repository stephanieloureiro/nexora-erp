package com.nexora.erp.auth.dto;

public class LoginResponse {

    private String tokenType;
    private String accessToken;
    private long expiresInSeconds;

    public LoginResponse(String tokenType, String accessToken, long expiresInSeconds) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
}
