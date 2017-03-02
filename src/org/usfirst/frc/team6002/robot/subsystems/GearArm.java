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
	private boolean getGearToggle;
	private boolean dropGearToggle;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    }
    
    public GearArm(){
    	dropGearToggle = false; 
    	getGearToggle = false;
    	gearArmMotor = new CANTalon(Constants.kGearArmId);
    	gearArmInit();
    }
    public void gearArmInit(){
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
	public void homeGear(){
		setDesiredAngle(0.0);
	}
	public void restGear(){
		setDesiredAngle(gearArmMotor.getPosition());
	}
	public void getGear(){
		setDesiredAngle(0.2);
	}
	public void dropGear(){
		setDesiredAngle(0.4);
	}
	public void inboundGear(){
		setDesiredAngle(0.1); 
	}
	
	public void switchToggle(){
		dropGearToggle = !dropGearToggle;
	}
	
	public boolean getGearToggle(){
		return dropGearToggle;
	}
	
	public void setGearToggle(boolean val){
		dropGearToggle = val;
	}
	
	public void switchGearToggle(){
		getGearToggle = !getGearToggle;
	}
	
	public boolean getInboundToggle(){
		return getGearToggle;
	}
	
	public void setInboundToggle(boolean val){
		getGearToggle = val;
	}
}