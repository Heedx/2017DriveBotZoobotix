package org.usfirst.frc.team6002.robot.subsystems;

import org.usfirst.frc.team6002.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *	defines the motor used for climbing
 */
public class Climber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private VictorSP climberMotor;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void climberInit(){
    	climberMotor = new VictorSP(Constants.kClimberId); //check port number
    	climberMotor.set(0.0);
    }
    public void climberOn(){
    	climberMotor.set(1);
    }
    public void climberOff(){
    	climberMotor.set(0.0);
    }
    public void climberReverse(){
    	climberMotor.set(-1);
    }
}

