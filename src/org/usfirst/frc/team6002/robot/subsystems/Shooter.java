package org.usfirst.frc.team6002.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team6002.robot.Constants;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.VictorSP;


/**
 *
 */
public class Shooter extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon masterShooter, slaveShooter;
	VictorSP serializerMotor;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void shooterInitialize() {
    	//initialize motor ports
    	masterShooter = new CANTalon(Constants.kMasterShooterId);
    	slaveShooter = new CANTalon(Constants.kSlaveShooterId);
    	serializerMotor = new VictorSP(Constants.kSerializerId); // check the serializer port number on roborio
    	//set talon control modes(percentVbus for master, follower for slave)
    	masterShooter.changeControlMode(TalonControlMode.PercentVbus);
    	masterShooter.set(0.0);
    	slaveShooter.changeControlMode(TalonControlMode.Follower);
    	slaveShooter.set(Constants.kMasterShooterId);
    	masterShooter.enableBrakeMode(false);
    	slaveShooter.enableBrakeMode(false);
    	//set up encoder for master
    	masterShooter.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	masterShooter.reverseSensor(false);
    	masterShooter.reverseOutput(false);
    	slaveShooter.reverseSensor(false);
    	slaveShooter.reverseOutput(true);
    	//set up pid for master
    	masterShooter.setPID(Constants.kPShooterVoltage, Constants.kIShooterVoltage, Constants.kDShooterVoltage);
    	masterShooter.setProfile(0);
    }
    public void shooterOn(){
    	masterShooter.set(0.8);
    }
    public void shooterOff(){
    	masterShooter.set(0.0);
    }
    public void serializerOn(){
    	serializerMotor.set(0.5);
    }
    public void serializerOff(){
    	serializerMotor.set(0.0);
    }
    
}

