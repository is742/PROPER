package inputOutput;

public class Time {

	private double startTime;
	private double stopTime;

	
	
	
	public Time(){
		this.startTime = System.currentTimeMillis();
		this.stopTime=-1;
	}

	public void stop(){
		this.stopTime = System.currentTimeMillis();
	}

	public double duration(){
		if(this.stopTime==-1){	
			return System.currentTimeMillis() - this.startTime;
		}
		return this.stopTime-this.startTime;
	}
	

	public String toString(){
		return (new Double(duration()/1000.0).toString());
	}

}
