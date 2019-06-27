package cn.com.zhizhangweilai.pachong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

import javax.annotation.Resource;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.m1c.gczj.biz.dao.CostTargetUrlDao;
import cn.m1c.gczj.biz.model.CostTargetUrl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring.xml",
		"classpath*:/spring-mvc.xml",
		"classpath*:/spring-mybatis.xml"})
public class ZhibiaoXQhtml {
	
	@Resource
	private CostTargetUrlDao modelDao;

	
	
	
	/**
	 * 造价指标详情页
	 * @param args
	 */
	@Test
	public void getHtml() {
		List<CostTargetUrl> list = modelDao.getListByType(3);
		for (CostTargetUrl costTargetUrl : list) {
			String urlData = costTargetUrl.getUrlData();
			String documentName = costTargetUrl.getDocumentName();
			System.out.println(urlData);
			htmlByul(urlData,documentName);
			
		}
	}
	
	public void htmlByul(String urlData,String documentName){
//		String url = "http://www.bjjs.gov.cn/eportal/ui?pageId=413019&guid=a038e450-1eff-41df-8242-ac8dd5f7fa6d";
		Document doc;
		try {
			doc = NetUtil.getDoc(urlData);
			// 造价指数
//			Elements eles = doc.select("table:eq(1)>tbody");
			//人工详情
			Elements eles = doc.select("div:eq(0)>table");
			gettext(eles,documentName);
		} catch (IOException e) {
			System.out.println("get message error :"+e);
		}
	}
	
	public void gettext(Elements eles,String documentName){
		String fileName="F:\\ccc\\bbb\\"+documentName+".html";
		File file = new File(fileName);

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
