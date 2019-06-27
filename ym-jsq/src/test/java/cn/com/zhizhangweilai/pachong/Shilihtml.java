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

public class Shilihtml {
	


	/**
	 * 获取造价实例表单内容
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> results = new ArrayList<String>();
//		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413013";
		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413010";
		Document doc;
		try {
			doc = NetUtil.getDoc(url);
			
			// 获取当前页url信息所在标签
			Elements eles = doc.select("tr.gvRow>td,tr.gvAlter>td");
			//获取ICP信息
			for (Element element : eles) {
				//当前元素为认证信息时，跳过
				if(true){
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
