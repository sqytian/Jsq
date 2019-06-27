package cn.m1c.gczj.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public  class Constants {
	static Properties pps = new Properties();
	static {
		try {//需要挪到配置文件中的文件均可以在配置文件中配置
			String path = Constants.class.getClassLoader().getResource("").getPath();
			pps.load(new FileInputStream(path+"config.properties"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**造价咨询项**/
	public static String COST_CONSULTATION = pps.getProperty("CostConsultation");
	
	
	

	
	
	
	
}
