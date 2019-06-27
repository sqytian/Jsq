package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Answer;
import cn.m1c.gczj.biz.model.CreateTable;
import cn.m1c.gczj.biz.model.TargetDetailData;

public interface TargetDetailDataService extends BaseService {
	



	/**
	 * 获取列表
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @return
	 */
	Map<Integer, List<TargetDetailData>> getList(Integer pageSize, Integer pageNum);

	List<TargetDetailData> getList();

}
