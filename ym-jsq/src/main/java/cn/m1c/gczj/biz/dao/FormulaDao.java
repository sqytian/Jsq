package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.Formula;

public interface FormulaDao extends IBaseDao {

	/**
	 * 通过公式名称和区号查询项目名称id
	 * @param formulaName 项目名称
	 * @param areaCode  区号
	 * @return
	 */
	Long getFormulaByName(@Param("formulaName")String formulaName,@Param("areaCode")String areaCode);

	/**
	 * 通过区号查询咨询项目
	 * @param areaCode 区号
	 * @return
	 */
	List<Formula> getFormulaListByArecaCode(@Param("areaCode")String areaCode);

	List<Formula> getFormulaList(PageBaseModel page);
}