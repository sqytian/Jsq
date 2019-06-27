package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.CostTargetUrl;

public interface CostTargetUrlService extends BaseService {
	



	/**
	 * 获取列表
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @return
	 */
	Map<Integer, List<CostTargetUrl>> getList(Integer pageSize, Integer pageNum,Integer type);

	void deletedModel(String id);

}
