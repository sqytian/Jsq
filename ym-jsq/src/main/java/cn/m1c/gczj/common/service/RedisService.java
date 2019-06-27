package cn.m1c.gczj.common.service;

import java.io.Serializable;

import java.util.Map;

import java.util.Set;

/**
 * RedisService.java redis的服务操作类
 * 
 * @attention
 * @author toto
 * @date 2017-1-12
 * @note begin modify by 涂作权 2017-1-12 原始创建
 */
public interface RedisService<T> {
	/**
	 * 将内容保存到缓存中
	 * @param key
	 * @param value
	 * @throws Exception
	 * @attention
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public void save(final String key, Object value) throws Exception;

	/**
	 * 
	 * 获取内存中的内容
	 * @param <T>
	 * @param key
	 * @param elementType
	 * @return
	 * @throws Exception
	 * @attention
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 原始创建 2017-1-12 原始创建
	 */

	public <T> T getByKey(final String key, Class<T> elementType)
			throws Exception;

	/**
	 * 
	 * 通过传递key删除所有的在缓存中的内容
	 * @param key
	 * @attention
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public void del(String... key) throws Exception;

	/**
	 * 批量删除<br/>
	 * @param pattern
	 * @throws Exception
	 * @attention 该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public void batchDel(String... pattern) throws Exception;

	/**
	 * 
	 * 取得缓存(int型)
	 * @param key
	 * @return
	 * @throws Exception
	 * @attention 方法的使用注意事项
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public Integer getInt(String key) throws Exception;

	/**
	 * 
	 * 取得缓存(字符串类型)
	 * @param key
	 * @return
	 * @attention 方法的使用注意事项
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public String getStr(String key) throws Exception;

	/**
	 * 取得缓存(字符串类型)
	 * @param key
	 * @param retain
	 * @return
	 * @throws Exception
	 * @attention
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public String getStr(String key, boolean retain) throws Exception;

	/**
	 * 
	 * 获取缓存<br>
	 * @param key
	 * @return
	 * @throws Exception
	 * @attention 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public Object getObj(String key) throws Exception;

	/**
	 * 
	 * 获取缓存<br>
	 * @param key
	 * @param retain
	 *            是否保留
	 * @return
	 * @attention 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public Object getObj(String key, boolean retain) throws Exception;

	/**
	 * 
	 * 获取缓存
	 * 
	 * @param <T>
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 * @attention 方法的使用注意事项
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 修改人 修改时间 修改内容摘要说明
	 */

	public <T> T get(String key, Class<T> clazz) throws Exception;

	/**
	 * 
	 * 将value对象写入缓存
	 * 
	 * @param key
	 * 
	 * @param value
	 * 
	 * @param time
	 *            失效时间(秒)
	 */

	public void set(String key, Object value, long expirationtime)
			throws Exception;

	/**
	 * 
	 * 递减操作
	 * 
	 * @param key
	 * 
	 * @param by
	 * 
	 * @return
	 */

	public double decr(String key, double by) throws Exception;

	/**
	 * 
	 * 递增操作
	 * 
	 * @param key
	 * 
	 * @param by
	 * 
	 * @return
	 */

	public double incr(String key, double by) throws Exception;

	/**
	 * 
	 * 获取double类型值
	 * 
	 * @param key
	 * 
	 * @return
	 */

	public double getDouble(String key) throws Exception;

	/**
	 * 
	 * 设置double类型值
	 * 
	 * @param key
	 * 
	 * @param value
	 * 
	 * @param time
	 *            失效时间(秒)
	 */

	public void setDouble(String key, double value, long expirationtime)
			throws Exception;

	/**
	 * 
	 * 设置double类型值
	 * 
	 * @param key
	 * 
	 * @param value
	 * 
	 * @param time
	 *            失效时间(秒)
	 */

	public void setInt(String key, int value, long expirationtime)
			throws Exception;

	/**
	 * 
	 * 将map写入缓存
	 * 
	 * @param key
	 * 
	 * @param map
	 * 
	 * @param time
	 *            失效时间(秒)
	 */

	public <T> void setMap(String key, Map<String, T> map, long expirationtime)
			throws Exception;

	/**
	 * 
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 * 
	 * @param map
	 */

	public <T> void addMap(String key, Map<String, T> map) throws Exception;

	/**
	 * 
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 *            cache对象key
	 * 
	 * @param field
	 *            map对应的key
	 * 
	 * @param value
	 *            值
	 */

	public void addMap(String key, String field, String value) throws Exception;

	/**
	 * 
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 *            cache对象key
	 * 
	 * @param field
	 *            map对应的key
	 * 
	 * @param obj
	 *            对象
	 */

	public <T> void addMap(String key, String field, T obj) throws Exception;

	/**
	 * 
	 * 获取map缓存
	 * 
	 * @param key
	 * 
	 * @param clazz
	 * 
	 * @return
	 */

	public <T> Map<String, T> mget(String key, Class<T> clazz) throws Exception;

	/**
	 * 
	 * 获取map缓存中的某个对象
	 * 
	 * @param key
	 * 
	 * @param field
	 * 
	 * @param clazz
	 * 
	 * @return
	 */

	public <T> T getMapField(String key, String field, Class<T> clazz)
			throws Exception;

	/**
	 * 
	 * 删除map中的某个对象
	 * 
	 * @author lh
	 * 
	 * @date 2016年8月10日
	 * 
	 * @param key
	 *            map对应的key
	 * 
	 * @param field
	 *            map中该对象的key
	 */

	public void delMapField(String key, String... field) throws Exception;

	/**
	 * 
	 * 指定缓存的失效时间
	 * 
	 *
	 * 
	 * @author FangJun
	 * 
	 * @date 2016年8月14日
	 * 
	 * @param key
	 *            缓存KEY
	 * 
	 * @param time
	 *            失效时间(秒)
	 */

	public void expire(String key, long expirationtime) throws Exception;

	/**
	 * 
	 * 添加set
	 * 
	 * @param key
	 * 
	 * @param value
	 */

	public void sadd(String key, String... value) throws Exception;

	/**
	 * 
	 * 删除set集合中的对象
	 * 
	 * @param key
	 * 
	 * @param value
	 */

	public void srem(String key, String... value) throws Exception;

	/**
	 * 
	 * set重命名
	 * 
	 * @param oldkey
	 * 
	 * @param newkey
	 */

	public void srename(String oldkey, String newkey) throws Exception;

	/**
	 * 
	 * 模糊查询keys
	 * 
	 * @param pattern
	 * 
	 * @return
	 */

	public Set<Serializable> keys(String pattern) throws Exception;

}