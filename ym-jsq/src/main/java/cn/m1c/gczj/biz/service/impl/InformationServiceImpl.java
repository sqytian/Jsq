package cn.m1c.gczj.biz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.InformationDao;
import cn.m1c.gczj.biz.model.Information;
import cn.m1c.gczj.biz.service.InformationService;

@Service("informationService")
public class InformationServiceImpl extends BaseServiceImpl implements InformationService {

	@Resource
	private InformationDao informationDao;
	
	@Override
	public IBaseDao getDao() {
		return informationDao;
	}

	/**
	 * 获取造价指标列表
	 * @param request
	 * @param response
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @param putaway  是否上架：上架 1
	 * @param type     类型 ：造价实例
	 */
	@Override
	public Map<Integer, List<Information>> getInformationList(Integer pageSize, Integer pageNum,
			Integer putaway,String type) {
		Map<Integer, List<Information>> map = new HashMap<Integer, List<Information>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(putaway!=null){
			sqlCondition.append(" and putaway = "+putaway);	
		}
		if(type!=null){
			sqlCondition.append(" and type = "+type);	
		}
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<Information> informationList = informationDao.getInformationList(page);
		map.put(page.getTotalCount(), informationList);
		return map;
	}

	/**
	 * 新建/修改造价指标
	 * @param id  造价指标id
	 * @param title     标题
	 * @param content   内容
	 * @param classify  具体分类
	 * @param projectSite    工程地点
	 * @param time      整理时间
	 * @param type      类型
	 */
	@Override
	public void updateInfor(String id, String title, String content, String classify, String projectSite, String time,
			String type) {
		//新建
		if(!StringUtils.hasText(id)){
		Information information = new Information();
		information.setId(UUIDGenerator.getUUID());
		information.setCreated(new Date());
		information.setUpdated(new Date());
		information.setClassify(classify);
		information.setContent(content);
		information.setDeleted(false);
		information.setNumber(projectSite);
		information.setPutaway(true);
		information.setTime(time);
		information.setTitle(title);
		information.setType(type);
		informationDao.insert(information);
		}else{
		Information information = new Information();
		information.setClassify(classify);
		information.setContent(content);
		information.setNumber(projectSite);
		information.setTime(time);
		information.setTitle(title);
		information.setType(type);
		informationDao.updateByPrimaryKeySelective(information);
		}
	}

	/**
	 * 删除造价指标
	 * @param id  指标id
	 */
	@Override
	public void deletedInfor(String id) {
		Information information = new Information();
		information.setId(id);  
		information.setDeleted(true);
		informationDao.updateByPrimaryKeySelective(information);
	}

}
