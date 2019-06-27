package cn.m1c.gczj.biz.service.impl;

import java.math.BigDecimal;
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
import cn.m1c.gczj.biz.dao.CalculationFormulaDao;
import cn.m1c.gczj.biz.model.CalculationFormula;
import cn.m1c.gczj.biz.service.CalculationFormulaService;

@Service("calculationFormulaService")
public class CalculationFormulaServiceImpl extends BaseServiceImpl implements CalculationFormulaService {

	@Resource
	private CalculationFormulaDao calculationFormulaDao;
	
	@Override
	public IBaseDao getDao() {
		return calculationFormulaDao;
	}


	@Override
	public void deletedCalculationFormula(String id) {
		CalculationFormula calculationFormula = new CalculationFormula();
		calculationFormula.setId(id);  
		calculationFormula.setDeleted(true);
		calculationFormulaDao.updateByPrimaryKeySelective(calculationFormula);
	}

	@Override
	public Map<Integer, List<CalculationFormula>> getCalculationFormulaList(Integer pageSize, Integer pageNum,
			Integer calculatorType,String calculatorItem,String formulaId) {
		Map<Integer, List<CalculationFormula>> map = new HashMap<Integer, List<CalculationFormula>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(calculatorType!=null){
			sqlCondition.append(" and calculator_type = "+calculatorType);	
		}
		if(formulaId!=null){
			sqlCondition.append(" and calculator_item = "+"'"+formulaId+"'");	
		}
//		if(calculatorItem!=null){
//			sqlCondition.append(" and remark = "+"'"+calculatorItem+"'");	
//		}
		page.setOrderBy(" order by rate_level ASC ");
		page.setSqlCondition(sqlCondition.toString());
		List<CalculationFormula> calculationFormulaList = calculationFormulaDao.getCalculationFormulaList(page);
		map.put(page.getTotalCount(), calculationFormulaList);
		return map;
		
	}
	
	@Override
	public List<CalculationFormula> getCalculationFormulaListByMoney(Integer calculatorType,BigDecimal money,String calculatorTtem,String formulaId) {
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(formulaId!=null){
			sqlCondition.append(" and calculator_item="+"'"+formulaId+"'");
		}
		if(calculatorType!=null){
			sqlCondition.append(" and calculator_type="+calculatorType);
		}
		if(money!=null){
			sqlCondition.append(" and "+money+">=price");
		}
		sqlCondition.append(" ORDER BY rate_level DESC");
		return calculationFormulaDao.getCalculationFormulaListByMoneyItem(sqlCondition.toString());
	}

	@Override
	public String updateCalculationFormula(String id, String rate, Integer rateLevel, BigDecimal price, Integer calculatorType,
			String formulaId, String remark) {
		//新建
		if(!StringUtils.hasText(id)){
			CalculationFormula calculationFormula = new CalculationFormula();
			id=UUIDGenerator.getUUID();
			calculationFormula.setId(id);
			calculationFormula.setRate(rate);
			calculationFormula.setRateLevel(rateLevel);
			calculationFormula.setPrice(price.longValue());
			calculationFormula.setCalculatorType(calculatorType);
			calculationFormula.setCalculatorItem(formulaId);
			calculationFormula.setRemark(remark);
			calculationFormula.setDeleted(false);
			calculationFormula.setUpdated(new Date());
			calculationFormula.setCreated(new Date());
			calculationFormulaDao.insertSelective(calculationFormula);
		}else{
			CalculationFormula calculationFormula = (CalculationFormula) calculationFormulaDao.selectByPrimaryKey(id);
			calculationFormula.setId(id);
			calculationFormula.setRate(rate);
			calculationFormula.setRateLevel(rateLevel);
			calculationFormula.setPrice(price.longValue());
			calculationFormula.setCalculatorType(calculatorType);
			calculationFormula.setCalculatorItem(formulaId);
			calculationFormula.setRemark(remark);
			calculationFormula.setUpdated(new Date());
			calculationFormulaDao.updateByPrimaryKeySelective(calculationFormula);
		}
		 return id;
	}



}
