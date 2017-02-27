package org.usfirst.frc.team6002.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6002.robot.Constants;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 *
 */
public class GearArm extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon gearArmMotor;
	private boolean getGearToggle, dropGearToggle;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    }
    public void gearArmInit(){
    	gearArmMotor = new CANTalon(Constants.kGearArmId);
    	gearArmMotor.changeControlMode(TalonControlMode.Position);
    	gearArmMotor.set(0.0);
    	gearArmMotor.enableBrakeMode(true);
    	gearArmMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        gearArmMotor.reverseSensor(false);
        gearArmMotor.reverseOutput(false);
        gearArmMotor.setPosition(0);
        gearArmMotor.setPID(Constants.kPGearArm, Constants.kIGearArm, Constants.kDGearArm);
        gearArmMotor.setProfile(0);
        
    }
	public void setDesiredAngle(double gearArmTarget){
		//gearArmMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		gearArmMotor.set(gearArmTarget);
	}
	public void zeroGearArmSensors(){
		gearArmMotor.setPosition(0.06);
		System.out.println("gear arm position set to 0");
	}
	public void restGear(){
		setDesiredAngle(0.0);
	}
	public void getGear(){
		getGearToggle = !getGearToggle;
		setDesiredAngle(0.2);
		System.out.println(getGearToggle);
	}
	public void dropGear(){
		setDesiredAngle(0.4);
	}
//	public void runtoggle(boolean buttonValue){
//		if(buttonValue && !buttonLast){
//			buttonToggle = !buttonToggle
//		}
//		buttonLast = buttonValue
//	}
//	
}