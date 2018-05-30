package cn.ktc.android.oobe.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	/**
	 * IP正则表达式的判断
	 * @param ip
	 * @return
	 */
    public static boolean matchIP(String ip) {
        // ip正则表达式
        String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
    
	/**
	 * 解析IP地址
	 * @param ip
	 * @return
	 */
    public static String[] resolutionIP(String ip) {
        return ip.split("\\.");
    }

    /**
	 * 从*.prop中根据key获取对应的值
	 * 
	 * @param filePath *.prop文件路径
	 * @param key
	 * @return
	 */
	public static String readPropValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
