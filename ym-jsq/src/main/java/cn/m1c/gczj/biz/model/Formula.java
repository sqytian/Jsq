package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 造价计算器计算项
 * @author Administrator
 *
 */
public class Formula extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4803251730981080239L;
	
	
	private String formulaName;
    private String areaCode;
    private String areaName;
    private String standardName;

    private Integer type;

    private String reservedFirst;

    private String reservedSecond;
    
    public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName == null ? null : formulaName.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}