package cn.m1c.gczj.biz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.CalculatorDao;
import cn.m1c.gczj.biz.model.Calculator;
import cn.m1c.gczj.biz.service.CalculatorService;

@Service("calculatorService")
public class CalculatorServiceImpl extends BaseServiceImpl implements CalculatorService {
	@Resource
	private CalculatorDao calculatorDao;
	
	@Override
	public IBaseDao getDao() {
		return calculatorDao;
	}

	
	
	@Override
	public String updateCalculator(String id, Integer type, String calculatorName,String standardName) {
		//新建
		if(!StringUtils.hasText(id)){
			Calculator calculator = new Calculator();
			id=UUIDGenerator.getUUID();
			calculator.setId(id);
			calculator.setType(type);
			calculator.setCalculatorName(calculatorName);
			calculator.setRemark(standardName);
			calculator.setDeleted(false);
			calculator.setUpdated(new Date());
			calculator.setCreated(new Date());
			calculatorDao.insertSelective(calculator);
		}else{
			Calculator calculator = (Calculator) calculatorDao.selectByPrimaryKey(id);
			calculator.setId(id);
			calculator.setType(type);
			calculator.setCalculatorName(calculatorName);
			calculator.setRemark(standardName);
			calculator.setUpdated(new Date());
			calculatorDao.updateByPrimaryKeySelective(calculator);
		}
		return id;
	}

	@Override
	public void deletedCalculator(String id) {
		Calculator calculator = (Calculator) calculatorDao.selectByPrimaryKey(id);
		calculator.setId(id);  
		calculator.setDeleted(true);
		calculatorDao.updateByPrimaryKeySelective(calculator);
	}

	@Override
	public Map<Integer, List<Calculator>> getCalculatorList(Integer pageSize, Integer pageNum) {
		Map<Integer, List<Calculator>> map = new HashMap<Integer, List<Calculator>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ORDER BY TYPE");
		page.setSqlCondition(sqlCondition.toString());
		List<Calculator> tableList = calculatorDao.getCalculatorList(page);
		map.put(page.getTotalCount(), tableList);
		return map;
		
	}

	



}
