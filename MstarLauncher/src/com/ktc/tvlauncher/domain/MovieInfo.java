package com.ktc.tvlauncher.domain;

public class MovieInfo {
	private String actors;
	private String description;
	private String director;
	private int id;
	private String linkUrl;
	private String picUrl;
	private String picUrl2;
	private String picUrl3;
	private int showType;
	private String title;
	private String viewPoint;
	private String year;
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPicUrl2() {
		return picUrl2;
	}
	public void setPicUrl2(String picUrl2) {
		this.picUrl2 = picUrl2;
	}
	public String getPicUrl3() {
		return picUrl3;
	}
	public void setPicUrl3(String picUrl3) {
		this.picUrl3 = picUrl3;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getViewPoint() {
		return viewPoint;
	}
	public void setViewPoint(String viewPoint) {
		this.viewPoint = viewPoint;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "RecommendInfoTest [actors=" + actors + ", description="
				+ description + ", director=" + director + ", id=" + id
				+ ", linkUrl=" + linkUrl + ", picUrl=" + picUrl + ", picUrl2="
				+ picUrl2 + ", picUrl3=" + picUrl3 + ", showType=" + showType
				+ ", title=" + title + ", viewPoint=" + viewPoint + ", year="
				+ year + "]";
	}

	
}
