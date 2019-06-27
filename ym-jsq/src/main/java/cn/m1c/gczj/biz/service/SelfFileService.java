package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.SelfFile;

public interface SelfFileService extends BaseService {


	Map<Integer, List<SelfFile>> getListByType(Integer pageSize, Integer pageNum, String type,String codeName,String fileName);



	void deletedmodel(String id);


	String updatemodel(String id,String typeName, Integer orderNum, String fileName, String urlType, String url, String imageUrl,
			String documentDescription,String codeName,String toolType);



	SelfFile getFileByName(String fileName);
	


}
