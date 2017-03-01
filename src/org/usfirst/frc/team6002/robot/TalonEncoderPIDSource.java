package org.usfirst.frc.team6002.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class TalonEncoderPIDSource implements PIDSource{
	private CANTalon leftTalon;
	private CANTalon rightTalon;
	
	public TalonEncoderPIDSource(CANTalon leftTalon, CANTalon rightTalon){
		this.leftTalon = leftTalon;
		this.rightTalon = rightTalon;
	}

	public double pidGet(){
		return convertRotToTicks((leftTalon.getPosition() + rightTalon.getPosition())/2);
	}

	public PIDSourceType getPIDSourceType(){
		return leftTalon.getPIDSourceType();
	}

	public void setPIDSourceType(PIDSourceType pidsource){
		pidsource = leftTalon.getPIDSourceType();
	}

	private double convertRotToTicks(double rot){
		return rot * Constants.kTicksPerRotation;
	}
}