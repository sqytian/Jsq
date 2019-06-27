package cn.m1c.gczj.person.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.gczj.person.model.User;

public interface UserDao extends IBaseDao {

	/**
	 * 通过手机号查询用户
	 * @param user
	 * @return
	 */
	User queryByMobile(@Param("mobile")String mobile);

	/**
	 * 查询用户信息列表
	 * @param request
	 * @param response
	 * @param role  用户类型
	 * @param pageNum  页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	List<User> getUserList(PageBaseModel page);
	
}