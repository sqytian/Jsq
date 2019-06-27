package cn.m1c.gczj.person.model;

import cn.m1c.frame.model.BaseModel;

public class Admin extends BaseModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8893975048650109054L;
    private String password;
    private Long mobile;
    private String nickname;
    private Integer role;
    private Integer times;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
    
    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

}
