
public interface SerialInterface {
	//this is a test interface.
	public int connect();
	public void analogWrite(int pinId);
	public void digitalWrite(int pinId);
	public double analogRead(int pinId);
	public boolean digitalRead(int pinId);
}
