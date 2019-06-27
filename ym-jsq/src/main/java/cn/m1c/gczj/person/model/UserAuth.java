package cn.m1c.gczj.person.model;

import java.util.Date;

/**
 * 用户认证model
 */
public class UserAuth {
    private String userId;

    private String accessToken;

    private Date expire;

    private String refreshToken;
    

    public UserAuth(String userId) {
		super();
		this.userId = userId;
	}

    
    public UserAuth() {
		super();
	}


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
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }
}