package cn.m1c.gczj.biz.model;

import java.math.BigDecimal;

import cn.m1c.frame.model.BaseModel;

/**
 * 监理费公式
 * @author sqy
 *
 */
public class SuperviseRate extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1344250870024196959L;
	private String rate;
    private Integer rateLevel;
    private BigDecimal price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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