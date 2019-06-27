package cn.m1c.gczj.person.service.impl;

import java.rmi.ServerException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.PWDEncoder;
import cn.m1c.frame.utils.PassportSecurity;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.model.Answer;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.AccessTokenCache;
import cn.m1c.gczj.common.Codings;
import cn.m1c.gczj.common.GczjCacheKeys;
import cn.m1c.gczj.common.RegEx;
import cn.m1c.gczj.common.Token;
import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.person.dao.UserDao;
import cn.m1c.gczj.person.model.User;
import cn.m1c.gczj.person.model.UserAuth;
import cn.m1c.gczj.person.service.UserService;
import cn.m1c.gczj.utils.SmsCodeUtils;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@SuppressWarnings("rawtypes")
	@Resource(name = "redisService")
	protected RedisService redisService;
	
	@Override
	public IBaseDao getDao() {
		return userDao;
	}

	/**
	 * 生成4位验证码并发送给手机并存放到redis里
	 * @param mobile  手机号
	 * @param cacheKey  缓存key
	 * @param second   时间
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String sendSmsCode(String mobile, String cacheKey, int second) throws Exception {
		String code = "";
		//生成4位随机数验证码
		for ( int i = 0; i < 4; i++) {
			Random random = new Random();
			code += random.nextInt(10);
		}
		//验证码入缓存
		redisService.set(cacheKey, code, second);
		String content = code;
		//1.发送短信
		try {
			SmsCodeUtils.sendSmsCodeAli(mobile, content, "");
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 验证短信验证码是否正确 
	 * @param cacheKey 缓存key
	 * @param smscode   验证码
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Boolean checkSmsCode(String cacheKey, String smscode) throws Exception {
		//获取缓存中存放的手机短信验证码 
		if(redisService.getObj(cacheKey)==null){
			return false;
		}
		String cacheCode = (String)redisService.getObj(cacheKey);
		if(smscode != null && smscode.equals(cacheCode)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 用户注册
	 * @param nickname  昵称
	 * @param mobile   手机号
	 * @param password   密码
	 * @param i        角色（普通用户：1）
	 * @return
	 */
	@Override
	public User register(String nickname, String mobile, String password, int i) {
		//判断该手机号是否已经注册
		boolean exist = queryUserExits(mobile);
		AssertUtil.isFalse(exist,GczjStatusCode.person_login_error);
		
		User user = new User();
		user.setId(UUIDGenerator.getUUID());
		user.setNickname(nickname);
		user.setMobile(Long.valueOf(mobile));
		user.setRole(1);
		user.setUpdated(new Date());
		user.setCreated(new Date());
		user.setDeleted(false);
		//用户预置5次免费生成文档
		user.setTimes(5);
		String rawMd5Password = PassportSecurity.getRawPassword(user.getId(), password);
		user.setPassword(PWDEncoder.encodePassword(rawMd5Password, PassportSecurity.SECURITY));

		insertSelective(user);
		return user;
	}
	
	/**
	 * 判断该手机号是否已经注册
	 * @param mobile
	 * @return
	 */
	public boolean queryUserExits(String mobile){
		User user = queryUserMobile(mobile);
		if(user!=null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 通过用手机号查询该用户信息
	 * @param mobile 
	 * @return
	 */
	public User queryUserMobile(String mobile){
		User user ;
		if(mobile.matches(RegEx.MOBILE_REGEX)){
			user = userDao.queryByMobile(mobile);
		}else{
			user= null;
		}
		return user;
	}
	
	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password 密码
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Token login(String mobile, String password) throws Exception {
		AssertUtil.hasLength(password, GczjStatusCode.password_empty_null);
		User user = queryUserMobile(mobile);
		AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
		AssertUtil.isTrue(Boolean.FALSE.equals(user.getDeleted()), GczjStatusCode.user_login_error_lock, "账号"+mobile+"已被锁定，如有问题请拨打客服电话400-184-5656");
		
		String rawMd5Password = PassportSecurity.getRawPassword(user.getId(), password);
		AssertUtil.isTrue(PWDEncoder.check(rawMd5Password, user.getPassword(), PassportSecurity.SECURITY), GczjStatusCode.user_login_password_error);
		
		return generatingToken(user);
	}
	
	/**
	 * token生成相关方法  并且将token放入缓存中 以便查询 userid
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Token generatingToken(User user) throws Exception{
		UserAuth auth = new UserAuth(user.getId());
		//设置刷新TOKEN
		auth.setRefreshToken(generatingRefreshToken(user.getId()));
		//设置访问TOKEN
		auth.setAccessToken(generatingAccessToken(user.getId(), auth.getRefreshToken()));
		//设置有效期
		auth.setExpire(getExpireAtCurrent());
		String usrjsonString = JSON.toJSONString(user);
		//access_token入缓存
		redisService.set(GczjCacheKeys.AUTH_USER_ACCESS_TOKEN + user.getId(), new AccessTokenCache(auth.getAccessToken(), auth.getExpire().getTime()), GczjCacheKeys.AUTH_USER_ACCESS_TOKEN_EXPIRE);
		redisService.set(auth.getAccessToken(), usrjsonString, GczjCacheKeys.AUTH_USER_ACCESS_TOKEN_EXPIRE);
		//封装Token并返回
		return new Token(user, auth.getAccessToken(), auth.getRefreshToken(), auth.getExpire());
	}
	
	/**
	 * 刷新密码
	 * @param userId
	 * @return
	 * String
	 */
	protected String generatingRefreshToken(String userId){
		return Codings.MD5Encoding(userId + PassportSecurity.SECURITY + System.currentTimeMillis());
	}
	
	/**
	 * 创建访问Access_Token
	 * @param userId
	 * @param refreshToken
	 * @return
	 * String
	 */
	protected String generatingAccessToken(String userId, String refreshToken){
		String rawPassword = PassportSecurity.getRawPassword(userId, refreshToken);
		return PWDEncoder.encodePassword(rawPassword, PassportSecurity.SECURITY);
	}
	
	/**
	 * 取过期时间
	 * @return
	 * Date
	 */
	protected Date getExpireAtCurrent(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 7);//Constants.AUTH_TOKEN_EXPIRE 7天
		return calendar.getTime();
	}

	/**
	 * 验证密码
	 * @param user  用户
	 * @param password  密码
	 * @return
	 */
	@Override
	public boolean checkPassword(User user, String password) {
		String rawMd5Password = PassportSecurity.getRawPassword(user.getId(), password);
		return PWDEncoder.check(rawMd5Password, user.getPassword(), PassportSecurity.SECURITY);
	}

	/**
	 * 修改密码
	 * @param id  用户id
	 * @param mobile  手机号
	 * @param newPassword  新密码
	 */
	@Override
	public void changePassword(String id, String mobile, String newPassword) {
		User user = (User)userDao.selectByPrimaryKey(id);
		AssertUtil.notNull(user,GczjStatusCode.user_notexist_error);
		String rawMd5Password = PassportSecurity.getRawPassword(user.getId(), newPassword);
		String password = PWDEncoder.encodePassword(rawMd5Password, PassportSecurity.SECURITY);
		User userUpdate = new User();
		userUpdate.setPassword(password);
		userUpdate.setUpdated(new Date());
		userUpdate.setId(id);
		updateByPrimaryKeySelective(userUpdate);
	}

	/**
	 * 用户信息列表
	 * @param request
	 * @param response
	 * @param role  用户类型
	 * @param pageNum  页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	@Override
	public Map<Integer, List<User>> getUserList(Integer pageSize, Integer pageNum, Integer role) {
		Map<Integer, List<User>> map = new HashMap<Integer, List<User>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where 1=1");
		if(role!=null){
			sqlCondition.append(" and role = "+role);	
		}
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<User> userList = userDao.getUserList(page);
		map.put(page.getTotalCount(), userList);
		return map;
	}
	
	@Override
	public void updateUserInfo(String id, String graduateSchool, String specialized,
			String companyName, String workplace, String placeWork,String url,String nickName){
		User model = (User) userDao.selectByPrimaryKey(id);
		model.setUpdated(new Date());
		model.setGraduateSchool(graduateSchool);
		model.setSpecialized(specialized);
		model.setCompanyName(companyName);
		model.setWorkplace(workplace);
		model.setPlaceWork(placeWork);
		model.setNickname(nickName);
		model.setReservedFirst(url);
		userDao.updateByPrimaryKeySelective(model);
	}
	
	
}
