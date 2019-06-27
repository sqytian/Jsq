package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.CostTable;
import cn.m1c.gczj.biz.model.CreateTable;

public interface CostTableService extends BaseService {

	String updateTable(String id, String areaCode, String item, String contentType, String valueRange, String unit,
			String presetValue, String parameters, Integer tableType, String remark, 
			String defaultValue, String itemKey, Integer orderNumber,String additionId);

	void deletedTable(String id);

	Map<Integer, List<CostTable>> getTableList(Integer pageSize, Integer pageNum, String areaCode);
	


}
