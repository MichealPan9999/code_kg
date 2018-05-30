package com.ktc.launcher.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

	/**
	 * 浠�*.prop涓牴鎹甼ey鑾峰彇瀵瑰簲鐨勫��
	 * 
	 * @param filePath *.prop鏂囦欢璺緞
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
