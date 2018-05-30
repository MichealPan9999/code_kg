package com.ktc.tvlauncher.domain;

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
public class ForecastWeatherInfo {
	private String[] day_temp;//白天温度 ℃
	private String[] night_temp;//晚上温度 ℃
	private String[] day_wind_direct;//白天风向
	private String[] night_wind_direct;//白天风向
	private String[] day_wind_strong;//晚上风力
	private String[] night_wind_strong;//晚上风力
	private String[] day_weather_status;//白天天气现象
	private String[] night_weather_status;//晚上天气现象
	private String[] switch_time;

	



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
	public ForecastWeatherInfo() {
		// TODO Auto-generated constructor stub
	}





	public String[] getDay_temp() {
		return day_temp;
	}





	public void setDay_temp(String[] day_temp) {
		this.day_temp = day_temp;
	}





	public String[] getNight_temp() {
		return night_temp;
	}





	public void setNight_temp(String[] night_temp) {
		this.night_temp = night_temp;
	}





	public String[] getDay_wind_direct() {
		return day_wind_direct;
	}





	public void setDay_wind_direct(String[] day_wind_direct) {
		this.day_wind_direct = day_wind_direct;
	}





	public String[] getNight_wind_direct() {
		return night_wind_direct;
	}





	public void setNight_wind_direct(String[] night_wind_direct) {
		this.night_wind_direct = night_wind_direct;
	}





	public String[] getDay_wind_strong() {
		return day_wind_strong;
	}





	public void setDay_wind_strong(String[] day_wind_strong) {
		this.day_wind_strong = day_wind_strong;
	}





	public String[] getNight_wind_strong() {
		return night_wind_strong;
	}





	public void setNight_wind_strong(String[] night_wind_strong) {
		this.night_wind_strong = night_wind_strong;
	}





	public String[] getDat_weather_status() {
		return day_weather_status;
	}





	public void setDay_weather_status(String[] dat_weather_status) {
		this.day_weather_status = dat_weather_status;
	}





	public String[] getNight_weather_status() {
		return night_weather_status;
	}





	public void setNight_weather_status(String[] night_weather_status) {
		this.night_weather_status = night_weather_status;
	}





	public String[] getSwitch_time() {
		return switch_time;
	}





	public void setSwitch_time(String[] switch_time) {
		this.switch_time = switch_time;
	}

	
	
	
}
