package application.model;

public class LoopInfo {
	private double mean;
	private double stddev;
	
	public LoopInfo(double mean, double stddev) {
		this.mean=mean;
		this.stddev=stddev;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStddev() {
		return stddev;
	}

	public void setStddev(double stddev) {
		this.stddev = stddev;
	}
	
	
}
