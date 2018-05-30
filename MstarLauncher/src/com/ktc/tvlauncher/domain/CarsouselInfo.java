package com.ktc.tvlauncher.domain;

public class CarsouselInfo {
	private int id;
	private String linkUrl;
	private String name;
	private String picUrl;
	private int program_id;
	private String showType;
	private int totalsize;
	private String videoUrl;
	private String viewPoint;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getProgram_id() {
		return program_id;
	}
	public void setProgram_id(int program_id) {
		this.program_id = program_id;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public int getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(int totalsize) {
		this.totalsize = totalsize;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getViewPoint() {
		return viewPoint;
	}
	public void setViewPoint(String viewPoint) {
		this.viewPoint = viewPoint;
	}
	@Override
	public String toString() {
		return "CarsouselInfo [id=" + id + ", linkUrl=" + linkUrl + ", name="
				+ name + ", picUrl=" + picUrl + ", program_id=" + program_id
				+ ", showType=" + showType + ", totalsize=" + totalsize
				+ ", videoUrl=" + videoUrl + ", viewPoint=" + viewPoint + "]";
	}
	
}
