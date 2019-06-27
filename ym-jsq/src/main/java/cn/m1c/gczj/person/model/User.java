package cn.m1c.gczj.person.model;

import cn.m1c.frame.model.BaseModel;

public class User extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4321921364713173996L;

    private String password;

    private Long mobile;

    private String nickname;

    private Integer role;

    private Integer times;

    private String graduateSchool;

    private String specialized;

    private String companyName;

    private String workplace;

    private String placeWork;

    private String reservedFirst;

    private String reservedSecond;

    private String reservedThird;


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

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool == null ? null : graduateSchool.trim();
    }

    public String getSpecialized() {
        return specialized;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized == null ? null : specialized.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace == null ? null : workplace.trim();
    }

    public String getPlaceWork() {
        return placeWork;
    }

    public void setPlaceWork(String placeWork) {
        this.placeWork = placeWork == null ? null : placeWork.trim();
    }

    public String getReservedFirst() {
        return reservedFirst;
    }

    public void setReservedFirst(String reservedFirst) {
        this.reservedFirst = reservedFirst == null ? null : reservedFirst.trim();
    }

    public String getReservedSecond() {
        return reservedSecond;
    }

    public void setReservedSecond(String reservedSecond) {
        this.reservedSecond = reservedSecond == null ? null : reservedSecond.trim();
    }

    public String getReservedThird() {
        return reservedThird;
    }

    public void setReservedThird(String reservedThird) {
        this.reservedThird = reservedThird == null ? null : reservedThird.trim();
    }

}