package cn.com.zhizhangweilai.pachong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Zhibiaohtml {
	

	// 请求超时时间，30秒
	public static final int TIME_OUT = 30*1000;
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
		
		conn = conn.proxy(new Proxy(Type.SOCKS,
				new InetSocketAddress("192.168.67.131", 1082)));
		Document doc = conn.get();
		
		return doc;
	}

	/**
	 * 获取造价指数 详情图数据
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> results = new ArrayList<String>();
//		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413013";
		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413019&guid=a038e450-1eff-41df-8242-ac8dd5f7fa6d";
		Document doc;
		try {
			doc = getDoc(url);
			
			// 获取当前页url信息所在标签
			Elements eles = doc.select("tr:gt(1)>td");
			//获取ICP信息
			for (Element element : eles) {
				//当前元素为认证信息时，跳过
				if(element.attr("style").contains("border-color: Black; border-width: 1px; border-style: solid;")){
//					System.out.println(element);
					String text = element.text();
					System.out.println(text);
//					String href = element.attr("href");
//					System.out.println(href);
//					Node childNode = element.childNode(0);
//					System.out.println(childNode);
				}
			}
		} catch (IOException e) {
			System.out.println("get message error :"+e);
		}
		doc = null;
	
	}
	
	
	
}
