package com.ktc.launcher.bean;

public class AppItem {
	private String position="";
	private String packageName="";
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	@Override
	public String toString() {
		return "AppItem [position=" + position + ", packageName=" + packageName + "]";
	}
	public AppItem(String position, String packageName) {
		super();
		this.position = position;
		this.packageName = packageName;
	}
	public AppItem() {
		// TODO Auto-generated constructor stub
	}
}
