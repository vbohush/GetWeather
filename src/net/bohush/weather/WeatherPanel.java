package net.bohush.weather;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.JPanel;

public class WeatherPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int years = 8;
	private int snaps = 1460;
	private int beginYear = 2006;
	private int undefined = -1000;
	
	private double[][] temperatures = new double[years][snaps];
	private int[][] windSpeeds = new int[years][snaps];
	private double[][] precipitations = new double[years][snaps];
	
	public WeatherPanel() throws FileNotFoundException, ParseException {
		Scanner input = new Scanner(new BufferedInputStream(new FileInputStream(new File("tmp/Lviv.csv"))), "UTF-8");
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
		}


	}

	@SuppressWarnings("unused")
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		int lineHeight = 120;
		int top = 170;

		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		
		g.drawString("Ціна поділки: 10°C", lineHeight, 60);
		g.drawString("Температура повітря", 750, 60);
		
		//g.drawString("Ціна поділки: 15 мм", lineHeight, 60);
		//g.drawString("Кількість опадів", 770, 57);
		
		//g.drawString("Ціна поділки: 5 м/с", lineHeight, 60);
		//g.drawString("Швидкість вітру", 770, 60);
		
		g.drawString("© Viktor Bohush, bohush.net", 730, 1062);
		g.drawString("Дані надані WEB-сайтом rp5.ua", 1315, 60);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		g.drawString("Львів", 800, 35);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		
		Color blueColor = new Color(0, 153, 204);
		Color redColor = new Color(255, 48, 48);
		Color violetColor = new Color(153, 102, 204);
		Color greenColor = new Color(102, 255, 0);
		for (int year = 0; year < 8; year++) {
			g.setColor(Color.WHITE);
			
			g.drawString((beginYear + year) + "", 50, top + year * lineHeight + 7);
			g.setColor(Color.DARK_GRAY);
			for (int i = -60; i <= 60; i+= 30) {			
				g.drawLine(lineHeight, top + year * lineHeight + i, lineHeight + temperatures[year].length,  top + year * lineHeight + i);
			}
			
			for (int i = 0; i < temperatures[year].length; i++) {
				if((i > 0)&&(temperatures[year][i] == undefined)) {
					temperatures[year][i] = temperatures[year][i - 1];
				}
				if(temperatures[year][i] <= 0) {
					g.setColor(blueColor);
				} else {
					g.setColor(redColor);
				}
				
				g.drawLine(lineHeight + i, top + year * lineHeight, lineHeight + i,  top + year * lineHeight - (int)(temperatures[year][i] * 3));

				
			}
			/*
			
			for (int i = 1; i < precipitations[year].length; i++) {
			if(precipitations[year][i] == undefined) {
				precipitations[year][i] = 0;
			}
			g.setColor(violetColor);			
			g.drawLine(lineHeight + (i - 1), top + year * lineHeight, lineHeight + (i - 1),  top + year * lineHeight - (int)(precipitations[year][i] * 2));
			g.drawLine(lineHeight + i, top + year * lineHeight, lineHeight + i,  top + year * lineHeight - (int)(precipitations[year][i] * 2));
			
			}
			
			for (int i = 0; i < windSpeeds[year].length; i++) {
				if(windSpeeds[year][i] == undefined) {
					windSpeeds[year][i] = 0;
				}
				g.setColor(greenColor);
				
				g.drawLine(lineHeight + i, top + year * lineHeight, lineHeight + i,  top + year * lineHeight - (int)(windSpeeds[year][i] * 6));

				
			}*/
			
			g.setColor(Color.WHITE);
			g.drawLine(lineHeight, top + year * lineHeight, lineHeight + temperatures[year].length,  top + year * lineHeight);

			
			int[] daysInMonths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};			
			for (int i = 0; i < daysInMonths.length; i++) {
				if(i != 0) {
					daysInMonths[i] = daysInMonths[i] + daysInMonths[i - 1];
				}
				g.fillRect(lineHeight + (daysInMonths[i] * 4), top + year * lineHeight - 10, 3,  20);
				
			}
			
		}
		

	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 600);
	}
}
