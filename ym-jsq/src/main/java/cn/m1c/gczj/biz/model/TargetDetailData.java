package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

public class TargetDetailData extends BaseModel{
    

	/**
	 * 
	 */
	private static final long serialVersionUID = -3603743249173480789L;

	//时间year：2015
    private Integer yearData;

    //时间 month：6
    private Integer monthData;

    //价格price：98.38
    private String priceData;

    //项目名称：混合结构住宅楼
    private String projectName;


	public Integer getYearData() {
		return yearData;
	}

	public void setYearData(Integer yearData) {
		this.yearData = yearData;
	}

	public Integer getMonthData() {
		return monthData;
	}

	public void setMonthData(Integer monthData) {
		this.monthData = monthData;
	}

	public String getPriceData() {
		return priceData;
	}

	public void setPriceData(String priceData) {
		this.priceData = priceData;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


    

}