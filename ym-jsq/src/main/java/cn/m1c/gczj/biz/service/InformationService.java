package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Information;

public interface InformationService extends BaseService {
	
	/**
	 * 获取造价指标列表
	 * @param request
	 * @param response
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @param putaway  是否上架：上架 1
	 * @param type     类型 ：造价实例
	 */
	Map<Integer, List<Information>> getInformationList(Integer pageSize, Integer pageNum,
			Integer putaway,String type);

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
	void updateInfor(String id, String title, String content, String classify, String projectSite, String time, String type);

	/**
	 * 删除造价指标
	 * @param id  指标id
	 */
	void deletedInfor(String id);

}
