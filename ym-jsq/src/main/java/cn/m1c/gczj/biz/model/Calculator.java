package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

public class Calculator extends BaseModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7361532263146673652L;

	private Integer type;

    private String calculatorName;

    private String remark;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCalculatorName() {
        return calculatorName;
    }

    public void setCalculatorName(String calculatorName) {
        this.calculatorName = calculatorName == null ? null : calculatorName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}