package cn.m1c.gczj.biz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.CostTableDao;
import cn.m1c.gczj.biz.model.CostTable;
import cn.m1c.gczj.biz.service.CostTableService;

@Service("costTableService")
public class CostTableServiceImpl extends BaseServiceImpl implements CostTableService {
	private static Logger logger = LoggerFactory.getLogger(CostTableServiceImpl.class);
	@Resource
	private CostTableDao costTableDao;
	
	@Override
	public IBaseDao getDao() {
		return costTableDao;
	}

	
	
	@Override
	public String updateTable(String id, String areaCode, String item, String contentType, String valueRange, String unit,
			String presetValue, String parameters, Integer tableType, String remark,
			String defaultValue, String itemKey, Integer orderNumber,String additionId) {
		logger.info("参数："+id, areaCode, item, contentType, valueRange, unit, presetValue,
				parameters, tableType, remark, defaultValue, itemKey);
//		String json = json(presetValue);
		//新建
		if(!StringUtils.hasText(id)){
			CostTable model = new CostTable();
			id=UUIDGenerator.getUUID();
			model.setId(id);
			model.setAreaCode(areaCode);
			model.setItem(item);
			model.setContentType(contentType);
			model.setValueRange(valueRange);
			model.setUnit(unit);
			model.setPresetValue(presetValue);
			model.setParameters(parameters);
			model.setTableType(tableType);
			model.setDefaultValue(defaultValue);
			model.setItemKey(itemKey);
			model.setOrderNumber(orderNumber);
			model.setDeleted(false);
			model.setUpdated(new Date());
			model.setCreated(new Date());
			model.setAdditionId(additionId);
			costTableDao.insertSelective(model);
		}else{
			CostTable model = (CostTable) costTableDao.selectByPrimaryKey(id);
			model.setId(id);
			model.setAreaCode(areaCode);
			model.setItem(item);
			model.setContentType(contentType);
			model.setValueRange(valueRange);
			model.setUnit(unit);
			model.setPresetValue(presetValue);
			model.setParameters(parameters);
			model.setTableType(tableType);
			model.setDefaultValue(defaultValue);
			model.setItemKey(itemKey);
			model.setOrderNumber(orderNumber);
			model.setUpdated(new Date());
			model.setAdditionId(additionId);
			costTableDao.updateByPrimaryKeySelective(model);
		}
		return id;
	}

	@Override
	public void deletedTable(String id) {
		CostTable model = (CostTable) costTableDao.selectByPrimaryKey(id);
		model.setDeleted(true);
		costTableDao.updateByPrimaryKeySelective(model);
	}

	@Override
	public Map<Integer, List<CostTable>> getTableList(Integer pageSize, Integer pageNum, String areaCode) {
		Map<Integer, List<CostTable>> map = new HashMap<Integer, List<CostTable>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(areaCode!=null){
			sqlCondition.append(" and area_code = "+"'"+areaCode+"'");	
		}
		page.setOrderBy(" order by order_number ASC ");
		page.setSqlCondition(sqlCondition.toString());
		List<CostTable> tableList = costTableDao.getTableList(page);
		map.put(page.getTotalCount(), tableList);
		return map;
		
	}

	



}
