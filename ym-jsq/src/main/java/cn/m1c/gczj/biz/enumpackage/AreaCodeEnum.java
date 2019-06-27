package cn.m1c.gczj.biz.enumpackage;

import cn.m1c.frame.enums.BaseEnum;

public enum AreaCodeEnum implements BaseEnum<AreaCodeEnum>{

	QUANGUO("全国","001"),
	BEIJING("北京","010"),
	GUANGXI("广西","0770"),
	GANSU("甘肃","0930"),
	GUANGDONG("广东","020"),
	GUIZHOU("贵州","0851"),
	HENAN("河南","0370"),
	HEILONGJIANG("黑龙江","0451"),
	HUBEI("湖北","027"),
	JILIN("吉林","0431"),
	JIANGSU("江苏","025"),
	JIANGXI("江西","0791"),
	SICHUAN("四川","028"),
	ZHEJIANG("浙江","0670");
	
	
	private String name;
	private String chineseName;
	private String areaCode;
	
	private AreaCodeEnum(String chineseName,String areaCode){
		this.chineseName=chineseName;
		this.areaCode=areaCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public int getOrdinal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AreaCodeEnum parsing(String element) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
