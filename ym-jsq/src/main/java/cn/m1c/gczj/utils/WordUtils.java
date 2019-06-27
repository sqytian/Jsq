package cn.m1c.gczj.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordUtils {
	
	private static Logger logger = LoggerFactory.getLogger(WordUtils.class);
    //配置信息,代码本身写的还是很可读的,就不过多注解了
    private static Configuration configuration = null;// /trd-web-haiguan/src/test/java/com/topsec/trd/web/test.ftl
    //这里注意的是利用WordUtils的类加载器动态获得模板文件的位置  /m1c-gczj/src/main/java/cn/m1c/gczj/utils/WordUtils.java
    ///D:/jisuanqi222/gczjjava/m1c-gczj/target/classes/cn/m1c/gczj/utils/
//    private static final String templateFolder = WordUtils.class.getClassLoader().getResource("cn/m1c/gczj/utils/").getPath();
//    
//    static {
//    	logger.info("oldurl  :"+templateFolder);
//    	String replace = templateFolder.replace("cn/m1c/gczj/utils/", "templates/");
//    	
//    	logger.info("newurl  :"+replace);
//        configuration = new Configuration();
//        configuration.setDefaultEncoding("utf-8");
//        try {
//            configuration.setDirectoryForTemplateLoading(new File(replace));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//   }

    private WordUtils() {
        throw new AssertionError();
    }

    @SuppressWarnings("resource")
	public static void exportMillCertificateWord(HttpServletResponse response,Map<String, Object> map) throws IOException {
    	configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    	configuration.setDirectoryForTemplateLoading(new File("/usr/local/tem/"));
    	
//    	File file2 = ResourceUtils.getFile("classpath:document.ftl");
//    	String replace = file2.getPath().replace("document.ftl", "");
//    	configuration = new Configuration();
//        configuration.setDefaultEncoding("utf-8");
//    	configuration.setDirectoryForTemplateLoading(new File(replace));
    	
    	Template freemarkerTemplate = configuration.getTemplate("document.ftl");
        // 调用工具类的createDoc方法生成Word文档
        File file = createDoc(map,freemarkerTemplate);
        try(ServletOutputStream out = response.getOutputStream();InputStream fin=new FileInputStream(file)) {
            // 设置浏览器以下载的方式处理该文件名
            String fileName = "工程投资二类费报告.doc";
            InputStream ins = new FileInputStream(file);
			BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
			OutputStream outs = response.getOutputStream();// 获取文件输出IO流
			BufferedOutputStream bouts = new BufferedOutputStream(outs);
			response.setContentType("application/x-download");// 设置response内容的类型
			response.setHeader("Content-disposition",
					"attachment;filename=" + URLEncoder.encode(fileName+ "", "UTF-8"));// 设置头部信息
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			// 开始向网络传输文件流
			while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
				bouts.write(buffer, 0, bytesRead);
			}
			bouts.flush();// 这里一定要调用flush()方法
        }catch (Exception e) {
        	e.printStackTrace();
		}
        finally {
//            if(fin != null) fin.close();
//            if(out != null) out.close();
            if(file != null) file.delete(); // 删除临时文件
        }
    }

    private static File createDoc(Map<String,Object> dataMap, Template template) {
        String name =  "testtext.doc";
        File f = new File(name);
        Template t = template;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }
}
