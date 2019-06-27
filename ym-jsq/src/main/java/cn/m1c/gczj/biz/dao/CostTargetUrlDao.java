package cn.m1c.gczj.biz.dao;

import java.util.List;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.CostTargetUrl;

public interface CostTargetUrlDao extends IBaseDao {

	List<CostTargetUrl> getListTypePage(PageBaseModel page);
	
	List<CostTargetUrl> getListByType(Integer type);
	
	
}