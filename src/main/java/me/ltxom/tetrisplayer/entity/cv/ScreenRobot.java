package me.ltxom.tetrisplayer.entity.cv;

import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public class ScreenRobot {
	private Robot robot;

	public ScreenRobot(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public Robot getRobot(){
		return robot;
	}
}
