package me.ltxom.tetrisplayer.controller;

import me.ltxom.tetrisplayer.service.serial.SerialCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SerialCommunicationController {
	@Autowired
	private SerialCommunicationService serialCommunicationService;

	@RequestMapping(value = "/init", method = {RequestMethod.GET})
	public Object init() {
		serialCommunicationService.init();
		return "started";
	}

	@RequestMapping(value = "/pause", method = {RequestMethod.GET})
	public Object pause() {
		serialCommunicationService.pauseLogging();
		return "paused";
	}

	@RequestMapping(value = "/resume", method = {RequestMethod.GET})
	public Object resume() {
		serialCommunicationService.resumeLogging();
		return "resumed";
	}

	@RequestMapping(value = "/write", method = {RequestMethod.GET})
	public Object write(@RequestParam("str") String str) {
		serialCommunicationService.writeString(str);
		return str;
	}

	@RequestMapping(value = "/press", method = {RequestMethod.GET})
	public Object press(@RequestParam("str") String str) {
		str = str.toLowerCase();
		str = str.equals("a") ? " " : str;
		str = str.equals("b") ? "c" : str;
		str = str.equals("x") ? "f" : str;
		str = str.equals("y") ? "r" : str;
		str = str.equals("up") ? "f-1" : str;
		str = str.equals("down") ? "2" : str;
		str = str.equals("left") ? "1" : str;
		str = str.equals("right") ? "3" : str;
		str = str.equals("l") ? "q" : str;
		str = str.equals("r") ? "e" : str;

		str = "@" + str + "*";
		serialCommunicationService.writeString(str);
		return str;
	}
}
