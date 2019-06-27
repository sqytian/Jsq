package cn.m1c.gczj.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final Log log = LogFactory.getLog(FileUtils.class);


    public static void createDirectory(File file) throws IOException {
        if (!file.exists()) {
            synchronized (FileUtils.class) {
                file.mkdirs();
            }
        }
        if (!file.isDirectory()) {
            throw new IOException(file.getName() + " must be a directory!");
        }
    }

    /**
     * 获得文件类型
     *
     * @param fileName
     * @return
     */
    public static String getfileType(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return fileType;
    }



    /**
     * 根据路径获得文件名称
     *
     * @return 新文件名
     */
    public static String getFileName(String filePath) {
        String result = filePath;
        int lastIndexOf = filePath.lastIndexOf("/");
        if (lastIndexOf > -1) {
            result = filePath.substring(lastIndexOf + 1);
        }
        return result;
    }

    /**
     * 复制流
     *
     * @param input  输入流
     * @param output 输出流
     * @return 流大小
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 0);
    }

    /**
     * 复制流 同时 加/解密
     *
     * @param input      输入流
     * @param output     输出流
     * @param encypeType 加/解密   0不做处理,1加密,-1解密
     * @return 流大小
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output, int encypeType) throws IOException {
        int DEFAULT_BUFFER_SIZE = encypeType == -1 ? 4104 : 4096;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            byte[] tempByte = null;
            if (encypeType == 1) {//加密
//				tempByte=EncypeUtil.desEncrypt(buffer);
            } else if (encypeType == -1) {//解密
//				tempByte=EncypeUtil.deCrypt(buffer);
            } else if (encypeType == 0) {//不做处理
                tempByte = buffer;
            }
//			System.out.println("n:"+n+"@@@@@@"+tempByte.length);
            output.write(tempByte, 0, tempByte.length);
            count += n;
        }
        return count;
    }

    /**
     * 复制文件
     *
     * @param modlePath   模板文件绝对路径（包括名字：如E：）
     * @param newFilePath 复制的新文件保存绝对路径（包括名字）
     * @param encypeType  加/解密   0不做处理,1加密,-1解密
     * @throws IOException
     */
    public static void copyFile(String modlePath, String newFilePath, int encypeType) {
        // file读到流中
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            try {
                inStream = new FileInputStream(modlePath);
                fs = new FileOutputStream(newFilePath);
                copy(inStream, fs, encypeType);
                fs.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("查找模板文件" + modlePath + "失败！", e);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("复制文件：" + newFilePath + "失败！", e);
        }
    }

    /**
     * 复制文件
     *
     * @param modlePath   模板文件绝对路径（包括名字：如E：）
     * @param newFilePath 复制的新文件保存绝对路径（包括名字）
     * @throws IOException
     */
    public static void copyFile(String modlePath, String newFilePath) {
        // file读到流中
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            try {
                inStream = new FileInputStream(modlePath);
                fs = new FileOutputStream(newFilePath);
                copy(inStream, fs);
                fs.flush();
            } catch (FileNotFoundException e) {
                log.error("查找模板文件" + modlePath + "失败！", e);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            }
        } catch (IOException e) {
            log.error("复制文件：" + newFilePath + "失败！", e);
        }
    }

    /**
	 * 文件名生成策略：日期+随机数
	 * 
	 * @param suffix
	 *            文件后缀
	 * @return
	 */
	public static String fileNameGenerator(String originalFilename) {
		Calendar now = new GregorianCalendar();
		now.add(Calendar.MONTH, 1);
		String result = "/" + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH)
				+ now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) + now.get(Calendar.SECOND)
				+ now.get(Calendar.MILLISECOND)+"_"+originalFilename;
		return result;
	}
    
    /**
     * 保持文件到服务器
     *
     * @param input       输入流
     * @param newFilePath 文件保存绝对路径（包括名字）
     * @throws IOException
     */
    public static void saveFile(InputStream input, String newFilePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(input, 2 * 1024);
            os = new FileOutputStream(newFilePath);

            IOUtils.copy(is, os);
            os.close();
            is.close();
        } catch (Exception e) {
            log.error("保存文件时出错！" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                    os = null;
                } catch (Exception e1) {
                    log.error("关闭输出流出错：" + e1.getMessage());
                    e1.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e1) {
                    log.error("关闭输入流出错：" + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        try {
            File file = new File(filePath);
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    public static boolean isExist(String filePath) {
        createFilepathFolder(filePath);
        File fp = new File(filePath);
        if (!fp.exists()) {
            return true; // 文件不存在，执行下载功能
        } else {
            return false; // 文件存在不做处理
        }
    }

    public static boolean createFilepathFolder(String filePath) {
        boolean result = true;
        String paths[] = filePath.split("/");
        String dir = paths[0];
        for (int i = 0; i < paths.length - 2; i++) {//注意此处循环的长度
            try {
                dir = dir + "/" + paths[i + 1];
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                    // log.debug("创建目录为："+ dir);
                }
            } catch (Exception err) {
                result = false;
                log.error("ELS - Chart : 文件夹创建发生异常");
            }
        }
        return result;
    }


    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得系统部署的根路径
     *
     * @return 系统绝对路径
     */
    public static String getWebRoot() {
        String rootUrl = "";
        try {
            rootUrl = FileUtils.class.getClassLoader().getResource("").getPath();
            int posWebInf = rootUrl.indexOf("/WEB-INF");
            if (posWebInf > -1) {
                rootUrl = rootUrl.substring(0, posWebInf);
            }
            int posTarget = rootUrl.indexOf("/target");
            if (posTarget > -1) {
                rootUrl = rootUrl.substring(0, posTarget);
                rootUrl += "/src/main/webapp";
            }
        } catch (Exception e) {
            String path = FileUtils.class.getClassLoader().getResource(".").getPath();
            rootUrl = path.replace("/WEB-INF/classes/", "");
        }
        if (isWindow()) {
            rootUrl = rootUrl.substring(1);
        }
        return rootUrl;
    }

    public static boolean isWindow() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static String escape(String s) {
        if (s != null) {
            StringBuffer sbuf = new StringBuffer();
            int len = s.length();
            for (int i = 0; i < len; i++) {
                int ch = s.charAt(i);
                if ('A' <= ch && ch <= 'Z') {
                    sbuf.append((char) ch);
                } else if ('a' <= ch && ch <= 'z') {
                    sbuf.append((char) ch);
                } else if ('0' <= ch && ch <= '9') {
                    sbuf.append((char) ch);
                } else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
                        || ch == '~' || ch == '*' || ch == '\'' || ch == '('
                        || ch == ')') {
                    sbuf.append((char) ch);
                } else if (ch <= 0x007F) {
                    sbuf.append('%');
                    sbuf.append(hex[ch]);
                } else {
                    sbuf.append('%');
                    sbuf.append('u');
                    sbuf.append(hex[(ch >>> 8)]);
                    sbuf.append(hex[(0x00FF & ch)]);
                }
            }
            return sbuf.toString();
        }
        return null;
    }

    private final static String[] hex = {"00", "01", "02", "03", "04", "05",
            "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
            "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
            "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
            "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
            "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
            "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
            "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
            "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
            "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
            "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
            "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
            "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
            "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
            "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
            "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
            "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
            "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
            "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
            "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
            "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"};

    /**
     * 计算数字
     * 例如，输入20.16，输出20.2； 输入20.13，输出20.1；
     *
     * @param filesize
     * @return
     */
    public static String filesize(double filesize) {

        double size = 0.0;
        DecimalFormat df3 = new DecimalFormat("0.0");

        size = filesize / (1024.0 * 1024.0);
        String c = df3.format(size);
        if ("0.0".equals(c)) {
            size = filesize / (1024);
            c = df3.format(size);
            return c + "K";
        } else {
            return c + "M";
        }


    }

    /**
     * 保持文件到服务器
     *
     * @param input       输入流
     * @param newFilePath 文件保存绝对路径（包括名字）
     * @throws IOException
     */
    public static void saveFile(InputStream input, String newFilePath, byte[] b) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(input, 16 * 1024);
            os = new FileOutputStream(newFilePath);
            os.write(b);
            IOUtils.copy(is, os);
            os.close();
            is.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                    os = null;
                } catch (Exception e1) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e1) {
                }
            }
        }

    }


}
