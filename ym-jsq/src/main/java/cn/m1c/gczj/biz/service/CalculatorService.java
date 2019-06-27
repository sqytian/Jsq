package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Calculator;

public interface CalculatorService extends BaseService {

	Map<Integer, List<Calculator>> getCalculatorList(Integer valueOf, Integer valueOf2);

	String updateCalculator(String id, Integer valueOf, String calculatorName,String standardName);

	void deletedCalculator(String id);

	


}
