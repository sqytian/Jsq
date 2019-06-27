package cn.m1c.gczj.person.service;

import java.util.List;
import java.util.Map;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.common.Token;
import cn.m1c.gczj.person.model.User;

public interface UserService extends BaseService {

	/**
	 * 生成4位验证码并发送给手机并存放到redis里
	 * @param mobile  手机号
	 * @param cacheKey  缓存key
	 * @param second   时间
	 * @return
	 * @throws Exception 
	 */
	String sendSmsCode(String mobile, String cacheKey, int second) throws Exception;

	/**
	 * 验证短信验证码是否正确 
	 * @param cacheKey 缓存key
	 * @param smscode   验证码
	 * @return
	 * @throws Exception 
	 */
	Boolean checkSmsCode(String string, String smscode) throws Exception;

	/**
	 * 用户注册
	 * @param nickname  昵称
	 * @param mobile   手机号
	 * @param password   密码
	 * @param i        角色（普通用户：1）
	 * @return
	 */
	User register(String nickname, String mobile, String password, int i);

	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password 密码
	 * @return
	 * @throws Exception 
	 */
	Token login(String mobile, String password) throws Exception;

	/**
	 * 通过手机号查询用户
	 * @param mobile
	 * @return
	 */
	User queryUserMobile(String mobile);

	/**
	 * 验证密码
	 * @param user  用户
	 * @param password  密码
	 * @return
	 */
	boolean checkPassword(User user, String password);

	/**
	 * 修改密码
	 * @param id  用户id
	 * @param mobile  手机号
	 * @param newPassword  新密码
	 */
	void changePassword(String id, String mobile, String newPassword);

	/**
	 * 用户信息列表
	 * @param request
	 * @param response
	 * @param role  用户类型
	 * @param pageNum  页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	Map<Integer, List<User>> getUserList(Integer pageSize, Integer pageNum, Integer role);

	void updateUserInfo(String id, String graduateSchool, String specialized,
			String companyName, String workplace, String placeWork,String url,String nickName);

}
