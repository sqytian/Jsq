package cn.m1c.gczj.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.gczj.biz.dao.CostTargetUrlDao;
import cn.m1c.gczj.biz.model.CostTargetUrl;
import cn.m1c.gczj.biz.service.CostTargetUrlService;

@Service("costTargetUrlService")
public class CostTargetUrlServiceImpl extends BaseServiceImpl implements CostTargetUrlService {

	@Resource
	private CostTargetUrlDao modelDao;
	
	@Override
	public IBaseDao getDao() {
		return modelDao;
	}

	/**
	 * 获取列表
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @return
	 */
	@Override
	public Map<Integer, List<CostTargetUrl>> getList(Integer pageSize, Integer pageNum,Integer type) {
		Map<Integer, List<CostTargetUrl>> map = new HashMap<Integer, List<CostTargetUrl>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(type!=null){
			sqlCondition.append(" and type = "+type);	
		}
		page.setOrderBy(" order by created DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<CostTargetUrl> modelList = modelDao.getListTypePage(page);
		map.put(page.getTotalCount(), modelList);
		return map;
	}

	@Override
	public void deletedModel(String id) {
		CostTargetUrl model = (CostTargetUrl) modelDao.selectByPrimaryKey(id);
		model.setDeleted(true);
		modelDao.updateByPrimaryKeySelective(model);
		
	}



}
