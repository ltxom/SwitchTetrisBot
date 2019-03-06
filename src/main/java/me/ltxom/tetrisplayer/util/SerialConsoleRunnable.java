package me.ltxom.tetrisplayer.util;

import me.ltxom.tetrisplayer.entity.serial.SerialMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SerialConsoleRunnable implements Runnable {

	private int threadSleepDuration = 5;
	private String suffix = "*";
	private Logger logger = LoggerFactory.getLogger(SerialConsoleRunnable.class);
	private boolean isPaused = false;
	private SerialMessageQueue serialMessageQueue;

	public SerialConsoleRunnable(SerialMessageQueue serialMessageQueue) {
		this.serialMessageQueue = serialMessageQueue;
	}

	@Override
	public void run() {
		synchronized(this) {
			try {
				StringBuilder sb = new StringBuilder();
				while (true) {
					Thread.sleep(threadSleepDuration);
						while (!serialMessageQueue.isEmpty()) {
							String str = serialMessageQueue.remove();
							if(str!=null)
								sb.append(str);
							if (sb.toString().contains(suffix)) {
								logger.info(sb.toString().trim());
								sb.setLength(0);
							}
						}
						if (isPaused) {
							isPaused = false;
							this.wait();
						}
					}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void pause(){
		isPaused = true;
	}
}
