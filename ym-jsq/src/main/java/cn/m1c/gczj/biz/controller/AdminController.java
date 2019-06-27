package cn.m1c.gczj.biz.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.utils.StringUtil;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.AdminToken;
import cn.m1c.gczj.common.Security;
import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.person.model.User;
import cn.m1c.gczj.person.service.AdminService;
import cn.m1c.gczj.person.service.UserService;
import cn.m1c.gczj.utils.DateUtils;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

	private static Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Resource
	private AdminService adminService;
	@Resource
	private UserService userService;
	@SuppressWarnings("rawtypes")
	@Resource(name = "redisService")
	protected RedisService redisService;
	
	/**
	 * 登录
	 * @param mobile  手机号
	 * @param password  密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/loginbymobile",method = {RequestMethod.POST,RequestMethod.GET})
	public void loginByMobile(HttpServletRequest request,HttpServletResponse response
			,String mobile,String password
			){
		log.info("/admin/loginbymobile");
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.user_login_success);
		try{
			AssertUtil.notNull(mobile, GczjStatusCode.mobile_empty_null);
			AssertUtil.notNull(password, GczjStatusCode.password_empty_null);
			//通过手机号查询用户
			Admin admin = adminService.queryAdminMobile(mobile);
			AssertUtil.isTrue(admin!=null, GczjStatusCode.user_notexist_error);
			AssertUtil.isTrue(adminService.checkPassword(admin, password), GczjStatusCode.user_login_password_error);
			//登陆
			AdminToken token = adminService.login(mobile, password);
			log.info("token:"+token.getAccessToken());
			//获取登陆用户
			Admin admin2 = token.getAdmin();
			//返回结果
			rpcResult.addDatabody("userId", admin2.getId());
			//token
			rpcResult.addDatabody("accessToken", token.getAccessToken());
			rpcResult.addDatabody("nickName", admin2.getNickname());
			//有效期时间
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
	 * @param request
	 * @param response
	 */
	@Security
	@RequestMapping(value = "/resetpassword",method = {RequestMethod.POST,RequestMethod.GET})
	public void resetPassword(HttpServletRequest request,HttpServletResponse response
			,String mobile,String newPassword){
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.user_change_password_success);
		log.info("/admin/resetpassword");
		try{
			//获取用户信息
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
//			Admin admin = (Admin)adminService.queryAdminMobile(mobile);
			AssertUtil.isNotNull(admin, GczjStatusCode.user_notexist_error);
			AssertUtil.isTrue(StringUtil.hasText(mobile), GczjStatusCode.mobile_empty_null);
			AssertUtil.isTrue(StringUtil.hasText(newPassword), GczjStatusCode.password_empty_null);
			//更新用户密码
			adminService.changePassword(admin.getId(), mobile, newPassword);
			//返回结果
			rpcResult = RpcResult.status(GczjStatusCode.user_change_password_success);
			rpcResult.addDatabody("adminId", admin.getId())
				     .addDatabody("mobile", admin.getMobile());
		}
		catch(M1CRuntimeException e){
			log.error("重置密码失败：", e);
			rpcResult = RpcResult.status(e.getStatusCode(), e.getMessage());
		}
		catch (Exception e) {
			log.error("重置密码失败：", e);
			rpcResult = RpcResult.status(GczjStatusCode.user_change_password_failed);
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
	@RequestMapping(value = "/registerbymobile",method = {RequestMethod.POST,RequestMethod.GET})
	public void registerByMobile(@RequestParam(required = false, defaultValue = "") String nickname, 
									@RequestParam(required = true) String mobile, 
									@RequestParam(required = true) String password,
									HttpServletRequest request,HttpServletResponse response){
		
		RpcResult rpcResult = null;
		log.info("/admin/registerbymobile");
		try{
			//校验参数不为空
			AssertUtil.isTrue(StringUtil.hasText(mobile), GczjStatusCode.mobile_empty_null);
			AssertUtil.isTrue(StringUtil.hasText(password), GczjStatusCode.password_empty_null);
			//1.注册
			Admin admin = adminService.register(nickname, mobile, password, 1);
			//2 登陆
			AdminToken token = adminService.login(mobile, password);
			rpcResult = RpcResult.status(GczjStatusCode.user_register_success);
			rpcResult.addDatabody("adminId", admin.getId());
			rpcResult.addDatabody("accessToken", token.getAccessToken());
			rpcResult.addDatabody("refreshToken", token.getRefreshToken());
			//有效期时间
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
	 * 用户信息列表
	 * @param request
	 * @param response
	 * @param role  用户类型
	 * @param pageNum  页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	@Security
	@RequestMapping(value="/userlist")
	public void userList(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(defaultValue = "30", required = false) Integer pageSize,
			String role){
		log.info("/admin/userlist");
		//初始化返回值（默认正确）
		RpcResult rpcResult = RpcResult.status(GczjStatusCode.get_users_success);
		try {
			//获取用户信息
			Admin admin = (Admin)HttpHelper.getAuthUser(request);
			AssertUtil.notNull(admin, GczjStatusCode.user_notexist_error);
			//校验参数不为空
			Map<Integer, List<User>> map = userService.getUserList(Integer.valueOf(pageSize), Integer.valueOf(pageNum),Integer.valueOf(role));
			Integer totalCount = map.keySet().iterator().next();
			rpcResult.addAttribute("totalCount", totalCount);
			List<User> userList = map.get(totalCount);
			if(userList!=null && userList.size()>0){
				for(int i=0;i<userList.size();i++){
					User user = userList.get(i);
					rpcResult.addArray().addDatabody("id", user.getId())
					          //手机号
					          .addDatabody("mobile", user.getMobile())
					          //更新时间
					          .addDatabody("updateTime", DateUtils.format(user.getUpdated(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS))
					          //用户类型
					          .addDatabody("role", user.getRole())
					          //昵称
					          .addDatabody("nickName", user.getNickname())
					          //是否冻结
					          .addDatabody("deleted", user.getDeleted())
					          //文档生成次数
					          .addDatabody("times", user.getTimes());
				}
			}else{
				rpcResult.addDatabody("noresult", "暂时没有查到用户信息！ ");
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
