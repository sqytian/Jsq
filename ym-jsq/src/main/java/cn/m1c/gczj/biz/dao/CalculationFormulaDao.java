package cn.m1c.gczj.biz.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.CalculationFormula;

public interface CalculationFormulaDao extends IBaseDao{

	void updateByPrimaryKeySelective(CalculationFormula calculationFormula);

	List<CalculationFormula> getCalculationFormulaList(PageBaseModel page);


	List<CalculationFormula> getCalculationFormulaListByMoney(@Param("calculatorType")Integer calculatorType, @Param("price")Long price);

	List<CalculationFormula> getCalculationFormulaListByMoneyItem(@Param("sqlCondition")String sqlCondition);


}
