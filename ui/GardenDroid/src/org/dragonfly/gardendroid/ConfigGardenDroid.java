package org.dragonfly.gardendroid;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author lee
 * Simply having no luck getting this thing to Sens a  message, could be because of the RF access already running?
 */
public class ConfigGardenDroid  implements SerialPortEventListener {
	static final SimpleDateFormat txDateFormat = new SimpleDateFormat("yyMMddhhmmss");
	
	public enum Commands {  
		HELP, SET_TIME, SET_TEMP_FREQ, SET_TEMP_THRESHOLD;
	}
	
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
	private static final int DATA_RATE = 1200;

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
		System.out.println("Could not find COM port.");
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
		System.err.println(e.toString());
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
	try {
		Thread.sleep(500);  //wait to get the message
	} catch (InterruptedException e1) {
		System.out.println(e1);
	}
	
	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
			int available = input.available();
			byte chunk[] = new byte[available];
			input.read(chunk, 0, available);

			// Displayed results are codepage dependent
			System.out.print(new String(chunk));
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	// Ignore all the other eventTypes, but you should consider the other ones.
}

/**
 * Sends provided command to GardenDroid.
 * @param cmd
 * @param msg
 * @throws IOException 
 */
public void sendCommand(String cmd, String msg) throws IOException {
	if(cmd != null ){
		String fullMsg = (msg != null)?(cmd.toUpperCase()+" "+msg):cmd.toUpperCase();
		char[] cStr = fullMsg.toCharArray();
		for (char c : cStr) {
			this.output.write(Character.getNumericValue(c));
		}
		this.output.write(0x0d);
		
		this.output.flush();
	}
	
}

public static void main(String[] args) throws Exception {
	ConfigGardenDroid main = new ConfigGardenDroid();
	try{
		main.initialize();
		
		//Send command
		if(args.length>0){
//			first is what to set or help
			String cmd = args[0];
			String data = (args.length > 1)? args[1]: null;
			System.out.println("CMD="+cmd + " data=" + data);
			if(cmd.toUpperCase().equals(Commands.HELP.toString())) {
				printHelp();
			}
			else if(cmd.toUpperCase().equals(Commands.SET_TIME.toString())){
				
				Date now = new Date();
				System.out.println("Setting GardenDroid clock to current time:" + now);
				
				String dateToSend = txDateFormat.format(now);
				String msg = "T|" + dateToSend;
				System.out.println("Send: "+msg);
				
				main.sendCommand(cmd, msg);
			}
		}
		else{
			printHelp();
		}
		System.in.read();
		System.out.println("Shutting down...");
	}
	catch (Throwable tw)
	{
	}
	finally{
		main.close();
	}
}

private static void printHelp() {
	System.out.println("== Config GardenDroid Help == ");
	
}

}
