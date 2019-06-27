package cn.m1c.gczj.biz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.gczj.biz.dao.CalculatorTypeDao;
import cn.m1c.gczj.biz.model.CalculatorType;
import cn.m1c.gczj.biz.service.CalculatorTypeService;

@Service("calculatorTypeService")
public class CalculatorTypeServiceImpl extends BaseServiceImpl implements CalculatorTypeService {

	@Resource
	private CalculatorTypeDao calculatorTypeDao;
	
	@Override
	public IBaseDao getDao() {
		return calculatorTypeDao;
	}

	@Override
	public List<CalculatorType> getCalculatorTypeList(int type, String formulaId) {
		
		return calculatorTypeDao.getCalculatorTypeList(type,formulaId);
	}




}
