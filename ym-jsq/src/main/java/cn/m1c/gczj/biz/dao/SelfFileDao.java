package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.SelfFile;

public interface SelfFileDao extends IBaseDao{


	List<SelfFile> getListByType(PageBaseModel page);

	SelfFile getFileByName(@Param("sqlCondition")String sqlCondition);

}
