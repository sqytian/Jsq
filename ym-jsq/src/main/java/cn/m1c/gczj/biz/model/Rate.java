package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 造价计算器计算公式
 * @author Administrator
 *
 */
public class Rate extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7771569510945546212L;
	
	private String rate;
    private Integer rateLevel;
    private Long price;
    private String areaCode;
    private String formulaId;
    private String remark;

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    public Integer getRateLevel() {
        return rateLevel;
    }

    public void setRateLevel(Integer rateLevel) {
        this.rateLevel = rateLevel;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId;
    }

}