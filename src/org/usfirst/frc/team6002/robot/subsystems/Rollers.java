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

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void rollerInit(){
    	// need to verify these ports
    	intakeMotor = new VictorSP(Constants.kIntakeId);
    	conveyorMotor = new VictorSP(Constants.kConveyorId);
    }
    public void intakeOn(){
    	intakeMotor.set(-0.8);
    }
    public void intakeOff(){
    	intakeMotor.set(0.0);
    }
    public void conveyorOn(){
    	conveyorMotor.set(0.9);
    }
    public void conveyorOff(){
    	conveyorMotor.set(0.0);
    }
}

