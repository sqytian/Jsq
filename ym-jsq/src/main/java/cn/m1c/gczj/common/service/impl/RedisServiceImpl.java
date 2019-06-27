package cn.m1c.gczj.common.service.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.m1c.gczj.common.service.RedisService;
import cn.m1c.gczj.common.service.SerializeUtils;

@SuppressWarnings({ "unchecked" })
@Service("redisService")
public class RedisServiceImpl<T> implements RedisService<T> {

	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(RedisServiceImpl.class);

	private static RedisTemplate<Serializable, Serializable> redisTemplate;

	private long expirationtime;

	public long getExpirationtime() {

		return expirationtime;

	}

	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {

		return redisTemplate;

	}

	@SuppressWarnings("static-access")
	public void setRedisTemplate(

	RedisTemplate<Serializable, Serializable> redisTemplate) {

		this.redisTemplate = redisTemplate;

	}

	public void setExpirationtime(long expirationtime) {

		this.expirationtime = expirationtime;

	}

	/**
	 * 
	 * 保存内容到缓存中
	 */

	public void save(final String key, Object value) throws Exception {

		final byte[] vbytes = SerializeUtils.serialize(value);

		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)

			throws DataAccessException {

				connection.set(
						redisTemplate.getStringSerializer().serialize(key),
						vbytes);

				return null;

			}

		});

	}

	/**
	 * 
	 * 通过key取到保存在缓存中的内容
	 */

	public <T> T getByKey(final String key, Class<T> elementType)
			throws Exception {

		try {

			return redisTemplate.execute(new RedisCallback<T>() {

				public T doInRedis(RedisConnection connection)

				throws DataAccessException {

					byte[] keyBytes = redisTemplate.getStringSerializer()
							.serialize(key);

					if (connection.exists(keyBytes)) {

						byte[] valueBytes = connection.get(keyBytes);

						T value = (T) SerializeUtils.unserialize(valueBytes);

						return value;

					}

					return null;

				}

			});

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 通过传递key删除所有的在缓存中的内容
	 * 
	 * @param key
	 * 
	 * @attention 这里个的key是可变参数的key
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public void del(String... key) throws Exception {

		if (key != null && key.length > 0) {

			if (key.length == 1) {

				redisTemplate.delete(key[0]);

			} else {

				redisTemplate.delete(CollectionUtils.arrayToList(key));

			}

		}

	}

	/**
	 * 
	 * 批量删除<br/>
	 * 
	 * @param pattern
	 * 
	 * @throws Exception
	 * 
	 * @attention 该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public void batchDel(String... pattern) throws Exception {

		for (String pt : pattern) {

			redisTemplate.delete(redisTemplate.keys(pt + "*"));

		}

	}

	/**
	 * 
	 * 取得缓存(int型)
	 * 
	 * @param key
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
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public Integer getInt(String key) throws Exception {

		String value = (String) redisTemplate.boundValueOps(key).get();

		if (StringUtils.isNotBlank(value)) {

			return Integer.valueOf(value);

		}

		return null;

	}

	/**
	 * 
	 * 取得缓存(字符串类型)
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 * @attention 方法的使用注意事项
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public String getStr(String key) throws Exception {

		return (String) redisTemplate.boundValueOps(key).get();

	}

	/**
	 * 
	 * 取得缓存(字符串类型)
	 * 
	 * @param key
	 * 
	 * @param retain
	 * 
	 * @return
	 * 
	 * @attention
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public String getStr(String key, boolean retain) throws Exception {

		String value = (String) redisTemplate.boundValueOps(key).get();

		if (!retain) {

			redisTemplate.delete(key);

		}

		return value;

	}

	/**
	 * 
	 * 获取缓存<br>
	 * 
	 * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
	 * 
	 * @param key
	 * 
	 * @return
	 */

	public Object getObj(String key) throws Exception {

		return redisTemplate.boundValueOps(key).get();

	}

	/**
	 * 
	 * 获取缓存<br>
	 * 
	 * @param key
	 * 
	 * @param retain
	 *            是否保留
	 * 
	 * @return
	 * 
	 * @attention 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
	 * 
	 * @author toto
	 * 
	 * @date 2017-1-12
	 * 
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */

	public Object getObj(String key, boolean retain) throws Exception {

		Object obj = redisTemplate.boundValueOps(key).get();

		if (!retain) {

			redisTemplate.delete(key);

		}

		return obj;

	}

	/**
	 * 
	 * 获取缓存<br>
	 * 
	 * 注：该方法暂不支持Character数据类型
	 * 
	 * @param key
	 *            key
	 * 
	 * @param clazz
	 *            类型
	 * 
	 * @return
	 */

	public <T> T get(String key, Class<T> clazz) throws Exception {

		return (T) redisTemplate.boundValueOps(key).get();

	}

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
			throws Exception {

		if (value.getClass().equals(String.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Integer.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Double.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Float.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Short.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Long.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else if (value.getClass().equals(Boolean.class)) {

			redisTemplate.opsForValue().set(key, value.toString());

		} else {

			redisTemplate.opsForValue().set(key,
					SerializeUtils.serialize(value));

		}

		if (expirationtime > 0) {

			redisTemplate.expire(key, expirationtime, TimeUnit.SECONDS);

		}

	}

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

	public double decr(String key, double by) throws Exception {

		return redisTemplate.opsForValue().increment(key, -by);

	}

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

	public double incr(String key, double by) throws Exception {

		return redisTemplate.opsForValue().increment(key, by);

	}

	/**
	 * 
	 * 获取double类型值
	 * 
	 * @param key
	 * 
	 * @return
	 */

	public double getDouble(String key) throws Exception {

		String value = (String) redisTemplate.boundValueOps(key).get();

		if (StringUtils.isNotBlank(value)) {

			return Double.valueOf(value);

		}

		return 0d;

	}

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
			throws Exception {

		redisTemplate.opsForValue().set(key, String.valueOf(value));

		if (expirationtime > 0) {

			redisTemplate.expire(key, expirationtime, TimeUnit.SECONDS);

		}

	}

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
			throws Exception {

		redisTemplate.opsForValue().set(key, String.valueOf(value));

		if (expirationtime > 0) {

			redisTemplate.expire(key, expirationtime, TimeUnit.SECONDS);

		}

	}

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
			throws Exception {

		redisTemplate.opsForHash().putAll(key, map);

	}

	/**
	 * 
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 * 
	 * @param map
	 */

	public <T> void addMap(String key, Map<String, T> map) throws Exception {

		redisTemplate.opsForHash().putAll(key, map);

	}

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

	public void addMap(String key, String field, String value) throws Exception {

		redisTemplate.opsForHash().put(key, field, value);

	}

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

	public <T> void addMap(String key, String field, T obj) throws Exception {

		redisTemplate.opsForHash().put(key, field, obj);

	}

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

	public <T> Map<String, T> mget(String key, Class<T> clazz) throws Exception {

		BoundHashOperations<Serializable, String, T> boundHashOperations = redisTemplate
				.boundHashOps(key);

		return boundHashOperations.entries();

	}

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
			throws Exception {

		return (T) redisTemplate.boundHashOps(key).get(field);

	}

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

	public void delMapField(String key, String... field) throws Exception {

		BoundHashOperations<Serializable, String, Object> boundHashOperations = redisTemplate
				.boundHashOps(key);

		boundHashOperations.delete(field);

	}

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

	public void expire(String key, long expirationtime) throws Exception {

		if (expirationtime > 0) {

			redisTemplate.expire(key, expirationtime, TimeUnit.SECONDS);

		}

	}

	/**
	 * 
	 * 添加set
	 * 
	 * @param key
	 * 
	 * @param value
	 */

	public void sadd(String key, String... value) throws Exception {

		redisTemplate.boundSetOps(key).add(value);

	}

	/**
	 * 
	 * 删除set集合中的对象
	 * 
	 * @param key
	 * 
	 * @param value
	 */

	public void srem(String key, String... value) throws Exception {

		redisTemplate.boundSetOps(key).remove(value);

	}

	/**
	 * 
	 * set重命名
	 * 
	 * @param oldkey
	 * 
	 * @param newkey
	 */

	public void srename(String oldkey, String newkey) throws Exception {

		redisTemplate.boundSetOps(oldkey).rename(newkey);

	}

	/**
	 * 
	 * 模糊查询keys
	 * 
	 * @param pattern
	 * 
	 * @return
	 */

	public Set<Serializable> keys(String pattern) throws Exception {

		return redisTemplate.keys(pattern);

	}

}