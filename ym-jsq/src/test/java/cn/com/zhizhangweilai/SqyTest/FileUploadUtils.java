package cn.com.zhizhangweilai.SqyTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;













//import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.expression.ParseException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.SelfFileDao;
import cn.m1c.gczj.biz.model.SelfFile;
import cn.m1c.gczj.biz.service.SelfFileService;
import cn.m1c.gczj.utils.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring.xml",
		"classpath*:/spring-mvc.xml",
		"classpath*:/spring-mybatis.xml"})
public class FileUploadUtils {

	public static final String PROP_NAME_CMP_HOME = "CMP_HOME";
	  
	@Resource
	private SelfFileDao modelDao;
	
	@Resource
	private SelfFileService modelservice;

	@Test
	public void modify(){
		String type="laborcost";
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
		Map<Integer, List<SelfFile>> map = modelservice.getListByType(200, 1, type, null, null);
		Integer totalCount = map.keySet().iterator().next();
		List<SelfFile> list = map.get(totalCount);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				SelfFile model = list.get(i);
				String documentName = model.getDocumentName();
				
				System.out.println(documentName);
				
				
				String nian = documentName.substring(documentName.indexOf("年")+1, documentName.indexOf("月"));
//				System.out.println("nian: "+nian);
				if(nian.length()==1){
					nian="0"+nian;
				}
				
				
				String yue = documentName.substring(0, 4);
//				System.out.println("年:"+yue);
				Integer orderN = Integer.valueOf(yue+nian);
				String strDate =yue+"年"+nian+"月"+nian+"日";
				System.out.println(strDate);
				  try {
			            Date date=simpleDateFormat.parse(strDate);
			            System.out.println(date);
			            model.setUpdated(date);
			            modelservice.updateByPrimaryKeySelective(model);
			        } catch(Exception px) {
			            px.printStackTrace();
			        }
			}
		}
	
	}
	
	
	
	public void getfile(){
//		String path = "H:\\计算器资料\\网页版";
//		getFilepath(path);
		
		//造价指数
//		String path ="H:\\ccc\\aaa";
		//人工成本
//		String path ="H:\\ccc\\ceshi";
//		getCostFilePath(path);
		//添加utf-8
//		String path ="H:\\ccc\\chengben";
		String path ="H:\\ccc\\shili";
		getCostFilePath(path);
//		getUtfFilePath(path);
		
	}
	
	/**
	 * 上传关连达文件 
	 * @param path
	 */
	public void getUtfFilePath(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
//						System.out.println("文件夹:" + file2.getAbsolutePath());
//						System.out.println(file2.getName());
						
						getFilepath(file2.getAbsolutePath());
					} else {
//						System.out.println("文件:" + file2.getAbsolutePath());
//							System.out.println("首页"+file2.getAbsolutePath());
						String substring = file2.getAbsolutePath();
						System.out.println(substring);
						String[] split = substring.split("\\\\");
						System.out.println("文件名: "+split[3]);
						System.out.println("文件： "+split[3].substring(0, split[3].length()-5));
						System.out.println("H:\\ccc\\shilinew\\"+split[3]);
						
						File oldfile = new File(substring);
						File newfile = new File("H:\\ccc\\shilinew\\"+split[3]);
						try {
							BufferedReader reader = new BufferedReader(new FileReader(oldfile));
							String tempString = null;
							int line = 1;
							// 一次读入一行，直到读入null为文件结束
							List<String> list = new ArrayList<String>();
							while ((tempString = reader.readLine()) != null) {
							    // 显示行号
//							    System.out.println("line " + line + ": " + tempString);
//							    line++;
								if(tempString.contains("ID_ucZjslShow_Image1")){
									tempString="<!--替换--> ";
								}
								if(tempString.contains("LinkButton2")){
									int x = tempString.indexOf("<span");
									int y = tempString.indexOf("</span>");
									tempString = tempString.substring(0, x)+tempString.substring(y, tempString.length());
									
								}
							    list.add(tempString);
							}
							reader.close();
							
							BufferedWriter bf = new BufferedWriter(new PrintWriter(newfile));
							bf.append("<head><meta charset=\"utf-8\"/></head>");
							bf.append("\r");
							for (String string : list) {
								bf.append(string);
								bf.append("\r");
							}
							bf.close();
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						

//						
//						for (String string : split) {
////								System.out.println("层级  "+string);
//						}
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}
	
	/**
	 * 上传指数，人工成本文件 
	 * @param path
	 */
	public void getCostFilePath(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
//						System.out.println("文件夹:" + file2.getAbsolutePath());
//						System.out.println(file2.getName());
						
						getFilepath(file2.getAbsolutePath());
					} else {
//						System.out.println("文件:" + file2.getAbsolutePath());
//							System.out.println("首页"+file2.getAbsolutePath());
							String substring = file2.getAbsolutePath();
//							System.out.println(substring);
							String[] split = substring.split("\\\\");
							System.out.println("文件名: "+split[3]);
							System.out.println("文件： "+split[3].substring(0, split[3].length()-5));
							SelfFile model = new SelfFile();
							model.setId(UUIDGenerator.getUUID());
							//造价指数
//							model.setTypeName("costindex");
							//人工成本
//							model.setTypeName("laborcost");
							//造价实例
							model.setTypeName("example");
							model.setDocumentName(split[3].substring(0, split[3].length()-5));
							model.setUrlType("html");
//							model.setReservedFirst(split[1]);
							model.setFileUrl("http://d.anyfees.com/example/"+split[3]);
							model.setDeleted(false);
							model.setUpdated(new Date());
							model.setCreated(new Date());
							modelDao.insertSelective(model);
							
							for (String string : split) {
//								System.out.println("层级  "+string);
							}
						}
					}
				}
		} else {
			System.out.println("文件不存在!");
		}
	}
	
	/**
	 * 上传广联达文件 
	 * @param path
	 */
	public void getFilepath(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
//						System.out.println("文件夹:" + file2.getAbsolutePath());
//						System.out.println(file2.getName());
						
						getFilepath(file2.getAbsolutePath());
					} else {
//						System.out.println("文件:" + file2.getAbsolutePath());
						if(file2.getAbsolutePath().endsWith("index.html")){
							System.out.println("首页"+file2.getAbsolutePath());
							int indexOf = file2.getAbsolutePath().indexOf("网页版");
							String substring = file2.getAbsolutePath().substring(indexOf, file2.getAbsolutePath().length());
							System.out.println(substring);
							String[] split = substring.split("\\\\");
							SelfFile model = new SelfFile();
							model.setId(UUIDGenerator.getUUID());
							model.setTypeName("quota");
							model.setDocumentName(split[2]);
							model.setUrlType("html");
							model.setReservedFirst(split[1]);
							model.setFileUrl("http://d.anyfees.com/quota/"+split[1]+"/"+split[2]+"/"+split[3]);
							model.setDeleted(false);
							model.setUpdated(new Date());
							model.setCreated(new Date());
//							modelDao.insertSelective(model);
							
							for (String string : split) {
								System.out.println(string);
							}
						}
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}
	  
}
