package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 计算器制表model（除了造价计算器）
 * @author sqy
 *
 */
public class CreateTable extends BaseModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1514001629738361304L;

	//计算器类型：设计费计算器 1
    private Integer type;

    //表行名：工程造价，专业调整，折扣浮动
    private String item;

    //内容类型：text,select
    private String contentType;

    //取值范围(0,n);(-100,100)
    private String valueRange;

    //单位:万元，%
    private String unit;

    //预置值，json
    private String presetValue;
    
    //参数名称
    private String parameters;
    
    //序号
    private Integer orderNumber;

    //表类型：1内容表；2，结果表
    private Integer tableType;
    
    //表行名key
    private String itemKey;

    //默认值
    private String defaultValue;
    
    private String formulaId;

    private String reservedFirst;

    private String reservedSecond;

    private String remark;

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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters == null ? null : parameters.trim();
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey == null ? null : itemKey.trim();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
    }

    public String getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}

	public String getReservedFirst() {
		return reservedFirst;
	}

	public void setReservedFirst(String reservedFirst) {
		this.reservedFirst = reservedFirst;
	}

	public String getReservedSecond() {
		return reservedSecond;
	}

	public void setReservedSecond(String reservedSecond) {
		this.reservedSecond = reservedSecond;
	}

	public String getPresetValue() {
        return presetValue;
    }

    public void setPresetValue(String presetValue) {
        this.presetValue = presetValue == null ? null : presetValue.trim();
    }
}