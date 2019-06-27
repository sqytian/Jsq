package cn.m1c.gczj.common.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SerializeUtils.java 序列化相关的操作类
 * 
 * @attention
 * @author toto
 * @date 2017-1-12
 * @note begin modify by 涂作权 2017-1-12 原始创建
 */
public class SerializeUtils {

	/**
	 * 将对象进行序列化
	 * 
	 * @param object
	 * @return
	 * @attention 传递对象，将
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 修改人 修改时间 修改内容摘要说明
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 * @attention 反序列化的操作
	 * @author toto
	 * @date 2017-1-12
	 * @note begin modify by 涂作权 2017-1-12 原始创建
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
