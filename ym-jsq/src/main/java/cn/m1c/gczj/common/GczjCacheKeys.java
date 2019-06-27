package cn.m1c.gczj.common;

import cn.m1c.frame.constants.CacheKeys;

/**
 * 说明:
 * 
 * 缓存key值
 * 
 */
public abstract class GczjCacheKeys extends CacheKeys{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3913788835773239753L;
	
	/**注册验证码缓存有效时间1分钟*/
	public static final int SMSCODE_INTERVAL_CHECKCODE_EXPIRE_1HOUR = 60;
	
	/*****获取短信验证码*******/
	//频繁调用次数控制在50秒--两次间隔时间 同一个手机号
	public static final String SMSCODE_INTERVAL_CHECKCODE = "SMSCODE_INTERVAL_CHECKCODE_";
	
	
	
	/**登录认证Access_Token码*/
	public static final String AUTH_USER_ACCESS_TOKEN = "auth_access_token_person_";
	/**登录认证Access_Token码失效时间7天*/
	public static final int AUTH_USER_ACCESS_TOKEN_EXPIRE =7 * 24 * 3600;
		
		
		
	public static <T> String getSingleEntityKey(Class<? extends T> entityClass, long id){
		return String.format(SINGLE_ENTITY, entityClass.getName(), id);
	}
}
