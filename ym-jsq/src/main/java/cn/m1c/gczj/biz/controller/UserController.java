package cn.m1c.gczj.biz.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.model.BaseModel;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.utils.StreamUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.GczjCacheKeys;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.common.Token;
import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.person.model.User;
import cn.m1c.gczj.person.service.UserService;
import cn.m1c.gczj.utils.DateUtils;

@Controller
@RequestMapping("/api/user")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	@SuppressWarnings("rawtypes")
	@Resource(name = "redisService")
	protected RedisService redisService;
	
	
	/**
	 * 发送短信验证码
	 * @param request
	 * @param response
	 * @param mobile  手机号
	 * @param kaptcha 图片验证码
	 */
	@RequestMapping(value = {"/sendsmscode"})
	public void sendSmscode(HttpServletRequest request,HttpServletResponse response
			,String mobile) {
		RpcResult rpcResult = null;
		log.info("/user/sendsmscode");
		try{
			//校验手机号不为空
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			//判断验证码是否失效，未失效报错
			AssertUtil.isNull(redisService.getObj(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile), GczjStatusCode.sms_often_error);
			//生成4位验证码并发送给手机并存放到redis里
			userService.sendSmsCode(mobile, GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile, GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE_EXPIRE_1HOUR);
			
			rpcResult = RpcResult.status(GczjStatusCode.sms_sned_success);
		}catch(M1CRuntimeException e){
			rpcResult = RpcResult.status(e.getStatusCode());
		}catch (Exception e) {
			log.error("错误信息： "+e);
			rpcResult = RpcResult.status(GczjStatusCode.sms_sned_success_failed);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 手机注册 
	 * @param nickname    昵称
	 * @param mobile      手机号
	 * @param password    密码
	 * @param smscode    验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/registerbymobile")
	public void registerByMobile(HttpServletRequest request,HttpServletResponse response
			,String nickname,String mobile,String password,String smscode){
		RpcResult rpcResult = null;
		log.info("/user/registerbymobile");
		try{
			//校验参数不为空
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			AssertUtil.notNull(password, GczjStatusCode.password_empty_null);
			AssertUtil.notNull(smscode, GczjStatusCode.smscode_empty_null);
			//校验验证码是否正确
			Boolean result = userService.checkSmsCode(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile, smscode);
			//当判断表达式不为真时，报错
			AssertUtil.isTrue(result.booleanValue(), GczjStatusCode.smscode_interval_error);
			//删除验证码缓存
			redisService.del(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile);
			//1.注册
			User user = userService.register(nickname, mobile, password, 1);
			//2 登陆
			Token token = userService.login(mobile, password);
			rpcResult = RpcResult.status(GczjStatusCode.user_register_success);
			rpcResult.addDatabody("userId", user.getId());
			rpcResult.addDatabody("accessToken", token.getAccessToken());
			rpcResult.addDatabody("refreshToken", token.getRefreshToken());
			rpcResult.addDatabody("expiersTime", token.getExpiers().getTime());
		}
		catch(M1CRuntimeException e){
			rpcResult = RpcResult.status(e.getStatusCode(), e.getMessage());
			log.error("注册失败",e);
		}
		catch (Exception e) {
			log.error("注册失败：", e);
			rpcResult = RpcResult.status(StatusCode.failed);
			e.printStackTrace();
		}
		
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password  密码
	 * @param smscode   短信验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/loginbymobile")
	public void loginByMobile(HttpServletRequest request,HttpServletResponse response
			,String nickname,String mobile,String password,String  smscode){
		log.info("/user/loginbymobile");
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.user_login_success);
		try{
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			AssertUtil.notNull(password, GczjStatusCode.password_empty_null);
//			AssertUtil.notNull(smscode, GczjStatusCode.smscode_empty_null);
			//通过手机号查询用户
			User user = userService.queryUserMobile(mobile);
			AssertUtil.isTrue(user!=null, GczjStatusCode.user_notexist_error);
			AssertUtil.isTrue(userService.checkPassword(user, password), GczjStatusCode.user_login_password_error);
			AssertUtil.isTrue(Boolean.FALSE.equals(user.getDeleted()), GczjStatusCode.user_login_error_lock, "账号"+mobile+"已被锁定，如有问题请拨打客服电话400-184-5656");
//			if(smscode!=null){
//				//获取缓存中存放的手机短信验证码 
//				Boolean result = userService.checkSmsCode(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile, smscode);
//				AssertUtil.isTrue(result, GczjStatusCode.smscode_interval_error);
//				redisService.del(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile);
//			}
			//登陆
			Token token = userService.login(mobile, password);
			log.info("token:"+token.getAccessToken());
			//获取登陆用户
			User user2 = token.getUser();
			
			//返回结果
			rpcResult.addDatabody("userId", user2.getId());
			rpcResult.addDatabody("accessToken", token.getAccessToken());
			rpcResult.addDatabody("nickName", user2.getNickname());
			rpcResult.addDatabody("expiersTime", token.getExpiers().getTime());
					
		}
		catch(M1CRuntimeException e){
			log.error("登陆失败：", e);
			rpcResult = RpcResult.status(e.getStatusCode(), e.getMessage());
		}
		catch (Exception e) {
			log.error("登陆失败：", e);
			rpcResult = RpcResult.status(StatusCode.failed);
			e.printStackTrace();
		}
		
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 重置密码
	 * @param mobile  手机号
	 * @param newPassword  新密码
	 * @param smscode  短信验证码
	 * @param request
	 * @param response
	 */
	@Security
	@RequestMapping(value = "/resetpassword")
	public void resetPassword(HttpServletRequest request,HttpServletResponse response
			,String nickname,String mobile,String newPassword,String smscode){
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.user_change_password_success);
		log.info("/user/resetpassword");
		try{
			//获取用户信息
			User user = (User)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			AssertUtil.notNull(newPassword, GczjStatusCode.password_empty_null);
			AssertUtil.notNull(smscode, GczjStatusCode.smscode_empty_null);
			Boolean result = userService.checkSmsCode(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile, smscode);
			AssertUtil.isTrue(result, GczjStatusCode.smscode_interval_error);
			redisService.del(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile);
			//更新用户密码
			userService.changePassword(user.getId(), mobile, newPassword);
			//返回结果
			rpcResult = RpcResult.status(GczjStatusCode.user_change_password_success);
			rpcResult.addDatabody("userId", user.getId())
				     .addDatabody("mobile", user.getMobile());
		}
		catch(M1CRuntimeException e){
			rpcResult = RpcResult.status(e.getStatusCode(), e.getMessage());
		}
		catch (Exception e) {
			log.error("重置密码失败：", e);
			rpcResult = RpcResult.status(GczjStatusCode.user_change_password_failed);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 找回密码
	 * @param request
	 * @param response
	 * @param mobile
	 * @param newPassword
	 * @param smscode
	 */
	@RequestMapping(value = "/findpassword")
	public void findPassword(HttpServletRequest request,HttpServletResponse response
			,String mobile,String newPassword,String smscode){
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.user_find_password_success);
		log.info("/user/findpassword");
		try{
			
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			AssertUtil.notNull(newPassword, GczjStatusCode.password_empty_null);
			AssertUtil.notNull(smscode, GczjStatusCode.smscode_empty_null);
			//获取用户信息
			User user = userService.queryUserMobile(mobile);
			AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
			Boolean result = userService.checkSmsCode(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile, smscode);
			AssertUtil.isTrue(result, GczjStatusCode.smscode_interval_error);
			redisService.del(GczjCacheKeys.SMSCODE_INTERVAL_CHECKCODE+mobile);
			//更新用户密码
			userService.changePassword(user.getId(), mobile, newPassword);
			//返回结果
			rpcResult.addDatabody("userId", user.getId())
			.addDatabody("mobile", user.getMobile());
		}
		catch(M1CRuntimeException e){
			rpcResult = RpcResult.status(e.getStatusCode(), e.getMessage());
		}
		catch (Exception e) {
			log.error("找回密码失败：", e);
			rpcResult = RpcResult.status(GczjStatusCode.user_find_password_failed);
		}
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 修改/添加个人信息
	 * @param request
	 * @param response
	 * @param id
	 * @param graduateSchool 毕业院校
	 * @param specialized  专业方向
	 * @param companyName 公司名称
	 * @param workplace  工作岗位
	 * @param placeWork  工作地点
	 * @param url  头像url
	 */
	@Security
	@RequestMapping(value="/saveuser")
	public void saveUserInfo(HttpServletRequest request, HttpServletResponse response
			,String id,String graduateSchool,String specialized
			,String companyName,String workplace,String placeWork,String url,String nickName
			){
		log.info("/user/saveuser");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//获取用户信息
			User user = (User)HttpHelper.getAuthUser(request);
//			User user = (User)userService.selectByPrimaryKey(id);
			AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
			//校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.id_null);
			userService.updateUserInfo(id,graduateSchool,
					specialized,companyName,workplace,placeWork,url,nickName);
		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			log.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			log.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
	/**
	 * 用户信息列表
	 * @param request
	 * @param response
	 * @param id
	 */
	@Security
	@RequestMapping(value="/userinfo")
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response
			,String id
			){
		log.info("/user/userinfo");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(StatusCode.success);
		try {
			//获取用户信息
			User user = (User)HttpHelper.getAuthUser(request);
//			User user = (User)userService.selectByPrimaryKey(id);
			AssertUtil.notNull(user, GczjStatusCode.user_notexist_error);
			// 校验参数不为空
			AssertUtil.notNull(id, GczjStatusCode.id_null);
			User model = (User) userService.selectByPrimaryKey(id);
			if (model != null) {
				rpcResult
						.addDatabody("id", model.getId())
						.addDatabody("mobile", model.getMobile())
						.addDatabody("nickName", model.getNickname())
						// 毕业院校
						.addDatabody("graduateSchool",model.getGraduateSchool())
						// 公司名称
						.addDatabody("companyName", model.getCompanyName())
						// 工作岗位
						.addDatabody("workplace", model.getWorkplace())
						// 工作地点
						.addDatabody("placeWork", model.getPlaceWork())
						//头像
						.addDatabody("url", model.getReservedFirst())
						// 专业方向
						.addDatabody("specialized", model.getSpecialized());
			}

		} catch (M1CRuntimeException e) {
			rpcResult = RpcResult.status(e.getStatusCode());
			log.error(rpcResult.getMessage(),e);
		} catch (Exception e){
			rpcResult = RpcResult.status(StatusCode.failed);
			log.error(StatusCode.failed.getMessage(),e);
		}
		//接口返回值
		rpcResult.outCrossOrigin(response);
	}
	
}
