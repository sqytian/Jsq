package cn.m1c.gczj.biz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.gczj.biz.model.ProjectDocument;

public interface ProjectDocumentDao extends IBaseDao {

	List<ProjectDocument> getStandardDocument(@Param("type")Integer type);

	
}