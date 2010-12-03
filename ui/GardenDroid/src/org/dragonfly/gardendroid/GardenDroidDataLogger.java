package org.dragonfly.gardendroid;

import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.dragonfly.gardendroid.dto.GardenDroidData;
import org.dragonfly.gardendroid.dto.SensorDataFactory;

public class GardenDroidDataLogger implements SerialPortEventListener {
	static final Logger log = Logger.getLogger(GardenDroidDataLogger.class);
	
	
	SerialPort serialPort;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB1", // Linux
			"COM3", // Windows
			};
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 1200;//9600;

	StringBuilder message =  new StringBuilder();
	
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			log.error("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Thread.sleep(500);  //wait to get the message
			} catch (InterruptedException e1) {
				log.error(e1);
			}
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);
				// Displayed results are codepage dependent
				for (byte b : chunk) {
					if(b>32 && b<127){
						byte[] bb = {b};
						message.append(new String( bb));
					}
					else if(b == 0x04 | b == -1)
					{
						if(message.length()>0){
							GardenDroidData gdata = SensorDataFactory.parseGardenData(message.toString());
							if(log.isDebugEnabled()){
								log.debug("EOM");
								log.debug("Message==["+ gdata+"]");
							}
							//TODO: Pass Object to persistence layer and save using separate thread or queue/stack.
							RestController.postDataToServer(gdata);
						}
						message = new StringBuilder();
					}
				}
				
			} catch (Exception e) {
				log.error("Error processing message. "+e.toString());
				message = new StringBuilder();
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public static void main(String[] args) throws Exception {
		GardenDroidDataLogger main = new GardenDroidDataLogger();
		try{
			log.info("Start up: "+new Date());
			main.initialize();
			System.in.read();
			System.out.println("Shutting down @ "+new Date());
		}
		catch (Throwable tw)
		{
		}
		finally{
			main.close();
		}
	}
}
