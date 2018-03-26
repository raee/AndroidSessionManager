/*
 * Copyright (c) 2017.
 */

package com.rae.session;

/**
 * 用户授权
 * Created by ChenRui on 2017/4/28 0028 18:33.
 */
public class SessionToken {

    private String userId;
    private String accessToken;
    private String refreshToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
