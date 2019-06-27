package cn.m1c.gczj.constants;

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
	/**地址搜索索引库**/
	public static final String INDEX_ADDRESS	= "/data/repository/index/address";

	
	/**文件存储地址**/
	public static final String ROOT 			= "/data/repository/file";
	
	/**文件服务器**/
	//"http://admin.anyfees.com"
	public static String SERVER_PATH 		= pps.getProperty("FileServerPath");
	
	//造价指数文件地址  "/costindex"
	public static String COSTINDEX_PATH 		= pps.getProperty("CostindexPath");
	//人工成本  /laborcost
	public static String LABORCOST_PATH 		= pps.getProperty("LaborcostPath");
	
	//自制案例文件地址  "/self"
	public static String SELF_PATH 		= pps.getProperty("SelfPath");
	
    
	/**用户文件空间**/
	public static final int MAX_SPACE 			= 10000;
	
	/**Token失效时间（天）**/
	public static final int AUTH_TOKEN_EXPIRE 	= 100;//天
	
	
	
}
