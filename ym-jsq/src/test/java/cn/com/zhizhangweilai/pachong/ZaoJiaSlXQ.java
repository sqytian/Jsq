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

public class ZaoJiaSlXQ {

	/**
	 * 造价实例详情
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> results = new ArrayList<String>();
		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413017&guid=61f6c7ac-0232-412c-863a-951197c08555";
		Document doc;
		try {
			doc = NetUtil.getDoc(url);
			
			
			// 获取当前页ICP信息所在标签
//			Elements eles = doc.select("tbody>tr:eq(0)");
//			Elements eles = doc.select("table.maintable>tbody");
			Elements eles = doc.select("//*[@id='c89d1416422d4d82926555e8500d4c09']/div[2]/div/div/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table");
			
			//			Element first = eles.get(1);
//			Elements eles = doc.select("div div:eq(2)");
//			System.out.println(first);
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
		File file = new File("H:\\ccc\\shiuli\\造价实例详情5.html");

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
