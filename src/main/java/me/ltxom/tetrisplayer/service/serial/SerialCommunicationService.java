package me.ltxom.tetrisplayer.service.serial;

import jssc.*;
import me.ltxom.tetrisplayer.entity.serial.SerialMessageQueue;
import me.ltxom.tetrisplayer.util.SerialConsoleRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SerialCommunicationService {
	private SerialPort serialPort;
	private SerialConsoleRunnable serialConsoleRunnable;
	@Autowired
	private SerialMessageQueue serialMessageQueue;

	public void init() {
		String[] portNames = SerialPortList.getPortNames();
		if (portNames.length == 0) {
			System.out.println("There are no serial-ports :( You can use an emulator, such " +
					"ad VSPE, to create a virtual serial port.");
			System.out.println("Press Enter to exit...");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String portName = null;
		for (int i = 0; i < portNames.length; i++) {
			portName = portNames[i];
			System.out.println(portName);
		}
		serialPort = new SerialPort(portName);

		new Thread(() -> {
			try {
				serialPort.openPort();

				serialPort.setParams(SerialPort.BAUDRATE_115200,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_SPACE);

				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
						SerialPort.FLOWCONTROL_RTSCTS_OUT);

				serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

				serialConsoleRunnable = new SerialConsoleRunnable(serialMessageQueue);
				serialConsoleRunnable.run();

			} catch (SerialPortException ex) {
				System.err.println("There are an error on writing string to port т: " + ex);
			}
		}).start();

	}

	public void resumeLogging() {
		new Thread(() -> {
			serialConsoleRunnable.run();
		}).start();
	}

	public void pauseLogging() {
		serialConsoleRunnable.pause();
	}

	public void writeString(String str){
		try {
			if (serialPort == null) {
				System.err.println("Serial Error");
				System.exit(400);
			}
			serialPort.writeString(str);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	private class PortReader implements SerialPortEventListener {

		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					serialMessageQueue.add(serialPort.readString(event.getEventValue()));
				} catch (SerialPortException ex) {
					System.out.println("Error in receiving string from COM-port: " + ex);
				}
			}
		}
	}

}
