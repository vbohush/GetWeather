package net.bohush.weather;

import java.util.Calendar;

public class WeatherSnap {
	private Calendar calendar;
	private double temperature;
	private int windSpeed;
	private double precipitation;
	
	
	public WeatherSnap(Calendar calendar, double temperature, int windSpeed, double precipitation) {
		super();
		this.calendar = calendar;
		this.temperature = temperature;
		this.windSpeed = windSpeed;
		this.precipitation = precipitation;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	public double getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}
	
	@Override
	public String toString() {
		return calendar.getTime() + "\t" + temperature + "\t" + windSpeed + "\t" + precipitation;
	}
}
