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
import cn.m1c.gczj.biz.dao.FormulaDao;
import cn.m1c.gczj.biz.model.Answer;
import cn.m1c.gczj.biz.model.CreateTable;
import cn.m1c.gczj.biz.model.Formula;
import cn.m1c.gczj.biz.service.FormulaService;

@Service("formulaService")
public class FormulaServiceImpl extends BaseServiceImpl implements FormulaService {
	
	@Resource
	private FormulaDao formulaDao;

	@Override
	public IBaseDao getDao() {
		return formulaDao;
	}


	/**
	 * 通过公式名称和区号查询项目名称id
	 * @param projectName 项目名称
	 * @param areaCode  区号
	 * @return
	 */
	@Override
	public Long getFormulaByName(String projectName,String areaCode) {
		return formulaDao.getFormulaByName(projectName,areaCode);
	}

	/**
	 * 通过区号查询咨询项目
	 * @param areaCode 区号
	 * @return
	 */
	@Override
	public List<Formula> getFormulaListByArecaCode(String areaCode) {
		return formulaDao.getFormulaListByArecaCode(areaCode);
	}


	@Override
	public String updateFormula(String id, String formulaName, String areaCode, 
			String areaName, String standardName,Integer type) {
		//新建
		if(!StringUtils.hasText(id)){
			Formula formula = new Formula();
			id =UUIDGenerator.getUUID();
			formula.setId(id);
			formula.setCreated(new Date());
			formula.setUpdated(new Date());
			formula.setDeleted(false);
			formula.setFormulaName(formulaName);
			formula.setAreaCode(areaCode);
			formula.setAreaName(areaName);
			formula.setStandardName(standardName);
			formula.setType(type);
			formulaDao.insert(formula);
		}else{
			Formula formula = (Formula) formulaDao.selectByPrimaryKey(id);
			formula.setUpdated(new Date());
			formula.setFormulaName(formulaName);
			formula.setAreaCode(areaCode);
			formula.setAreaName(areaName);
			formula.setStandardName(standardName);
			formula.setType(type);
			formulaDao.updateByPrimaryKeySelective(formula);
		}
		return id;
	}


	@Override
	public void deletedFormula(String id) {
		Formula formula = (Formula) formulaDao.selectByPrimaryKey(id);
		formula.setDeleted(true);
		formulaDao.updateByPrimaryKeySelective(formula);
		
	}


	@Override
	public Map<Integer, List<Formula>> getFormulaList(Integer pageSize, Integer pageNum, String areaCode,Integer type) {
		Map<Integer, List<Formula>> map = new HashMap<Integer, List<Formula>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(areaCode!=null){
			sqlCondition.append(" and area_code = "+"'"+areaCode+"'");	
		}
		if(type!=null){
			sqlCondition.append(" and type = "+type);	
		}
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<Formula> formulaList = formulaDao.getFormulaList(page);
		map.put(page.getTotalCount(), formulaList);
		return map;
	}

}
