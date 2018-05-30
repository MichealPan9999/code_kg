package com.ktc.tvlauncher.ui;

/**
 * 
 * @ClassName: WeatherInfo
 * 
 * @Description: WeatherInfo对象类,描述某天对应的天气信息
 * 
 * @author Nathan.Liao
 * 
 * @date 2013-3-18 下午3:43:10
 */
public class WeatherInfo {
	private String city;//城市中文名
	private String city_en;//城市拼音
	private String city_id;//城市ID
	private String date_y;//当前日期
	private String week;// 星期
	private String index_d;//天气建议信息
	private String[] temp_C;//6天温度 ℃
	private String[] weather;//6天天气情况描述
	private String[] wind;//6天风力
	private String[] weatherImage;

	



	/**
	 * 
	 * 
	 * @Title: WeatherInfo
	 * 
	 * @Description: WeatherInfo构造函数
	 * 
	 * @param
	 * 
	 * @throws
	 */
	public WeatherInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity_en() {
		return city_en;
	}

	public void setCity_en(String city_en) {
		this.city_en = city_en;
	}

	public String getDate_y() {
		return date_y;
	}

	public void setDate_y(String date_y) {
		this.date_y = date_y;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String[] getTemp_C() {
		return temp_C;
	}

	public void setTemp_C(String[] temp_C) {
		this.temp_C = temp_C;
	}

	public String[] getWeather() {
		return weather;
	}

	public void setWeather(String[] weather) {
		this.weather = weather;
	}

	public String[] getWind() {
		return wind;
	}

	public void setWind(String[] wind) {
		this.wind = wind;
	}
	
	public String[] getWeatherImage() {
		return weatherImage;
	}

	public void setWeatherImage(String[] weatherImage) {
		this.weatherImage = weatherImage;
	}
	
	

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	
	
	public String getIndex_d() {
		return index_d;
	}

	public void setIndex_d(String index_d) {
		this.index_d = index_d;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.city+this.city_en+this.date_y;
	}

	
	
}
