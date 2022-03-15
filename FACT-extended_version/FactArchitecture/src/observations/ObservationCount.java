package observations;
/**
 * This class stores the number of times a transition has been observed, 
 * and the "total" number of transitions made from that state.  
 * 
 * */
public class ObservationCount {
	
	public double transitionObservationCount;
	public double totalObservationCountForStateParameter;
	public int n_size;

	
	public String toString(){
		return transitionObservationCount+"/"+totalObservationCountForStateParameter;
	}
}
