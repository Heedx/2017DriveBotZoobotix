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
    public void setShooterVoltage(double volts){
    	masterShooter.set(volts);
    } 
    public void shooterOn(){
    	setShooterVoltage(0.9);
    }
    public void shooterOff(){
    	setShooterVoltage(0.0);
    }
    public void setSerializerSpeed(double speed){
    	serializerMotor.set(speed);
    }
    public void serializerOn(){
    	setSerializerSpeed(0.9);
    }
    public void serializerOff(){
    	setSerializerSpeed(0.0);
    }
    
}

