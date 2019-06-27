package cn.m1c.gczj.person.dao;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.gczj.person.model.Admin;

public interface AdminDao extends IBaseDao{

	/**
	 * 通过手机号查询用户
	 * @param user
	 * @return
	 */
	Admin queryByMobile(@Param("mobile")String mobile);


}
