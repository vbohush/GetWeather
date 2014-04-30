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
			GregorianCalendar calendar = new GregorianCalendar();
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
		
		
		int years = 8;
		int snaps = 1460;
		int beginYear = 2006;
		int undefined = -1000;
		
		double[][] temperatures = new double[years][snaps];
		int[][] windSpeeds = new int[years][snaps];
		double[][] precipitations = new double[years][snaps];
		for (int i = 0; i < years; i++) {
			for (int j = 0; j < snaps; j++) {
				temperatures[i][j] = undefined;
				windSpeeds[i][j] = undefined;
				precipitations[i][j] = undefined;
			}
		}
		
		for (WeatherSnap weatherSnap : list) {
			int i = weatherSnap.getCalendar().get(Calendar.YEAR) - beginYear;
			int j = (weatherSnap.getCalendar().get(Calendar.DAY_OF_YEAR) - 1) * 4;
			switch (weatherSnap.getCalendar().get(Calendar.HOUR_OF_DAY)) {
			case 2: j += 0; break;
			case 8: j += 1; break;
			case 14: j += 2; break;
			case 20: j += 3; break;
			}
			if(weatherSnap.getCalendar().isLeapYear(weatherSnap.getCalendar().get(Calendar.YEAR))) {
				if(weatherSnap.getCalendar().get(Calendar.DAY_OF_YEAR) > 60) {
						j -= 4;
				}
			}
			temperatures[i][j] = weatherSnap.getTemperature();
			windSpeeds[i][j] = weatherSnap.getWindSpeed();
			precipitations[i][j] = weatherSnap.getPrecipitation();
			/*System.out.println(weatherSnap);
			System.out.println(i + "\t" + j + "\t" + temperatures[i][j] + "\t" + windSpeeds[i][j] + "\t" + precipitations[i][j]);*/
		}
		
		for (int i = 0; i < years; i++) {
			for (int j = 0; j < snaps; j++) {
				System.out.println((beginYear + i) + "\t" + (j / 4) + "\t" + temperatures[i][j] + "\t" + windSpeeds[i][j] + "\t" + precipitations[i][j]);
			}
		}
		System.out.println(list.size());
	}

}
