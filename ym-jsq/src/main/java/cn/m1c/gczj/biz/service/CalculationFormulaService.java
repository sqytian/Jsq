package cn.m1c.gczj.biz.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.CalculationFormula;

public interface CalculationFormulaService extends BaseService {

	 String updateCalculationFormula(String id, String rate, Integer rateLevel, BigDecimal price, Integer calculatorType,
			String formulaId, String remark);

	void deletedCalculationFormula(String id);

	Map<Integer, List<CalculationFormula>> getCalculationFormulaList(Integer pageSize, Integer pageNum,
			Integer calculatorType,String calculatorItem,String formulaId);

	 List<CalculationFormula> getCalculationFormulaListByMoney(Integer calculatorType, BigDecimal totalMoney,String calculatorTtem,String formulaId);

	


}
