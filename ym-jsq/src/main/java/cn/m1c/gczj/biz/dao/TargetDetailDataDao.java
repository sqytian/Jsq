package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.TargetDetailData;

public interface TargetDetailDataDao extends IBaseDao {

	List<TargetDetailData> getList(PageBaseModel page);

	List<TargetDetailData> getListAll(@Param("sqlCondition")String sqlCondition);
	
}