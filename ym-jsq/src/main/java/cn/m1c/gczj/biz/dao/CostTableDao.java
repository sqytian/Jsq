package cn.m1c.gczj.biz.dao;

import java.util.List;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.CostTable;

public interface CostTableDao extends IBaseDao{

	List<CostTable> getTableList(PageBaseModel page);

}
