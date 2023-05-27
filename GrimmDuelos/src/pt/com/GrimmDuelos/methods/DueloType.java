package pt.com.GrimmDuelos.methods;

public enum DueloType {
	LIVRE("Livre"), HAND("Mão"), FULL("Full P4");
	
	public String display;
	DueloType(String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		return this.display;
	}
}
