package cn.m1c.gczj.person.service;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.common.AdminToken;
import cn.m1c.gczj.person.model.Admin;

public interface AdminService extends BaseService{

	/**
	 * 通过手机号查询用户
	 * @param mobile
	 * @return
	 */
	Admin queryAdminMobile(String mobile);

	/**
	 * 验证密码
	 * @param admin  用户
	 * @param password  密码
	 * @return
	 */
	boolean checkPassword(Admin admin, String password);

	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password 密码
	 * @return
	 * @throws Exception 
	 */
	AdminToken login(String mobile, String password) throws Exception;

	/**
	 * 修改密码
	 * @param id  用户id
	 * @param mobile  手机号
	 * @param newPassword  新密码
	 */
	void changePassword(String id, String mobile, String newPassword);

	/**
	 * 用户注册
	 * @param nickname  昵称
	 * @param mobile   手机号
	 * @param password   密码
	 * @param i        角色（普通用户：1）
	 * @return
	 */
	Admin register(String nickname, String mobile, String password, int i);

}
