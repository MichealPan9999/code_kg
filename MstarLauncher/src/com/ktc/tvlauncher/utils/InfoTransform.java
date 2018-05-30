package com.ktc.tvlauncher.utils;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.ktc.tvlauncher.R;

public class InfoTransform {
	private static InfoTransform transformInstance = null;
	private Context mContext;
	
	public InfoTransform(Context context)
	{
		this.mContext = context;
	}
	
	public static InfoTransform getInstance(Context context)
	{
		if(transformInstance == null)
			transformInstance = new InfoTransform(context);
		return transformInstance;
	}
	
	public String getWeatherStatusTransform(String weatherStatusSign)
	{
		String weatherStatus="";
		String weatherStatusArray[];
		int index = 0;
		int sign = 0;
		weatherStatusArray = mContext.getResources().getStringArray(R.array.str_array_weather_status);
		if(weatherStatusSign != null && weatherStatusSign.length()>0)
		sign = Integer.parseInt(weatherStatusSign);
		if(sign>=0 && sign<=31)
		{
			index = sign;
		} else if(sign == 53)
		{
			index = 32;
		} else
		{
			index = 33;
		}
		if(weatherStatusArray != null && weatherStatusArray.length >0 && index < weatherStatusArray.length)			
		weatherStatus = weatherStatusArray[index];
		return weatherStatus;
	}
	
	
	
	public String getMonthString(int mouth)
	{				
		String[] monthName=mContext.getResources().getStringArray(R.array.str_array_month);
		switch (mouth){
		case Calendar.JANUARY:   return monthName[0];
		case Calendar.FEBRUARY:   return monthName[1];
		case Calendar.MARCH:   return monthName[2];
		case Calendar.APRIL:   return monthName[3];
		case Calendar.MAY:   return monthName[4];
		case Calendar.JUNE:   return monthName[5];
		case Calendar.JULY:   return monthName[6];
		case Calendar.AUGUST:   return monthName[7];
		case Calendar.SEPTEMBER:   return monthName[8];
		case Calendar.OCTOBER:   return monthName[9];
		case Calendar.NOVEMBER:   return monthName[10];
		case Calendar.DECEMBER:   return monthName[11];
		default:
			return null;
		}
	}

	public String getWeekString(int week){
		String[] weekName=mContext.getResources().getStringArray(R.array.str_array_week);
		switch (week){
		case Calendar.SUNDAY:   return weekName[0];
		case Calendar.MONDAY:   return weekName[1];
		case Calendar.TUESDAY:   return weekName[2];
		case Calendar.WEDNESDAY:   return weekName[3];
		case Calendar.THURSDAY:   return weekName[4];
		case Calendar.FRIDAY:   return weekName[5];
		case Calendar.SATURDAY:   return weekName[6];
		default:
			return null;
		}	
	}
	
	public int getDay_startHour(String switchTime)
	{
		int startHour = 0;
		String[] timeArray = switchTime.split("\\|");
		if(timeArray.length > 0)
		{
			String[] startTime = timeArray[0].split(":");
			if(startTime.length>0)
				startHour = Integer.parseInt(startTime[0]);

		}
		return startHour;
	}
	public int getDay_startMinute(String switchTime)
	{
		int startMinute = 0;
		String[] timeArray = switchTime.split("\\|");
		if(timeArray.length > 0)
		{
			String[] startTime = timeArray[0].split(":");
			if(startTime.length>1)				
				startMinute = Integer.parseInt(startTime[1]);
		}
		return startMinute;
	}
	public int getDay_endHour(String switchTime)
	{
		int endHour = 0;
		String[] timeArray = switchTime.split("\\|");
		if(timeArray.length > 1)
		{
			String[] startTime = timeArray[1].split(":");
			if(startTime.length>0)
				endHour = Integer.parseInt(startTime[0]);

		}
		return endHour;
	}
	public int getDay_endMinute(String switchTime)
	{
		int endMinute = 0;
		String[] timeArray = switchTime.split("\\|");
		if(timeArray.length > 1)
		{
			String[] startTime = timeArray[1].split(":");
			if(startTime.length>1)				
				endMinute = Integer.parseInt(startTime[1]);
		}
		return endMinute;
	}
}
