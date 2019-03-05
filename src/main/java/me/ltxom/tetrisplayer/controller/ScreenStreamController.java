package me.ltxom.tetrisplayer.controller;

import me.ltxom.tetrisplayer.service.cv.ScreenStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@RestController()
public class ScreenStreamController {
	@Autowired
	private ScreenStreamService screenStreamService;


	@RequestMapping(value = "/capture", method = {RequestMethod.GET})
	public Object capture(){
		try {
			ImageIO.write(screenStreamService.captureScreen(new Rectangle(0,0,775,435)), "png", new File("img/"+System.currentTimeMillis()+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screenStreamService.getRobot().hashCode();
	}


}
