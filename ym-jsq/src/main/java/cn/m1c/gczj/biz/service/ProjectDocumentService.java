package cn.m1c.gczj.biz.service;

import java.util.List;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.ProjectDocument;

public interface ProjectDocumentService extends BaseService {

	List<ProjectDocument> createProjectDocument(Integer type);

}
