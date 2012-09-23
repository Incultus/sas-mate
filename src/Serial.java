import gnu.io.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class Serial implements SerialPortEventListener {
    
	//for containing the ports that will be found
    private Enumeration ports = null;
    
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    
    //contains names of ports (ex. COM0, COM1)
    private ArrayList<String> portNames = new ArrayList();
    
    private boolean isConnected = false; 
    
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void searchForPorts() {
	        ports = CommPortIdentifier.getPortIdentifiers();

	        while (ports.hasMoreElements()) {
	            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

	            //get only serial ports
	            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	                portNames.add(curPort.getName());
	                portMap.put(curPort.getName(), curPort);
	            }
	        }
	 }
	
	public ArrayList<String> getPorts() {
		if(portNames.size() != 0)
			return portNames;
		searchForPorts();
		if(portNames.size() != 0)
			return portNames;
		else
			return null;
	}
	
	 public void connect(String portSelected)
	    {
	        String selectedPort = portSelected;
	        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

	        CommPort commPort = null;

	        try
	        {
	            //the method below returns an object of type CommPort
	            commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
	            //the CommPort object can be casted to a SerialPort object
	            serialPort = (SerialPort)commPort;

	            //for controlling GUI elements
	            setConnected(true);

	            //logging
	            logText = selectedPort + " opened successfully.";
	            window.txtLog.setForeground(Color.black);
	            window.txtLog.append(logText + "\n");

	            //CODE ON SETTING BAUD RATE ETC OMITTED
	            //XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

	            //enables the controls on the GUI if a successful connection is made
	            window.keybindingController.toggleControls();
	        }
	        catch (PortInUseException e)
	        {
	            logText = selectedPort + " is in use. (" + e.toString() + ")";

	            window.txtLog.setForeground(Color.RED);
	            window.txtLog.append(logText + "\n");
	        }
	        catch (Exception e)
	        {
	            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
	            window.txtLog.append(logText + "\n");
	            window.txtLog.setForeground(Color.RED);
	        }
	    }
	 private void setConnected(boolean b) {
		 isConnected=b;
	 }
	 private boolean isConnected() {
		 return isConnected;
	 }
}
