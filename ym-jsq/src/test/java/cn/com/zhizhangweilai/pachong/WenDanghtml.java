package cn.com.zhizhangweilai.pachong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

public class WenDanghtml {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		try {
			File file = new File("H:\\网页文档\\在centos和redhat上安装docker.html");
			String strURL = "http://www.dulifei.com/standard/detail/" + "n";
			int secCont = 1000;
			URL url = new URL(strURL);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection(new Proxy(Type.SOCKS,
							new InetSocketAddress("192.168.67.131", 1082)));
			httpConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			httpConn.setDoOutput(true);
			httpConn.setReadTimeout(100 * secCont);
			int responseCode = httpConn.getResponseCode();
			InputStreamReader input = new InputStreamReader(
					httpConn.getInputStream(), "UTF-8");
			BufferedReader bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			String buf = contentBuf.toString();
			int beginIx = buf.indexOf("<html");
			int endIx = buf.indexOf("/html>");
			String result = buf.substring(beginIx, endIx);

			BufferedWriter bf = new BufferedWriter(new PrintWriter(file));
			bf.append(buf);
			input.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
