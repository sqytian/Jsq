package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 生成报告时需要的规范标准名称
 * @author Administrator
 *
 */
public class ProjectDocument extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2642938569876334966L;
	private String standardDocument;
    private Integer type;


    public String getStandardDocument() {
        return standardDocument;
    }

    public void setAnswer(String standardDocument) {
        this.standardDocument = standardDocument == null ? null : standardDocument.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}