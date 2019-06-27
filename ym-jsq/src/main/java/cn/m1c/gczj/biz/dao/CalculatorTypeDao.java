package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.gczj.biz.model.CalculatorType;

public interface CalculatorTypeDao extends IBaseDao {

	List<CalculatorType> getCalculatorTypeList(@Param("type")int type, @Param("formulaId")String formulaId);

	
}