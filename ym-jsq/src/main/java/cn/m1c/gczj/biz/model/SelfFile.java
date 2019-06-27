package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

public class SelfFile extends BaseModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5800427408286063131L;

	private String typeName;

    private Integer orderNum;

    private String documentName;

    private String urlType;

    private String fileUrl;

    private String imageUrl;

    private String remark;

    private String reservedFirst;

    private String reservedSecond;

    private String documentDescription;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName == null ? null : documentName.trim();
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType == null ? null : urlType.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription == null ? null : documentDescription.trim();
    }
}