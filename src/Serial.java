import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

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


    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    
    //contains names of ports (ex. COM0, COM1)
    private ArrayList<String> portNames = new ArrayList();
    
    private boolean isConnected = false; 
	
    public Serial() throws Exception {
    	searchForPorts();
    	connect(choosePort(getPorts()));
    	if(initIOStream() == false) {
    		log("IO Init Failed!");
    		throw new Exception();
    	}
    	initListener();
    	log("Sucessfully Connected");
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
	
	public String choosePort(ArrayList<String> ports) throws Exception {
		if(ports.size() != 1) {
			log("Too little/too many devices connected!");
			for(String s:ports)
				log("Device: "+s);
			throw new Exception();
		}
		return ports.get(0);
	}
	
	 public void connect(String port)
	    {
	        String selectedPort = port;
	        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

	        CommPort commPort = null;

	        try
	        {
	            //the method below returns an object of type CommPort
	            commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
	            //the CommPort object can be casted to a SerialPort object
	            serialPort = (SerialPort)commPort;
	            
	            //sets baud rate (transfer rate)
	            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
	                    SerialPort.PARITY_NONE);

	            setConnected(true);

	            //logging
	            log(selectedPort + " opened successfully.");
	            
	            }
	        catch (PortInUseException e) {
	            log(selectedPort + " is in use. (" + e.toString() + ")");
	        }
	        catch (UnsupportedCommOperationException e) {
	        	log("Baud rate selection failed at "+selectedPort+"("+e.toString()+")");
	        }
	        catch (Exception e) {
	            log("Failed to open " + selectedPort + "(" + e.toString() + ")");
	        }
	    }
	 private void setConnected(boolean b) {
		 isConnected=b;
	 }
	 public boolean isConnected() {
		 return isConnected;
	 }
	 public boolean initIOStream() throws IOException
	    {
	        boolean successful = false;

	        try {
	            //
	            input = serialPort.getInputStream();
	            output = serialPort.getOutputStream();
	            writeData(0, 0);

	            successful = true;
	            return successful;
	        }
	        catch (IOException e) {
	            log("I/O Streams failed to open. (" + e.toString() + ")");
	            return successful;
	        }
	    }
	   public void initListener()
	    {
	        try
	        {
	            serialPort.addEventListener(this);
	            serialPort.notifyOnDataAvailable(true);
	        }
	        catch (TooManyListenersException e)
	        {
	            log("Too many listeners. (" + e.toString() + ")");
	        }
	    }
	   
	   public void disconnect()
	    {
	        //close the serial port
	        try
	        {
	            writeData(0, 0);

	            serialPort.removeEventListener();
	            serialPort.close();
	            input.close();
	            output.close();
	            setConnected(false);
	            
	            log("Disconnected.");
	        }
	        catch (Exception e)
	        {
	            log("Failed to close " + serialPort.getName()+ "(" + e.toString() + ")");
	        }
	    }
	   @Override
	   public void serialEvent(SerialPortEvent evt) {
	        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
	        {
	            try
	            {
	                byte singleData = (byte)input.read();

	                if (singleData != NEW_LINE_ASCII)
	                {
	                	processData(new String(new byte[] {singleData}));
	                    logData(new String(new byte[] {singleData}));
	                }
	                else
	                {
	                   	logData("\n");
	                }
	            }
	            catch (Exception e)
	            {
	                log("Failed to read data. (" + e.toString() + ")");
	            }
	        }
	    }
	   public void writeData(int first, int second)
	    {
	        try
	        {
	            output.write(first);
	            output.flush();
	            //this is a delimiter for the data
	            output.write(DASH_ASCII);
	            output.flush();

	            output.write(second);
	            output.flush();
	            //will be read as a byte so it is a space key
	            output.write(SPACE_ASCII);
	            output.flush();
	        }
	        catch (Exception e)
	        {
	            log("Failed to write data. (" + e.toString() + ")");
	        }
	    }
	 public void processData(String s) {
		 //to be implemented by child class
	 }
	 public void log(String s) {
		 System.out.println(s);
	 }
	 public void logData(String s) {
		 System.out.println(s);
	 }

	 
}
