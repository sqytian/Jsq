package cn.m1c.gczj.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ComputeUtils {

	/**
	 * 根据公式和价格计算
	 * @param rate  公式
	 * @param price 价格
	 * @return
	 */
	public static BigDecimal getPrice(String rate,BigDecimal price){
	     Binding binding = new Binding();
	     binding.setVariable("x", price);
	     GroovyShell shell = new GroovyShell(binding);
	     String string = shell.evaluate(rate).toString();
	     return  new BigDecimal(string);
	}
	
	
	/**
	 * 判断字符串是否是正浮点数
	 * @param str
	 * @return
	 */
	public static Boolean regEX(String str) {
		String regEx =  "^(([0-9]\\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\\.[0-9]*)|([1-9]*[1-9][0-9]*))$";//正浮点数
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
}
