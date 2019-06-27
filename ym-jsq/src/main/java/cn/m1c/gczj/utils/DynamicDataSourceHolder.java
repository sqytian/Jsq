package cn.m1c.gczj.utils;
/**
 * @date 2016年8月15日
 * @description 
 * @author  phil --> E-mail: s@m1c.cn
 * @corp m1c soft Co.,ltd
 */
public class DynamicDataSourceHolder {
	public static final ThreadLocal<String> holder = new ThreadLocal<String>();

	public static void putDataSource(String name) {
		holder.set(name);
	}

	public static String getDataSouce() {
		return holder.get();
	}
}