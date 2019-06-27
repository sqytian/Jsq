package cn.m1c.gczj.biz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.gczj.biz.dao.ProjectDocumentDao;
import cn.m1c.gczj.biz.model.ProjectDocument;
import cn.m1c.gczj.biz.service.ProjectDocumentService;

@Service("projectDocumentService")
public class ProjectDocumentServiceImpl extends BaseServiceImpl implements ProjectDocumentService {

	@Resource
	private ProjectDocumentDao projectDocumentDao;
	
	@Override
	public IBaseDao getDao() {
		return projectDocumentDao;
	}

	@Override
	public List<ProjectDocument> createProjectDocument(Integer type) {
		// TODO Auto-generated method stub
		return projectDocumentDao.getStandardDocument(type);
	}


}
