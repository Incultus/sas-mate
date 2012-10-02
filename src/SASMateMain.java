import java.io.IOException;


public class SASMateMain {

	//the to-be main class of the MATE controller
	
	//TODO implement child class of Serial (such as SerialTest)
	//TODO implement joystick controls
	//TODO make GUI
	//TODO parse info from joystick and send to Serial child class
	
	//TODO DO-ALL-THE-THINGS.png
	
	public static void main(String[] args) throws Exception {
	
		/*
		 * test use of SerialTest class; client classes may use writeData to write data
		 * and child classes must provide a usable way of logging data within itself.
		 * 
		 * all classes implementing Serial class is expected to be similar to SerialTest class.
		*/
		
		//this initializes all connections and initializes the listener
		SerialTest s = new SerialTest();
		
		//this writes a sample data point to the serial line: (1,0).
		s.writeData(1, 0);
		
	}

}
