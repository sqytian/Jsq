package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Formula;

public interface FormulaService extends BaseService {

	/**
	 * 通过公式名称和区号查询项目名称id
	 * @param projectName 项目名称
	 * @param areaCode  区号
	 * @return
	 */
	Long getFormulaByName(String projectName,String areaCode);

	/**
	 * 通过区号查询咨询项目
	 * @param areaCode 区号
	 * @return
	 */
	List<Formula> getFormulaListByArecaCode(String areaCode);
	String updateFormula(String id, String formulaName, String areaCode, String areaName, String standardName,Integer type);

	void deletedFormula(String id);

	Map<Integer, List<Formula>> getFormulaList(Integer pageSize, Integer pageNum, String areaCode,Integer type);  


}
