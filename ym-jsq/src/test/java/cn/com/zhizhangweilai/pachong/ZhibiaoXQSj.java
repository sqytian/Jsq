package cn.com.zhizhangweilai.pachong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.TargetDetailDataDao;
import cn.m1c.gczj.biz.model.TargetDetailData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring.xml",
		"classpath*:/spring-mvc.xml",
		"classpath*:/spring-mybatis.xml"})

public class ZhibiaoXQSj {
	
	@Resource
	private TargetDetailDataDao modelDao;

	/**
	 * 造价指标数据
	 * @param args
	 */
	@Test
	public void gettargetDetailData() {
		
		List<String> results = new ArrayList<String>();
//		String url = "http://www.bjjs.gov.cn/eportal/ui?struts.portlet.mode=view&struts.portlet.action=/portlet/zjzbFront!createEChartsByZjzs.action&pageId=413019&moduleId=f7c0f73f24da4911af1c21f0a6a64e8c";
		String url = "http://www.bjjs.gov.cn/eportal/ui?struts.portlet.mode=view&struts.portlet.action=/portlet/zjzbFront!createEChartsByZjzs.action&pageId=413019&moduleId=f7c0f73f24da4911af1c21f0a6a64e8c";	
		Document doc;
		try {
			doc = NetUtil.getDoc(url);
			// 获取当前页信息所在标签
			Elements eles = doc.select("body");
			String text = eles.text();
			JSONArray parseArray = JSON.parseArray(text);
			for (Object object : parseArray) {
				//{"month":3,"year":2016,"price":97.25,"className":"混合结构住宅楼"}
				Map<String, Object> map = (Map<String, Object>)JSON.parse(object.toString());
				TargetDetailData targetDetailData = new TargetDetailData();
				targetDetailData.setId(UUIDGenerator.getUUID());
				targetDetailData.setCreated(new Date());
				targetDetailData.setUpdated(new Date());
				targetDetailData.setDeleted(false);
 				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					switch(key){
						case "month":{
							targetDetailData.setMonthData(Integer.valueOf(value));
							break;
						}
						case "year":{
							targetDetailData.setYearData(Integer.valueOf(value));
							break;
						}
						case "price":{
							targetDetailData.setPriceData(value);
							break;
						}
						case "className":{
							targetDetailData.setProjectName(value);
							break;
						}
					}
					System.out.println(key);
					System.out.println(value);
				}
 				modelDao.insert(targetDetailData);
			}
			
			
		} catch (IOException e) {
			System.out.println("get message error :"+e);
		}
		doc = null;
	
	}
	
	
}
