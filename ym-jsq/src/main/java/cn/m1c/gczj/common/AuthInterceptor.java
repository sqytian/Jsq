package cn.m1c.gczj.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.m1c.frame.constants.StatusCode;
import cn.m1c.frame.exception.M1CRuntimeException;
import cn.m1c.frame.utils.AssertUtil;
import cn.m1c.frame.utils.HttpHelper;
import cn.m1c.frame.vo.RpcResult;
import cn.m1c.gczj.biz.status.GczjStatusCode;
import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.person.model.Admin;
import cn.m1c.gczj.person.model.User;
import cn.m1c.gczj.person.service.AdminService;
import cn.m1c.gczj.person.service.UserService;
import cn.m1c.gczj.utils.GetRequestJsonUtils;

import com.alibaba.fastjson.JSON;

/**
 * 2016年11月19日 request拦截
 * 
 * @author phil(s@m1c.cn,m1c softCo.,ltd)
 * @version lannie
 */
public class AuthInterceptor implements HandlerInterceptor {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private UserService userService;
	@Resource
	private AdminService adminService;
	@SuppressWarnings("rawtypes")
	@Resource(name = "redisService")
	protected RedisService redisService;

	/*
	 * 在这里拦截权限
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestUrl = request.getRequestURI();
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		
		logger.debug("RequestURL:" + requestUrl);
		// 解决跨域问题

		String method = request.getMethod();

		if (method.equals("OPTIONS")) {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/x-www-form-urlencoded");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
			response.setDateHeader("Expires", 3600); // Proxies.
			response.setHeader("connection", "close");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Headers",
					"x-requested-with, content-type, Accept, x-xsrf-token, Authorization");
			response.setStatus(200);
			return false;
		}

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Security security = getSecurity(handlerMethod);
			Map<String,Object> map = new HashMap<String, Object>();
			//获取参数
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				String[] value = entry.getValue();
				logger.debug("key:" +key);
				logger.debug("value[0]:" + value[0]);
				if("source".equals(key)){
					map.put(key, value[0]);
				}
				if("accessToken".equals(key)){
					map.put(key, value[0]);
				}
				
			}
			
			if (security != null) {
				try {
					AssertUtil.notEmpty(map, StatusCode.forbidden);
					String source = (String)map.get("source");
					logger.debug("source:" +source);
					String accessToken = (String)map.get("accessToken");
					logger.debug("accessToken:" +accessToken);
					AssertUtil.notNull(accessToken, GczjStatusCode.token_null);
					// 如果来自后台
					if ("2".equals(source)) {
						// 从缓存中获取user
						User user =JSON.parseObject(redisService.getStr(accessToken), User.class);
						AssertUtil.notNull(user, StatusCode.forbidden);
						User dUser = (User) userService.selectByPrimaryKey(user.getId());
						AssertUtil.isNotNull(dUser, StatusCode.forbidden);
						AssertUtil.isTrue(Boolean.FALSE.equals(dUser.getDeleted()),
								GczjStatusCode.user_login_error_lock, "账号" + user.getMobile() + "已被锁定，如有问题请拨打客服电话");
						HttpHelper.setAuthUser(request, user);
					} else {
						// 从缓存中获取user
						Admin user = JSON.parseObject(redisService.getStr(accessToken), Admin.class);
						AssertUtil.notNull(user, StatusCode.forbidden);
						Admin admin = (Admin) adminService.selectByPrimaryKey(user.getId());
						AssertUtil.isNotNull(admin, StatusCode.forbidden);
						HttpHelper.setAuthUser(request, user);
					}
				} catch (M1CRuntimeException e) {
					RpcResult.status(e.getStatusCode()).outCrossOrigin(response);
					logger.info("", e);
					return false;
				} catch (Exception e) {
					RpcResult.status(StatusCode.failed).outCrossOrigin(response);
					logger.error("", e);
					return false;
				}
				return true;
			}
		}
		logger.info("12");
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private Security getSecurity(HandlerMethod handlerMethod) {
		Security security = handlerMethod.getMethodAnnotation(Security.class);
		if (security == null) {
			Class<?> beanType = handlerMethod.getBeanType();
			security = beanType.getAnnotation(Security.class);
		}
		return security;
	}

}
