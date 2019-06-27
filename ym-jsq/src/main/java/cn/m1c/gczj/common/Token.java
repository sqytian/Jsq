package cn.m1c.gczj.common;

import java.io.Serializable;
import java.util.Date;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.gczj.person.model.User;

public class Token implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5877285154637972083L;

	private User user;
	
	private String accessToken;
	
	private String refreshToken;
	
	private Date expiers;

	private StatusCode statusCode;
	
	
	public Token() {
		super();
	}
	
	public Token(User user, String accessToken, String refreshToken, Date expiers) {
		super();
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiers = expiers;
	}


	//-------------------------------------------------------------------------------------------

	public String getUsername(){
		return this.user == null ? null : user.getNickname();
	}
	
	public Long getMobile(){
		return this.user == null ? null : user.getMobile();
	}
	
	public String getUserId(){
		return this.user == null ? null : user.getId();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Date getExpiers() {
		return expiers;
	}

	public void setExpiers(Date expiers) {
		this.expiers = expiers;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
}
