package states.sv;

import java.util.LinkedList;

import states.ifSVchassis;

public class stSVChassisVTOLSmall implements ifSVchassis {

	public double getBCV() {
		return 0.2;
	}

	public double getBEV() {
		return 0.002;
	}

	public double getMaximumTonnage() {
		return 4.999;
	}

	public String getMinimumTR() {
		return "C";
	}

	public double getMinimumTonnage() {
		return 0.1;
	}

	public String getMotiveType() {
		return "VTOL";
	}

	public String getSize() {
		return "Small";
	}
	
	public LinkedList<String> getLocations() {
		LinkedList<String> tempLocations = new LinkedList<String>();
		tempLocations.add("Front");
		tempLocations.add("Left");
		tempLocations.add("Right");
		tempLocations.add("Rear");
		tempLocations.add("Rotor");
		tempLocations.add("Chin Turret");
		return tempLocations;
	}

}
