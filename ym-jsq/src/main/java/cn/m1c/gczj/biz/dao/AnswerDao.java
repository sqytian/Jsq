package cn.m1c.gczj.biz.dao;

import java.util.List;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.Answer;

public interface AnswerDao extends IBaseDao {

	List<Answer> getAnswerList(PageBaseModel page);
	
}