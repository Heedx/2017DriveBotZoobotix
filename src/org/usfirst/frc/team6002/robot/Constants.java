package org.usfirst.frc.team6002.robot;

public class Constants {
	//DRIVE
	public static final int kLeftFrontMotorId = 1;
	public static final int kLeftBackMotorId = 2;
	public static final int kRightFrontMotorId = 3;
	public static final int kRightBackMotorId = 4;
	
	//DRIVE SHIFT TARGETS
	//this is in relation to RPM of drive motors
	public static double kShiftTarget = 1000;
	
	//Drive PID values
	public static final double kPDriving = 0.0005;
	public static final double kIDriving = 0.0;
	public static final double kDDriving = 0.002;

	public static final double kPTurning = 0.0;
	public static final double kITurning = 0.0;
	public static final double kDTurning = 0.0;

	public static final double kFLeftDriveVelocity = 0.265;
	public static final double kPLeftDriveVelocity = 0.14;
	public static final double kILeftDriveVelocity = 0.0;
	public static final double kDLeftDriveVelocity = 15;

	public static final double kFRightDriveVelocity = 0.254;
	public static final double kPRightDriveVelocity = 0.1;
	public static final double kIRightDriveVelocity = 0.0;
	public static final double kDRightDriveVelocity = 8;
	
	//COMPRESSOR
	public static int kCompressorId = 0;
	public static int kGearShiftLow = 0;
	public static int kGearShiftHigh = 1;
	
	//CLIMBER
	public static int kClimberId = 1;
	
	//ROLLERS
	public static int kIntakeId = 2;
	public static int kConveyorId = 3;
	
	//SHOOTER AND SERIALIZER
	public static int kMasterShooterId = 16;
	public static int kSlaveShooterId = 17;
	public static int kSerializerId = 0; //previous is 3
	public static double kShooterSpeed = 4000;
	
	//SHOOTER PID values
	public static double kFShooterVelocity = 2.75;
	public static double kPShooterVelocity = 20;
	public static double kIShooterVelocity = 0.0;
	public static double kDShooterVelocity = 100;
	
	//GEARARM
	public static int kGearArmId = 6;
	public static double kPGearArm = 0.6;
	public static double kIGearArm = 0.0;
	public static double kDGearArm = 0.0;

	//Robot's mechanics
	public static int kWheelDiameter = 4;
	public static double kWheelCircumference = kWheelDiameter * Math.PI;
	public static double kMaxDriveRPM = 1074;

	//Encoder
	public static double kNativeUnitsPerRotation = 2000;
	public static double kTicksPerRotation = 500;
	public static double kInchPerPulse = kWheelCircumference / kTicksPerRotation;

}
