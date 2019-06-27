package cn.m1c.gczj.common;

import java.io.Serializable;

public class AccessTokenCache implements Serializable {

	
	private static final long serialVersionUID = 6692118517812086965L;

	private String accessToken;
	
	private long expiers;

	
	public AccessTokenCache() {
		super();
	}
	
	public AccessTokenCache(String accessToken, long expiers) {
		super();
		this.accessToken = accessToken;
		this.expiers = expiers;
	}



	//-----------------------------------------------------------------
	
	

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiers() {
		return expiers;
	}

	public void setExpiers(long expiers) {
		this.expiers = expiers;
	}
	
	
}
