package org.usfirst.frc.team6002.robot.subsystems;

import org.usfirst.frc.team6002.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This subystem defines the intake and conveyor belt functions.
 */
public class Rollers extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private VictorSP intakeMotor;
	private VictorSP conveyorMotor;
	private boolean intakeToggle;
    private boolean reverseToggle;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public Rollers(){
    	// need to verify these ports
    	intakeMotor = new VictorSP(Constants.kIntakeId);
    	conveyorMotor = new VictorSP(Constants.kConveyorId);
    	intakeToggle = false;
        reverseToggle = false;
    }
    
    public void intakeOn(){
    	intakeMotor.set(-0.8);
    }
    public void intakeOff(){
    	intakeMotor.set(0.0);
    }
    
    public void intakeReverse(){
        intakeMotor.set(0.8);
    }

    public void conveyorOn(){
    	conveyorMotor.set(0.9);
    }
    public void conveyorOff(){
    	conveyorMotor.set(0.0);
    }
    public void conveyorReverse(){
        conveyorMotor.set(-0.9);
    }
    
    public void switchIntakeToggle(){
    	intakeToggle = !intakeToggle;
    }
    
    public boolean getIntakeToggle(){
    	return intakeToggle;
    }

    public void reverse(){
        intakeReverse();
        conveyorReverse();
    }

    public void switchReverseToggle(){
        reverseToggle = !reverseToggle;
    }

    public boolean getReverseToggle(){
        return reverseToggle;
    }

    public void setIntakeToggle(boolean val){
        intakeToggle = val;
    }

    public void setReverseToggle(boolean val){
        reverseToggle = val;
    }
}

