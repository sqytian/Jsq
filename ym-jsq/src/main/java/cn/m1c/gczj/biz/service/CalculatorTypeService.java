package cn.m1c.gczj.biz.service;

import java.util.List;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.CalculatorType;

public interface CalculatorTypeService extends BaseService {

	List<CalculatorType> getCalculatorTypeList(int type, String formulaId);
	


}
