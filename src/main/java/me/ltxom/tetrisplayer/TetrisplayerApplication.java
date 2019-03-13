package me.ltxom.tetrisplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TetrisplayerApplication {
	public static final String COMMAND_PREFIX = "@";
	public static final String COMMAND_SUFFIX = "*";
	public static final String COMMAND_TOP = "1";
	public static final String COMMAND_DOWN = "2";
	public static final String COMMAND_LEFT = "3";
	public static final String COMMAND_RIGHT = "4";
	public static final String COMMAND_A = "a";
	public static final String COMMAND_B = "b";
	public static final String COMMAND_R = "r";


	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(TetrisplayerApplication.class, args);

	}


}
