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
	
	// private static AHRS gyro;

	private static CANTalon leftFrontMotor;
	private static CANTalon leftBackMotor;
	private static CANTalon rightFrontMotor;
	private static CANTalon rightBackMotor;

    //Dummy classes used to hold the output from the PIDController.
    private PIDOutput drivePIDOutput;
    private PIDOutput turnPIDOutput;

    private PIDController drivePID;
    private PIDController turnPID;

    private double maxDrivePIDOutput;

    //Custom made PIDSource class to return the average of both of the talons' encoder values
    private PIDSource talonEncoders;

	private static Compressor airC;

	private static DoubleSolenoid gearShiftSolenoid;
	private static boolean enabled;
	private static boolean isItInHighGear; 
	
    public Drive(){
        // gyro = new AHRS(SPI.Port.kMXP);

        drivePIDOutput = new DummyPIDOutput();
        turnPIDOutput = new DummyPIDOutput();

        leftFrontMotor = new CANTalon(Constants.kLeftFrontMotorId);
        leftBackMotor = new CANTalon(Constants.kLeftBackMotorId);
        rightFrontMotor = new CANTalon(Constants.kRightFrontMotorId);
        rightBackMotor = new CANTalon(Constants.kRightBackMotorId);

        //Initialize motors
        motorInit();

        talonEncoders = new TalonEncoderPIDSource(leftFrontMotor, rightFrontMotor);

        drivePID = new PIDController(Constants.kPDriving, Constants.kIDriving, Constants.kDDriving, talonEncoders, drivePIDOutput);
        // turnPID = new PIDController(Constants.kPTurning, Constants.kITurning, Constants.kDTurning, gyro, turnPIDOutput);
        
        maxDrivePIDOutput = .5;

        drivePID.setOutputRange(-maxDrivePIDOutput, maxDrivePIDOutput); 
        // turnPID.setOutputRange(-1.0, 1.0);

        drivePID.setPercentTolerance(3);

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
        leftFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
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
        leftFrontMotor.reverseOutput(false);//change this as needed
        
        rightFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        if (rightFrontMotor.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
        		System.out.println("Unable to find rightFrontMotor encoder!");
        }

        leftFrontMotor.configEncoderCodesPerRev(500);
        rightFrontMotor.configEncoderCodesPerRev(500);

        rightFrontMotor.reverseSensor(false);//change this as needed
        rightFrontMotor.reverseOutput(true);//change this as needed        
        
        leftFrontMotor.configNominalOutputVoltage(+0.0f, -0.0f);
        leftFrontMotor.configPeakOutputVoltage(+12.0f, -12.0f);

        rightFrontMotor.configNominalOutputVoltage(+0.0f, -0.0f);
        rightFrontMotor.configPeakOutputVoltage(+12.0f, -12.0f);

    	//load PID values
        leftFrontMotor.setProfile(0);  
        leftFrontMotor.setF(Constants.kFLeftDriveVelocity);
        leftFrontMotor.setP(Constants.kPLeftDriveVelocity); 
        leftFrontMotor.setI(Constants.kILeftDriveVelocity);
        leftFrontMotor.setD(Constants.kDLeftDriveVelocity);

        rightFrontMotor.setProfile(0);
        rightFrontMotor.setF(Constants.kFRightDriveVelocity);
        rightFrontMotor.setP(Constants.kPRightDriveVelocity); 
        rightFrontMotor.setI(Constants.kIRightDriveVelocity);
        rightFrontMotor.setD(Constants.kDRightDriveVelocity);     

        resetTalonEncoders(); 
    }
    
    public void compressorOn(){
    	airC.setClosedLoopControl(true);
    	
    	System.out.println(enabled);
    }
    
    public void driveWithJoysticks(double leftValue, double rightValue){
    	leftFrontMotor.set(-leftValue); // cube each value to decrease sensitivity.
    	rightFrontMotor.set(rightValue);
    }
	
    public void drive(double leftDrive, double rightDrive){
    	leftFrontMotor.set(leftDrive); // cube each value to decrease sensitivity.
    	rightFrontMotor.set(-rightDrive);
    }
    
    public void driveStraightForADistance(double distanceInInches){
    	// System.out.println("----------------------------------------------------------------");
     //    //System.out.println("Gyro: " + gyro.pidGet() + " Gyro.get(): " + gyro.getAngle()%360);
     //    System.out.print("Target Ticks:");
     //    System.out.print(convertInchesToTicks(distanceInInches));
     //    System.out.print("Encoder:");
     //    System.out.print(talonEncoders.pidGet());
     //    System.out.print("Inches:");
     //    System.out.print(convertTicksToInches(talonEncoders.pidGet()));
     //    System.out.println("----------------------------------------------------------------");

        double targetRPM;// = Constants.maxDriveRPM * .6;

        //Reset talons' encoders
        resetTalonEncoders();
        //Set talons to speed control and enable pid loops
        setTalonsToSpeedControl();
        //enableTalonsPIDLoop();
        
        drivePID.setSetpoint(convertInchesToTicks(distanceInInches));
        drivePID.reset();    
        drivePID.enable();

        // while(!drivePID.onTarget()){
        //     targetRPM = drivePID.get();
        //     System.out.println(targetRPM + " " + talonEncoders.pidGet() + " " + drivePID.getSetpoint());
        // }
        drivePID.disable();

        // rightFrontMotor.set(targetRPM);
        // leftFrontMotor.set(targetRPM);

        // System.out.print("\terr(native):");
        // System.out.print(rightFrontMotor.getClosedLoopError());
        // System.out.print("\tspd:");
        // System.out.print(leftFrontMotor.getSpeed());
        // System.out.print("\terr(rpm):");
        // System.out.print(convertNativeToRPM(leftFrontMotor.getClosedLoopError()));
        // System.out.print("\ttrg:");
        // System.out.print(targetRPM);

        // System.out.print("\tspd:");
        // System.out.print(rightFrontMotor.getSpeed());
        // System.out.print("\terr(rpm):");
        // System.out.print(convertNativeToRPM(rightFrontMotor.getClosedLoopError()));
        // System.out.print("\ttrg:");
        // System.out.print(targetRPM);

        // System.out.print("\n");
    }

    //UNIT CONVERSION FUNCTIONS
    public double convertInchesToTicks(double inches){
        return Math.abs(inches / Constants.kInchPerPulse);
    }

    public double convertTicksToInches(double ticks){
        return Math.abs(ticks * Constants.kInchPerPulse);
    }

    public double convertRotToInches(double rot){
        return rot * Constants.kWheelCircumference;
    }

    public double convertRpmToInchesPerSecond(double rpm){
        return convertRotToInches(rpm) / 60;
    }

    public double convertInchesToRot(double inches){
        return inches / Constants.kWheelCircumference;
    }

    public double convertInchesPerSecondToRpm(double inchesPerSecond){
        return convertInchesToRot(inchesPerSecond) * 60;
    }

    public double convertNativeToRPM(double nativeVal){
        double millisecPerSeconds = 1000;
        double secondsPerMin = 60;
        double nativePerMillisec = 100;
        return (nativeVal*millisecPerSeconds*secondsPerMin)/(nativePerMillisec*Constants.kNativeUnitsPerRotation);
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

    public double getLeftDriveSpeed(){
    	return leftFrontMotor.getSpeed();// only returns front motor speeds
    }

    public double getRightDriveSpeed(){
    	return rightFrontMotor.getSpeed();// only returns front motor speeds
    }

    public double getRightDriveRPM(){
    	//System.out.println("rightFrontMotor RPM is:" + rightFrontMotor.getEncVelocity());
    	return leftFrontMotor.getEncVelocity();
    }

    public double getLeftDriveRPM(){
    	//System.out.println("leftFrontMotor RPM is:" + leftFrontMotor.getEncVelocity());
    	return rightFrontMotor.getEncVelocity();
    }

    public void setLeftDriveSpeed(double rpm){
        leftFrontMotor.set(rpm);
    }

    public void setRightDriveSpeed(double rpm){
        rightFrontMotor.set(rpm);
    }

    //Set the talons' encoders to 0
    public void resetTalonEncoders(){
        leftFrontMotor.setEncPosition(0);
        rightFrontMotor.setEncPosition(0);
    }

    public void setTalonsToSpeedControl(){
        leftFrontMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
        rightFrontMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    }

    public void setTalonsToVoltageControl(){
        //Disable the pid loops when using the talons as voltage controls
        disableTalonsPIDLoop();
        leftFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        rightFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    }

    public void enableTalonsPIDLoop(){
        //Reset the pid loops first
        leftFrontMotor.reset();
        rightFrontMotor.reset();

        //Enable pid loop
        leftFrontMotor.enable();    
        rightFrontMotor.enable();
    }

    public void disableTalonsPIDLoop(){
        leftFrontMotor.disable();
        rightFrontMotor.disable();
    }

    public void enableDrivePID(){
        drivePID.enable();
    }

    public void disableDrivePID(){
        drivePID.disable();
    }

    public void resetDrivePID(){
        drivePID.reset();
    }

    public void setDrivePIDSetpoint(double setpoint){
        drivePID.setSetpoint(setpoint);
    }

    public double getDrivePIDOutput(){
        return drivePID.get();
    }

    public double getDrivePIDSetpoint(){
        return drivePID.getSetpoint();
    }

    public double getDrivePIDInput(){
        return talonEncoders.pidGet();
    }

    public boolean isTalonControlEnabled(){
        return leftFrontMotor.isControlEnabled();
    }

    public void enableTalonsControl(){
        leftFrontMotor.enableControl();
        rightFrontMotor.enableControl();
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

 