package cn.m1c.gczj.common;

import java.io.Serializable;
import java.util.Date;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.gczj.person.model.Admin;

public class AdminToken implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5877285154637972083L;

	private Admin admin;
	
	private String accessToken;
	
	private String refreshToken;
	
	private Date expiers;

	private StatusCode statusCode;
	
	
	public AdminToken() {
		super();
	}
	
	public AdminToken(Admin admin, String accessToken, String refreshToken, Date expiers) {
		super();
		this.admin = admin;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiers = expiers;
	}


	//-------------------------------------------------------------------------------------------

	public String getAdminname(){
		return this.admin == null ? null : admin.getNickname();
	}
	
	public Long getMobile(){
		return this.admin == null ? null : admin.getMobile();
	}
	
	public String getAdminId(){
		return this.admin == null ? null : admin.getId();
	}
	
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
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
