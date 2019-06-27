package cn.m1c.gczj.person.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.PWDEncoder;
import cn.m1c.frame.utils.PassportSecurity;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.AccessTokenCache;
import cn.m1c.gczj.common.AdminToken;
import cn.m1c.gczj.common.Codings;
import cn.m1c.gczj.common.GczjCacheKeys;
import cn.m1c.gczj.common.RegEx;
import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.person.dao.AdminDao;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.person.model.UserAuth;
import cn.m1c.gczj.person.service.AdminService;

@Service("adminService")
public class AdminServiceImpl extends BaseServiceImpl implements AdminService{

	@Resource
	private AdminDao adminDao;
	
	@SuppressWarnings("rawtypes")
	@Resource(name = "redisService")
	protected RedisService redisService;
	
	@Override
	public IBaseDao getDao() {
		return adminDao;
	}

	/**
	 * 通过用手机号查询该用户信息
	 * @param mobile 
	 * @return
	 */
	@Override
	public Admin queryAdminMobile(String mobile) {
		Admin admin ;
		if(mobile.matches(RegEx.MOBILE_REGEX)){
			admin = adminDao.queryByMobile(mobile);
		}else{
			admin= null;
		}
		return admin;
	}

	/**
	 * 验证密码
	 * @param user  用户
	 * @param password  密码
	 * @return
	 */
	@Override
	public boolean checkPassword(Admin admin, String password) {
		String rawMd5Password = PassportSecurity.getRawPassword(admin.getId(), password);
		return PWDEncoder.check(rawMd5Password, admin.getPassword(), PassportSecurity.SECURITY);
	}

	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password 密码
	 * @return
	 * @throws Exception 
	 */
	@Override
	public AdminToken login(String mobile, String password) throws Exception {
		AssertUtil.hasLength(password, GczjStatusCode.password_empty_null);
		Admin admin = queryAdminMobile(mobile);
		AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
		
		String rawMd5Password = PassportSecurity.getRawPassword(admin.getId(), password);
		AssertUtil.isTrue(PWDEncoder.check(rawMd5Password, admin.getPassword(), PassportSecurity.SECURITY), GczjStatusCode.user_login_password_error);
		
		return generatingToken(admin);
	}
	
	/**
	 * token生成相关方法  并且将token放入缓存中 以便查询 adminid
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public AdminToken generatingToken(Admin admin) throws Exception{
		UserAuth auth = new UserAuth(admin.getId());
		//设置刷新TOKEN
		auth.setRefreshToken(generatingRefreshToken(admin.getId()));
		//设置访问TOKEN
		auth.setAccessToken(generatingAccessToken(admin.getId(), auth.getRefreshToken()));
		//设置有效期
		auth.setExpire(getExpireAtCurrent());
		String usrjsonString = JSON.toJSONString(admin);
		//access_token入缓存
		redisService.set(GczjCacheKeys.AUTH_USER_ACCESS_TOKEN + admin.getId(), new AccessTokenCache(auth.getAccessToken(), auth.getExpire().getTime()), GczjCacheKeys.AUTH_USER_ACCESS_TOKEN_EXPIRE);
		redisService.set(auth.getAccessToken(), usrjsonString, GczjCacheKeys.AUTH_USER_ACCESS_TOKEN_EXPIRE);
		//封装Token并返回
		return new AdminToken(admin, auth.getAccessToken(), auth.getRefreshToken(), auth.getExpire());
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
	 * 刷新密码
	 * @param userId
	 * @return
	 * String
	 */
	protected String generatingRefreshToken(String userId){
		return Codings.MD5Encoding(userId + PassportSecurity.SECURITY + System.currentTimeMillis());
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
	 * 修改密码
	 * @param id  用户id
	 * @param mobile  手机号
	 * @param newPassword  新密码
	 */
	@Override
	public void changePassword(String id, String mobile, String newPassword) {
		Admin admin = (Admin)adminDao.selectByPrimaryKey(id);
		AssertUtil.notNull(admin,GczjStatusCode.user_notexist_error);
		String rawMd5Password = PassportSecurity.getRawPassword(admin.getId(), newPassword);
		String password = PWDEncoder.encodePassword(rawMd5Password, PassportSecurity.SECURITY);
		Admin userUpdate = new Admin();
		userUpdate.setPassword(password);
		userUpdate.setUpdated(new Date());
		userUpdate.setId(id);
		updateByPrimaryKeySelective(userUpdate);
		
	}
	
	/**
	 * 判断该手机号是否已经注册
	 * @param mobile
	 * @return
	 */
	public boolean queryUserExits(String mobile){
		Admin user = queryAdminMobile(mobile);
		if(user!=null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Admin register(String nickname, String mobile, String password, int i) {
		//判断该手机号是否已经注册
		boolean exist = queryUserExits(mobile);
		AssertUtil.isFalse(exist,GczjStatusCode.person_login_error);
		
		Admin admin = new Admin();
		admin.setId(UUIDGenerator.getUUID());
		admin.setNickname(nickname);
		admin.setMobile(Long.valueOf(mobile));
		admin.setRole(1);
		admin.setUpdated(new Date());
		admin.setCreated(new Date());
		admin.setDeleted(false);
		String rawMd5Password = PassportSecurity.getRawPassword(admin.getId(), password);
		admin.setPassword(PWDEncoder.encodePassword(rawMd5Password, PassportSecurity.SECURITY));

		insertSelective(admin);
		return admin;
	}
}
