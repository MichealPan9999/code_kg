package com.ktc.launcher.bean;

public class InputSourceItem {
	private String sourceName;
	private int poistion;
	private int typeFlag;
	private boolean signalFlag;
	public InputSourceItem() {
		// TODO Auto-generated constructor stub
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getPoistion() {
		return poistion;
	}
	public void setPoistion(int poistion) {
		this.poistion = poistion;
	}
	public int getTypeFlag() {
		return typeFlag;
	}
	public void setTypeFlag(int typeFlag) {
		this.typeFlag = typeFlag;
	}
	public boolean isSignalFlag() {
		return signalFlag;
	}
	public void setSignalFlag(boolean signalFlag) {
		this.signalFlag = signalFlag;
	}
	@Override
	public String toString() {
		return "InputSourceItem [sourceName=" + sourceName + ", poistion=" + poistion + ", typeFlag=" + typeFlag
				+ ", signalFlag=" + signalFlag + "]";
	}
	public InputSourceItem(String sourceName, int poistion, int typeFlag, boolean signalFlag) {
		super();
		this.sourceName = sourceName;
		this.poistion = poistion;
		this.typeFlag = typeFlag;
		this.signalFlag = signalFlag;
	}
	
}
