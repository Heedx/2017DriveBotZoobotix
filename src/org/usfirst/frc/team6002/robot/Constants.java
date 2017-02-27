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
	public static final double kPDriving = 0.0;
	public static final double kIDriving = 0.0;
	public static final double kDDriving = 0.0;

	public static final double kPTurning = 0.0;
	public static final double kITurning = 0.0;
	public static final double kDTurning = 0.0;

	public static final double kPDriveVelocity = 0.5;
	public static final double kIDriveVelocity = 0.0;
	public static final double kDDriveVelocity = 0.0;
	
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
	//SHOOTER PID values
	public static double kPShooterVoltage = 0.5;
	public static double kIShooterVoltage = 0.0;
	public static double kDShooterVoltage = 0.0;
	
	//GEARARM
	public static int kGearArmId = 6;
	public static double kPGearArm = 0.6;
	public static double kIGearArm = 0.0;
	public static double kDGearArm = 0.0;
}
