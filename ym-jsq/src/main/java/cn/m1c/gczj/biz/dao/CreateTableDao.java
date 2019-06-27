package cn.m1c.gczj.biz.dao;

import java.util.List;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.biz.model.CreateTable;

public interface CreateTableDao extends IBaseDao{

	List<CreateTable> getTableList(PageBaseModel page);

	List<CreateTable> getCostTableList(PageBaseModel page);

	List<CreateTable> getTableAll();

}
