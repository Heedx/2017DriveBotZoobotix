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
	private static CANTalon masterShooter, slaveShooter;
	private static VictorSP serializerMotor;
    private static boolean toggle;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public Shooter(){
        //initialize motor ports
        masterShooter = new CANTalon(Constants.kMasterShooterId);
        slaveShooter = new CANTalon(Constants.kSlaveShooterId);
        serializerMotor = new VictorSP(Constants.kSerializerId); // check the serializer port number on roborio

        toggle = false;

        shooterInitialize();
    }

    public void shooterInitialize() {
    	//set talon control modes(percentVbus for master, follower for slave)
    	masterShooter.changeControlMode(TalonControlMode.PercentVbus);
    	masterShooter.set(0.0);
    	slaveShooter.changeControlMode(TalonControlMode.Follower);
    	slaveShooter.set(Constants.kMasterShooterId);

    	masterShooter.enableBrakeMode(false);
    	slaveShooter.enableBrakeMode(false);

    	//set up encoder for master
    	masterShooter.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        masterShooter.configEncoderCodesPerRev(12); //Armobot encoder

        //Reset the encoder
        masterShooter.setPosition(0);
        masterShooter.setEncPosition(0);

    	masterShooter.reverseSensor(false);
    	masterShooter.reverseOutput(false);
        slaveShooter.reverseOutput(true); //It's on the left side of the shooter so its output needs to be reversed

        masterShooter.configNominalOutputVoltage(+0.0f, -0.0f);
        masterShooter.configPeakOutputVoltage(+12f, -12f);

//        setShooterToSpeedControl();

    	//set up pid for master
    	masterShooter.setProfile(0);
        masterShooter.setF(Constants.kFShooterVelocity);
        masterShooter.setP(Constants.kPShooterVelocity); 
        masterShooter.setI(Constants.kIShooterVelocity);
        masterShooter.setD(Constants.kDShooterVelocity);

        //Enable pid
//        enableShooterPID();
    }

    public void switchToggle(){
        toggle = !toggle;
    }

    public boolean getToggle(){
        return toggle;
    }

    public void setShooterVoltage(double volts){
    	masterShooter.set(volts);
    } 

    public void enableShooterPID(){
        masterShooter.reset();
        setShooterToSpeedControl();
        masterShooter.enable();
    }

    public void disableShooterPID(){
        masterShooter.disable();
    }

    public void setShooterSpeed(double rpm){
        masterShooter.set(rpm);
    }

    public void setShooterToVoltageControl(){
        masterShooter.changeControlMode(TalonControlMode.PercentVbus);
    }

    public void setShooterToSpeedControl(){
        masterShooter.changeControlMode(TalonControlMode.Speed);
    }

    public double getShooterSpeed(){
        return masterShooter.getSpeed();
    }

    public double getShooterSetpoint(){
        return masterShooter.getSetpoint();
    }

    public void shooterOn(){
    	setShooterVoltage(0.9);
        //setShooterSpeed(Constants.kShooterSpeed);
    }

    public void shooterOff(){
        //disableShooterPID();
        setShooterToVoltageControl();
    	setShooterVoltage(0);
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

