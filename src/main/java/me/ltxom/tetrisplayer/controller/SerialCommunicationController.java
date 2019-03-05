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
	public Object init(){
		serialCommunicationService.init();
		return "started";
	}

	@RequestMapping(value = "/pause", method = {RequestMethod.GET})
	public Object pause(){
		serialCommunicationService.pauseLogging();
		return "paused";
	}

	@RequestMapping(value = "/resume", method = {RequestMethod.GET})
	public Object resume(){
		serialCommunicationService.resumeLogging();
		return "resumed";
	}

	@RequestMapping(value = "/write", method = {RequestMethod.GET})
	public Object write(@RequestParam("str") String str){
		serialCommunicationService.writeString(str);
		return str;
	}
}
