package cn.m1c.gczj.biz.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.Answer;

public interface AnswerService extends BaseService {
	

	/**
	 * 新建/修改问答
	 * @param id 
	 * @param answerContent  回答内容
	 * @param question   提问内容
	 */
	String updateAnswer(String id, String answerContent, String question);

	/**
	 * 删除问答
	 * @param id  问答id
	 */
	void deletedAnswer(String id);

	/**
	 * 获取问答列表
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @return
	 */
	Map<Integer, List<Answer>> getAnswerList(Integer pageSize, Integer pageNum);

}
