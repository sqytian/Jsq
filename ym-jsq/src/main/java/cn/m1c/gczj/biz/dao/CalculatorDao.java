package cn.m1c.gczj.biz.dao;

import java.util.List;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.Calculator;

public interface CalculatorDao extends IBaseDao{

	List<Calculator> getCalculatorList(PageBaseModel page);


}
