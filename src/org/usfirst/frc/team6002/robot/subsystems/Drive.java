package org.usfirst.frc.team6002.robot.subsystems;

import org.usfirst.frc.team6002.robot.Constants;
import org.usfirst.frc.team6002.robot.DummyPIDOutput;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import org.usfirst.frc.team6002.robot.Robot;
import org.usfirst.frc.team6002.robot.TalonEncoderPIDSource;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.VictorSP;


/**
 *	This subsystem defines the drivetrain's CANTalons and pneumatics for driving the robot.
 */
public class Drive extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	int kForward = 1;
	int kReverse = 0;
	
	private static AHRS gyro = new AHRS(SPI.Port.kMXP);

	private static CANTalon leftFrontMotor;
	private static CANTalon leftBackMotor;
	private static CANTalon rightFrontMotor;
	private static CANTalon rightBackMotor;

    //Dummy classes used to hold the output from the PIDController.
    private PIDOutput drivePIDOutput;
    private PIDOutput turnPIDOutput;

    private PIDController drivePID;
    private PIDController turnPID;

    //Custom made PIDSource class to return a talon's encoder value
    private PIDSource talonEncoder;

	private static Compressor airC;

	private static DoubleSolenoid gearShiftSolenoid;
	private static boolean enabled;
	private static boolean isItInHighGear; 
	
    public Drive(){
        gyro = new AHRS(SPI.Port.kMXP);

        drivePIDOutput = new DummyPIDOutput();
        turnPIDOutput = new DummyPIDOutput();

        leftFrontMotor = new CANTalon(Constants.kLeftFrontMotorId);
        leftBackMotor = new CANTalon(Constants.kLeftBackMotorId);
        rightFrontMotor = new CANTalon(Constants.kRightFrontMotorId);
        rightBackMotor = new CANTalon(Constants.kRightBackMotorId);

        //Initialize motors
        motorInit();

        talonEncoder = new TalonEncoderPIDSource(leftFrontMotor);

        drivePID = new PIDController(Constants.kPDriving, Constants.kIDriving, Constants.kDDriving, talonEncoder, drivePIDOutput);
        turnPID = new PIDController(Constants.kPTurning, Constants.kITurning, Constants.kDTurning, gyro, turnPIDOutput);

        airC = new Compressor(Constants.kCompressorId);

        gearShiftSolenoid = new DoubleSolenoid(0, 1);
        enabled = airC.enabled();
        isItInHighGear = false;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void motorInit(){

        //initialize motors in percentvbus for master motors, and follower for slave motors
        leftFrontMotor.changeControlMode(CANTalon.TalonControlMode.Position);
        leftFrontMotor.set(0.0);
        leftBackMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
    	leftBackMotor.set(leftFrontMotor.getDeviceID());

        rightFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	rightFrontMotor.set(0.0);
        rightBackMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
    	rightBackMotor.set(rightFrontMotor.getDeviceID());
    	
        //Brake mode
        leftFrontMotor.enableBrakeMode(true);
        leftBackMotor.enableBrakeMode(true);
        rightFrontMotor.enableBrakeMode(true);
    	rightBackMotor.enableBrakeMode(true);

    	//Encoder initialization
        leftFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        if (leftFrontMotor.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
        		System.out.println("Unable to find leftFrontMotor encoder!");
        }
        
        leftFrontMotor.reverseSensor(true);//change this as needed
        leftFrontMotor.reverseOutput(true);//change this as needed
        
        rightFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        if (rightFrontMotor.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
        		System.out.println("Unable to find rightFrontMotor encoder!");
        }
        
        rightFrontMotor.reverseSensor(false);//change this as needed
        rightFrontMotor.reverseOutput(false);//change this as needed        
        
    	//load PID values
        rightFrontMotor.setPID(Constants.kPDriveVelocity, Constants.kIDriveVelocity, Constants.kDDriveVelocity);
        rightFrontMotor.setProfile(0);
        leftFrontMotor.setPID(Constants.kPDriveVelocity, Constants.kIDriveVelocity, Constants.kDDriveVelocity);
        leftFrontMotor.setProfile(0);   
    }
    
    public void compressorOn(){
    	airC.setClosedLoopControl(true);
    	
    	System.out.println(enabled);
    }
    
    public void driveWithJoysticks(double leftValue, double rightValue){
    	leftFrontMotor.set(-(leftValue*leftValue*leftValue)); // cube each value to decrease sensitivity.
    	rightFrontMotor.set((rightValue*rightValue*rightValue));
    }
	
    public void drive(double leftDrive, double rightDrive){
    	leftFrontMotor.set(leftDrive); // cube each value to decrease sensitivity.
    	rightFrontMotor.set(-rightDrive);
    }
    
    public void driveStraightForADistance(double distanceInInches){
    	System.out.println("----------------------------------------------------------------");
        System.out.println("Gyro: " + gyro.pidGet() + " Gyro.get(): " + gyro.getAngle()%360);

<<<<<<< HEAD
        System.out.println("Encoder: " + leftFrontMotor.pidGet() + " talon.get(): " + leftFrontMotor.get());
=======
        System.out.println("Encoder: " + leftFrontMotor.pidGet() + " talon.get(): " + leftFrontMotor.getPosition());
        System.out.println("----------------------------------------------------------------");
>>>>>>> c54ece818b46d9a71ed110d5e19733ccd3e9bcbb
    }

    public double convertInchesToTicks(double inches){
        return Math.abs(inches / Constants.kInchPerPulse);
    }

    public double convertTicksToInches(double ticks){
        return Math.abs(ticks * Constants.kInchPerPulse);
    }

    public void setHighGear(){
    	gearShiftSolenoid.set(DoubleSolenoid.Value.kForward);
    	//System.out.println("Forward");
    	isItInHighGear = true;
    }

    public void setLowGear(){
    	gearShiftSolenoid.set(DoubleSolenoid.Value.kReverse);
    	//System.out.println("Reverse");
    	isItInHighGear = false;
    }

    public double leftMotorSpeed(){
    	return leftFrontMotor.get();// only returns front motor speeds
    }

    public double rightMotorSpeed(){
    	return rightFrontMotor.get();// only returns front motor speeds
    }

    public double getRightDriveRPM(){
    	//System.out.println("rightFrontMotor RPM is:" + rightFrontMotor.getEncVelocity());
    	return leftFrontMotor.getEncVelocity();
    }

    public double getLeftDriveRPM(){
    	//System.out.println("leftFrontMotor RPM is:" + leftFrontMotor.getEncVelocity());
    	return rightFrontMotor.getEncVelocity();
    }
    
    /*
    public void autoShift(){
    	//check if bothsides are above the target. If both sides are above speed,
    	//then the gearshift will shift up.
    	if(Robot.chassis.getLeftDriveRPM() > Constants.kShiftTarget && Robot.chassis.getRightDriveRPM() > Constants.kShiftTarget){
			Robot.chassis.setHighGear();
			isItInHighGear = true;
    	}
    	//checks if both sides are below the target. If bothsides are below speed, and it is in high gear,
    	//then the gearshift will shift down.
		else if(Robot.chassis.getLeftDriveRPM() < Constants.kShiftTarget && Robot.chassis.getRightDriveRPM() < Constants.kShiftTarget 
				&& isItInHighGear == true){
				Robot.chassis.setLowGear();
		}
	}
	*/
}

 