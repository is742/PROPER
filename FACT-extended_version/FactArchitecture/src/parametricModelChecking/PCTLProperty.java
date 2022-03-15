package parametricModelChecking;
/**
 * 
 * THe PCTLProperty class contains a PCTL state property expressed
 * in terms of Prism's PCTL Property Specification Language
 * 
 * 
 * 
 * */
public class PCTLProperty extends Property{

	private String property;
	
	public PCTLProperty(String property){
		this.setProperty(property);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	
	
	
}
