package net.bohush.weather;


import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.text.ParseException;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class Main extends JApplet {

	private static final long serialVersionUID = 1L;
	private WeatherPanel weatherPanel;
	
	public Main() throws FileNotFoundException, ParseException {
		setLayout(new BorderLayout());
		weatherPanel = new WeatherPanel();
		add(weatherPanel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		JFrame frame = new JFrame("");
		Main main = new Main();
		frame.add(main);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(1920, 1080);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

}
