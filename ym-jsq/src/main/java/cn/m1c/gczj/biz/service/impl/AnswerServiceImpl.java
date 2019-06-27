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
import cn.m1c.gczj.biz.dao.AnswerDao;
import cn.m1c.gczj.biz.model.Answer;
import cn.m1c.gczj.biz.service.AnswerService;

@Service("answerService")
public class AnswerServiceImpl extends BaseServiceImpl implements AnswerService {

	@Resource
	private AnswerDao answerDao;
	
	@Override
	public IBaseDao getDao() {
		return answerDao;
	}

	/**
	 * 获取问答列表
	 * @param pageSize 每页几条
	 * @param pageNum  第几页
	 * @return
	 */
	@Override
	public Map<Integer, List<Answer>> getAnswerList(Integer pageSize, Integer pageNum) {
		Map<Integer, List<Answer>> map = new HashMap<Integer, List<Answer>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<Answer> answerList = answerDao.getAnswerList(page);
		map.put(page.getTotalCount(), answerList);
		return map;
	}

	/**
	 * 新建/修改问答
	 * @param request
	 * @param response
	 * @param id  问答id
	 * @param answerContent    回答内容
	 * @param question   提问内容
	 */
	@Override
	public String updateAnswer(String id, String answerContent, String question) {
		//新建
		if(!StringUtils.hasText(id)){
			Answer answer = new Answer();
			id =UUIDGenerator.getUUID();
			answer.setId(id);
			answer.setCreated(new Date());
			answer.setUpdated(new Date());
			answer.setDeleted(false);
			answer.setAnswer(answerContent);
			answer.setQuestion(question);
			answerDao.insert(answer);
		}else{
			Answer answer = (Answer) answerDao.selectByPrimaryKey(id);
			answer.setUpdated(new Date());
			answer.setAnswer(answerContent);
			answer.setQuestion(question);
			answerDao.updateByPrimaryKeySelective(answer);
		}
		return id;
	}

	/**
	 * 删除问答
	 * @param id  问答id
	 */
	@Override
	public void deletedAnswer(String id) {
		Answer answer = (Answer) answerDao.selectByPrimaryKey(id);
		answer.setDeleted(true);
		answerDao.updateByPrimaryKeySelective(answer);
	}

}
