package cn.com.zhizhangweilai.pachong;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * @author sqy
 *
 */
public class UpFile {
	
	private static Logger logger = Logger.getLogger(UpFile.class);
	private static MultipartFile file;
	
//	public static void main(String[] args) {
//		
//		//文件在服务器的路径
//		String path = "/root/probex-home/file/datas/script";
//		
//		InputStream inputStream = null;
//        try {
//            inputStream = file.getInputStream();
//        } catch (IOException e) {
//            logger.error("上传脚本保存失败！", e);
//        }
//
//        File outFile = new File(path);
//        if (!outFile.exists() && !outFile.isDirectory()) {
//            outFile.mkdirs();// 如果目录不存在就创建
//        }
//
//        String filePath = path + "/" + fileNameGenerator("zip");
//        if (filePath != null) {
//            FileUtils.saveFile(inputStream, filePath);
//        }
//	}

	
	/**
	 * 文件名生成策略：日期+随机数
	 * 
	 * @param suffix
	 *            文件后缀
	 * @return
	 */
	public static String fileNameGenerator(String suffix) {
		Calendar now = new GregorianCalendar();
		now.add(Calendar.MONTH, 1);
		String result = "" + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH)
				+ now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) + now.get(Calendar.SECOND)
				+ now.get(Calendar.MILLISECOND) + "."
				+ suffix;
		return result;
	}
	
	public static void main(String[] args) {
		Calendar now = new GregorianCalendar();
		now.add(Calendar.MONTH, 1);
		String result = "" + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH)
		+ now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) + now.get(Calendar.SECOND)
		+ now.get(Calendar.MILLISECOND) + "."
		+ "zip";
		System.out.println(result);
	}
}
