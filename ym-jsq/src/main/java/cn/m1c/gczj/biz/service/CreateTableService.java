package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.CreateTable;

public interface CreateTableService extends BaseService {

	String updateTable(String id, Integer type, String item, String contentType, String valueRange, String unit,
			String presetValue, String parameters, Integer tableType, String remark, 
			String defaultValue, String itemKey, Integer orderNumber,String formulaId);

	void deletedTable(String id);

	Map<Integer, List<CreateTable>> getTableList(Integer pageSize, Integer pageNum, Integer type);

	Map<Integer, List<CreateTable>> getCostTableList(Integer pageSize, Integer pageNum, String formulaId);

	List<CreateTable> getTableAll();
	


}
