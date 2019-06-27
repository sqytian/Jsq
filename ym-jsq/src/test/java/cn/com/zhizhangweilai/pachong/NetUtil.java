package cn.com.zhizhangweilai.pachong;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NetUtil {

	// 请求超时时间，30秒
		public static final int TIME_OUT = 3600*3600*1000;
		// 模拟浏览器请求头信息
		public static Map<String,String> headers = new HashMap<String,String>();
		static{
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0");
			headers.put("Accept", "text/html");
			headers.put("Accept-Language", "zh-CN,zh");
			headers.put("Accept-Language", "zh-CN,zh");
		}
		
		//根据url获取html文档
		public static Document getDoc(String url) throws IOException{
			//新建一个连接
			Connection conn = Jsoup.connect(url).timeout(TIME_OUT);
			conn = conn.headers(headers);
			conn =conn.data("currentPage", "1");
			conn =conn.data("pageSize", "30");
//			conn =conn.data("guid", "a038e450-1eff-41df-8242-ac8dd5f7fa6d");
			
			conn = conn.proxy(Proxy.NO_PROXY); //不需要代理
//			conn = conn.proxy(new Proxy(Type.SOCKS,
//					new InetSocketAddress("192.168.67.131", 1082)));
			Document doc = conn.get();
			return doc;
		}
		
		public static void main(String[] args) {
			Date d=new Date();   
			System.out.println(new Date(d.getTime() - 2 * 60 * 60 * 1000));
		}
	
			  
			   
		 
		
}
