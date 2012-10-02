import java.io.IOException;
import java.util.ArrayList;


public class SerialTest extends Serial {
	
	public SerialTest() throws Exception {
		super();
	}

	/*
	 * this is a test implementation of class Serial
	 * This does not reflect the final product, but merely shows future programmars how to
	 * use the Serial class.
	 * 
	 * this class will get 5 data points from logData and average them
	 * and print the averaged value in the Java console.
	 * The process repeats 3 times and the code will unload itself.
	*/
	
	int count = 0;
	int sum = 0;
	
	@Override
	public void log(String s) {
		System.out.println("l: "+s);
	}
	
	@Override
	public void processData(String s) {
		try {
			
			//there has been reports of random line break escape chars being mixed in. filter that out.
			if(s!="\n") {
				sum += Integer.parseInt(s);
			}
			
		}
		
		//only exception would come from parseInt
		catch (Exception e){
			System.out.println("Wrong datum recieved!");
			return;
		}
		
		count++;
		
		if (count==5) {
			System.out.println(sum/5.0);
			count=0;
			sum=0;
		}
	}
}
