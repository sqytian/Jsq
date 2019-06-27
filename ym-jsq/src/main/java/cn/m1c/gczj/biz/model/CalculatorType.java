package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;
import java.util.Date;

/**
 * 生成造价计算器表model
 * @author Administrator
 *
 */
public class CalculatorType extends BaseModel {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1559093342587713515L;
	private String id;
    private Date created;
    private Date updated;
    private Boolean deleted;
    private Integer type;
    private String item;
    private String contentType;
    private String valueRange;
    private String unit;
    private String formulaId;
    private String presetValue;
    private String parameters;
    
    
    public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getPresetValue() {
		return presetValue;
	}

	public void setPresetValue(String presetValue) {
		this.presetValue = presetValue;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType == null ? null : contentType.trim();
    }

    public String getValueRange() {
        return valueRange;
    }

    public void setValueRange(String valueRange) {
        this.valueRange = valueRange == null ? null : valueRange.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId == null ? null : formulaId.trim();
    }
}