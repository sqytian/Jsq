package cn.com.zhizhangweilai.pachong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.CostTargetUrlDao;
import cn.m1c.gczj.biz.model.CostTargetUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring.xml",
		"classpath*:/spring-mvc.xml",
		"classpath*:/spring-mybatis.xml"})
public class ZhufangZaojixinxihtml {
	
	@Resource
	private CostTargetUrlDao modelDao;
	
	/**
	 * <dependency>
			<groupId>org.jsoup</groupId>
			<artifactId>jsoup</artifactId>
			<version>1.10.3</version>
		</dependency>
	 */
	//网站域名：www.bjjs.gov.cn
	private static String urlPre="http://www.bjjs.gov.cn";
	
	


	/**
	 * 获取造价指数，人工成本 ，造价实例 url
	 * @param args
	 */
	@Test
	public void getUrl() {
		
		//造价信息
		String url = "http://www.bjjs.gov.cn/bjjs/gcjs/zczjxx/zjxx/48843934-4.shtml";
		Document doc;
		try {
			doc = NetUtil.getDoc(url);
			// 获取当前页信息所在标签
			Elements eles = doc.select("a[href]");
			int n=1;
			//获取信息
			for (Element element : eles) {
				CostTargetUrl model = new CostTargetUrl();
				String id =UUIDGenerator.getUUID();
				model.setId(id);
				Date d=new Date(); 
				model.setCreated(new Date(d.getTime() - 4*n *60* 60 * 1000));
				model.setUpdated(new Date(d.getTime() - 4*n *60* 60 * 1000));
				model.setDeleted(false);
				//当前元素为认证信息时，跳过
				if("_blank".equals(element.attr("target"))
						&& element.attr("href").contains(".pdf")){
//					System.out.println(element);
					model.setTypeName("造价信息");
					model.setType(4);
					String href = element.attr("href");
					System.out.println(href);
					model.setUrlData(urlPre+href);
					Node childNode = element.childNode(0);
					System.out.println(childNode);
					model.setDocumentName(childNode.toString());
				}
				modelDao.insert(model);
				n++;
				Thread.sleep(1*1000);
			}
		} catch (Exception e) {
			System.out.println("get message error :"+e);
		}
		doc = null;
	
	}
	
	
	
}
