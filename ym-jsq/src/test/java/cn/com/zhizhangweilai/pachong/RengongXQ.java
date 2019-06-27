package cn.com.zhizhangweilai.pachong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class RengongXQ {

	/**
	 * 人工成本详情
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> results = new ArrayList<String>();
		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413021&guid=17f005e4-0a64-4ba9-b6fc-8d2410d09fd7";
		Document doc;
		try {
			doc = NetUtil.getDoc(url);
			
			
			// 获取当前页ICP信息所在标签
			Elements eles = doc.select("div:eq(0)>table");
			gettext(eles);
//			Iterator<Element> iterator = eles.iterator();
//			while(iterator.hasNext()){
//				Element next = iterator.next();
//				break;
//			}
			
			
			
		} catch (IOException e) {
			System.out.println("get message error :"+e);
		}
		doc = null;
	
	}
	
	public static void gettext(Elements eles){
		File file = new File("H:\\ccc\\人工成本详情.html");

		try {
			BufferedWriter bf = new BufferedWriter(new PrintWriter(file));
			bf.append(eles.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
