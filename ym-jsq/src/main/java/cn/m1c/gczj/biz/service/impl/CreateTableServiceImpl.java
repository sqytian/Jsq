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
import cn.m1c.frame.model.BaseModel;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.CreateTableDao;
import cn.m1c.gczj.biz.model.CreateTable;
import cn.m1c.gczj.biz.service.CreateTableService;

@Service("createTableService")
public class CreateTableServiceImpl extends BaseServiceImpl implements CreateTableService {
	private static Logger logger = LoggerFactory.getLogger(CreateTableServiceImpl.class);
	@Resource
	private CreateTableDao createTableDao;
	
	@Override
	public IBaseDao getDao() {
		return createTableDao;
	}

//	public String json(String text){
//		 JSONObject jsont = new JSONObject();
//		 //●♠
//	        String[] sp = text.split("●");
//	        for (String string : sp) {
//	        	String[] split = string.split("♠");
//	        	jsont.put(split[0], split[1]);
//			}
//	        return jsont.toJSONString();
//	}
	
	
	@Override
	public String updateTable(String id, Integer type, String item, String contentType, String valueRange, String unit,
			String presetValue, String parameters, Integer tableType, String remark,
			String defaultValue, String itemKey, Integer orderNumber,String formulaId) {
		logger.info("参数："+id, type, item, contentType, valueRange, unit, presetValue,
				parameters, tableType, remark, defaultValue, itemKey);
//		String json = json(presetValue);
		//新建
		if(!StringUtils.hasText(id)){
			CreateTable createTable = new CreateTable();
			id=UUIDGenerator.getUUID();
			createTable.setId(id);
			createTable.setType(type);
			createTable.setItem(item);
			createTable.setContentType(contentType);
			createTable.setValueRange(valueRange);
			createTable.setUnit(unit);
			createTable.setPresetValue(presetValue);
			createTable.setParameters(parameters);
			createTable.setTableType(tableType);
			createTable.setDefaultValue(defaultValue);
			createTable.setItemKey(itemKey);
			createTable.setFormulaId(formulaId);
			createTable.setOrderNumber(orderNumber);
			createTable.setDeleted(false);
			createTable.setUpdated(new Date());
			createTable.setCreated(new Date());
			createTableDao.insertSelective(createTable);
		}else{
			CreateTable createTable = (CreateTable) createTableDao.selectByPrimaryKey(id);
			createTable.setId(id);
			createTable.setType(type);
			createTable.setItem(item);
			createTable.setContentType(contentType);
			createTable.setValueRange(valueRange);
			createTable.setUnit(unit);
			createTable.setPresetValue(presetValue);
			createTable.setParameters(parameters);
			createTable.setTableType(tableType);
			createTable.setDefaultValue(defaultValue);
			createTable.setItemKey(itemKey);
			createTable.setOrderNumber(orderNumber);
			createTable.setFormulaId(formulaId);
			createTable.setUpdated(new Date());
			createTableDao.updateByPrimaryKeySelective(createTable);
		}
		return id;
	}

	@Override
	public void deletedTable(String id) {
		CreateTable createTable = (CreateTable) createTableDao.selectByPrimaryKey(id);
		createTable.setDeleted(true);
		createTableDao.updateByPrimaryKeySelective(createTable);
	}

	@Override
	public Map<Integer, List<CreateTable>> getTableList(Integer pageSize, Integer pageNum, Integer type) {
		Map<Integer, List<CreateTable>> map = new HashMap<Integer, List<CreateTable>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(type!=null){
			sqlCondition.append(" and type = "+type);	
		}
		page.setOrderBy(" order by order_number ASC ");
		page.setSqlCondition(sqlCondition.toString());
		List<CreateTable> tableList = createTableDao.getTableList(page);
		map.put(page.getTotalCount(), tableList);
		return map;
		
	}

	@Override
	public Map<Integer, List<CreateTable>> getCostTableList(Integer pageSize, Integer pageNum, String formulaId) {
		Map<Integer, List<CreateTable>> map = new HashMap<Integer, List<CreateTable>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(formulaId!=null){
			sqlCondition.append(" and formula_id = "+"'"+formulaId+"'");	
		}
		page.setOrderBy(" order by order_number ASC ");
		page.setSqlCondition(sqlCondition.toString());
		List<CreateTable> tableList = createTableDao.getCostTableList(page);
		map.put(page.getTotalCount(), tableList);
		return map;
	}

	@Override
	public List<CreateTable> getTableAll() {
		// TODO Auto-generated method stub
		return createTableDao.getTableAll();
	}

	



}
