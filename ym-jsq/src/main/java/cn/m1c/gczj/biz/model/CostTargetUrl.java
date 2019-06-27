package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

public class CostTargetUrl extends BaseModel{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4318000889314532535L;

	private Integer type;

    private String typeName;

    private String documentName;

    private String urlData;



    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName == null ? null : documentName.trim();
    }

    public String getUrlData() {
        return urlData;
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData == null ? null : urlData.trim();
    }

}