package net.bohush.weather;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {		
		Scanner input = new Scanner(new BufferedInputStream(new FileInputStream(new File("tmp/33288.01.01.2005.31.12.2013.1.0.0.ua.utf8.00000000.csv"))), "UTF-8");
		ArrayList<WeatherSnap> list = new ArrayList<>();
		while (input.hasNextLine()) {
			String str = input.nextLine();
			String[] strs = str.split("\";\"");
			
			strs[0] = strs[0].substring(1);
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			Date date = df.parse(strs[0]);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 0) {
				calendar.add(Calendar.HOUR_OF_DAY, -1);
			}
			if((calendar.get(Calendar.HOUR_OF_DAY) == 2) ||	(calendar.get(Calendar.HOUR_OF_DAY) == 8) ||(calendar.get(Calendar.HOUR_OF_DAY) == 14) ||(calendar.get(Calendar.HOUR_OF_DAY) == 20)) {				
				try {
					double temperature = Double.parseDouble(strs[1]);
					int windSpeed = Integer.parseInt(strs[7]);
					double precipitation = 0;
					if(strs[24].equals("12")) {
						try {
							precipitation = Double.parseDouble(strs[23]);	
						} catch (NumberFormatException e) {
							precipitation = 0.1;
						}
					}
					list.add(new WeatherSnap(calendar, temperature, windSpeed, precipitation));
				} catch (NumberFormatException e) {
				}
			}
		}
		input.close();
		for (WeatherSnap weatherSnap : list) {
			System.out.println(weatherSnap);
		}
		System.out.println(list.size());
	}

}
