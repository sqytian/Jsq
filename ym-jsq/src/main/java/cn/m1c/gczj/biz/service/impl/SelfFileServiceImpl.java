package cn.m1c.gczj.biz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.SelfFileDao;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.model.SelfFile;
import cn.m1c.gczj.biz.service.SelfFileService;

@Service("selfFileService")
public class SelfFileServiceImpl extends BaseServiceImpl implements SelfFileService {
	private static Logger logger = LoggerFactory.getLogger(SelfFileServiceImpl.class);
	@Resource
	private SelfFileDao modelDao;
	
	@Override
	public IBaseDao getDao() {
		return modelDao;
	}


	
	@Override
	public void deletedmodel(String id) {
		SelfFile model = (SelfFile) modelDao.selectByPrimaryKey(id);
		model.setDeleted(true);
		modelDao.updateByPrimaryKeySelective(model);
		
	}

	@Override
	public Map<Integer, List<SelfFile>> getListByType(Integer pageSize, Integer pageNum, 
			String type,String codeName,String fileName) {
		Map<Integer, List<SelfFile>> map = new HashMap<Integer, List<SelfFile>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(type!=null){
			sqlCondition.append(" and type_name = "+"'"+type+"'");	
		}
		if(codeName!=null){
			sqlCondition.append(" and reserved_first = "+"'"+codeName+"'");	
		}
		if(fileName!=null){
			sqlCondition.append(" and document_name = "+"'"+fileName+"'");	
		}
		
		page.setOrderBy(" order by order_num DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<SelfFile> list = modelDao.getListByType(page);
		map.put(page.getTotalCount(), list);
		return map;
		
	}


	@Override
	public String updatemodel(String id,String typeName, Integer orderNum, String fileName, String urlType, 
			String fileUrl,String imageUrl, String documentDescription,String codeName,String toolType) {
		//新建
		if(!StringUtils.hasText(id)){
			SelfFile model = new SelfFile();
		    id=UUIDGenerator.getUUID();
			model.setId(id);
			model.setTypeName(typeName);
			model.setOrderNum(orderNum);
			model.setDocumentName(fileName);
			model.setUrlType(urlType);
			model.setFileUrl(fileUrl);
			model.setImageUrl(imageUrl);
			model.setReservedFirst(codeName);
			model.setReservedSecond(toolType);
			model.setDocumentDescription(documentDescription);
			model.setDeleted(false);
			model.setUpdated(new Date());
			model.setCreated(new Date());
			modelDao.insertSelective(model);
		}else{
			SelfFile model = (SelfFile) modelDao.selectByPrimaryKey(id);
			model.setId(id);
			model.setTypeName(typeName);
			model.setOrderNum(orderNum);
			model.setDocumentName(fileName);
			model.setUrlType(urlType);
			model.setFileUrl(fileUrl);
			model.setImageUrl(imageUrl);
			model.setReservedFirst(codeName);
			model.setReservedSecond(toolType);
			model.setDocumentDescription(documentDescription);
			model.setUpdated(new Date());
			modelDao.updateByPrimaryKeySelective(model);
		}
		return id;
		
	}



	@Override
	public SelfFile getFileByName(String fileName) {
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(StringUtils.hasLength(fileName)){
			sqlCondition.append(" and document_name like "+"'%"+fileName+"%'");
		}
		return modelDao.getFileByName(sqlCondition.toString());
	}

	

	



	

	



}
