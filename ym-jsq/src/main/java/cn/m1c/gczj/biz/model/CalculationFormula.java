package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 其他计算器公式
 * @author Administrator
 *
 */
public class CalculationFormula extends BaseModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3059128159664276059L;

	private String rate;

    private Integer rateLevel;

    private Long price;

    private Integer calculatorType;

    private String calculatorItem;


    private String remark;


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

    public Integer getCalculatorType() {
        return calculatorType;
    }

    public void setCalculatorType(Integer calculatorType) {
        this.calculatorType = calculatorType;
    }

    public String getCalculatorItem() {
        return calculatorItem;
    }

    public void setCalculatorItem(String calculatorItem) {
        this.calculatorItem = calculatorItem == null ? null : calculatorItem.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}