package cn.m1c.gczj.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.gczj.biz.dao.TargetDetailDataDao;
import cn.m1c.gczj.biz.model.TargetDetailData;
import cn.m1c.gczj.biz.service.TargetDetailDataService;

@Service("targetDetailDataService")
public class TargetDetailDataServiceImpl extends BaseServiceImpl implements TargetDetailDataService {

	@Resource
	private TargetDetailDataDao modelDao;
	
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
	public Map<Integer, List<TargetDetailData>> getList(Integer pageSize, Integer pageNum) {
		Map<Integer, List<TargetDetailData>> map = new HashMap<Integer, List<TargetDetailData>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<TargetDetailData> answerList = modelDao.getList(page);
		map.put(page.getTotalCount(), answerList);
		return map;
	}

	@Override
	public List<TargetDetailData> getList() {

		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ORDER BY year_data,month_data");
		return modelDao.getListAll(sqlCondition.toString());
	}



}
