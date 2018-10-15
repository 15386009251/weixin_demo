package com.weixin.bean;

import java.io.Serializable;

public class TokenBean implements Serializable {
    private static final long serialVersionUID = 2805549734627719587L;
    private String token;
    private long timestamp;
    private int expiresIn;

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
