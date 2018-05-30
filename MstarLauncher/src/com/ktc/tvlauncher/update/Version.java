package com.ktc.tvlauncher.update;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Version {
	
	private String			appName;			//软件名
	private String			fileName;			//文件名
	private String			pkgName;			//包名
	private String			verName;			//版本名
	private int				verCode;			//版本号
	private String			url;				//下载地址
	private String			introduction;		//简介
	private String			md5sum;				//MD5值
	
	public Version(JSONObject jsonObj) {
		try {
			this.appName = jsonObj.getString("app_name");
			this.fileName = jsonObj.getString("file_name");
			this.pkgName = jsonObj.getString("pkg_name");
			this.verName = jsonObj.getString("ver_name");
			this.verCode = jsonObj.getInt("ver_code");
			this.url = jsonObj.getString("url");
			this.introduction = jsonObj.getString("introduction");
			this.md5sum = jsonObj.getString("MD5");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static List<Version> constructVersion(String str) {
		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray array = jsonObj.getJSONArray("version");
			int size = array.length();
			List<Version> versions = new ArrayList<Version>(size);
			for (int i = 0; i < size; i++) {
				versions.add(new Version(array.getJSONObject(i)));
			}
			return versions;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAppName() {
		return this.appName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getPkgName() {
		return this.pkgName;
	}
	
	public String getVerName() {
		return this.verName;
	}
	
	public int getVerCode() {
		return this.verCode;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getIntroduction() {
		return this.introduction;
	}
	
	public String getMD5() {
		return this.md5sum;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Version{"
				+ "appName=" + appName
				+ ", fileName=" + fileName
				+ ", pkgName=" + pkgName
				+ ", verName=" + verName
				+ ", verCode=" + verCode
				+ ", url=" + url
				+ ", introduction=" + introduction
				+ ", md5sum=" + md5sum
				+ '}';
	}
}
