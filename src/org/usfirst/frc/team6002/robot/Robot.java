
package org.usfirst.frc.team6002.robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6002.robot.subsystems.Drive;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team6002.robot.subsystems.Climber;
import org.usfirst.frc.team6002.robot.subsystems.Rollers;
import org.usfirst.frc.team6002.robot.subsystems.Shooter;
import org.usfirst.frc.team6002.robot.subsystems.GearArm;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private static OI oi;
	private static Drive chassis;
	//private static Climber climber;
	private static Rollers roller;
	private static Shooter shooter;
	private static GearArm geararm;
	
	Thread visionThread;
	
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chassis = new Drive();
		//climber = new Climber();
		roller = new Rollers();
		shooter = new Shooter();
		geararm = new GearArm();

		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		// Get the Axis camera from CameraServer
		//AxisCamera camera = CameraServer.getInstance().addAxisCamera("axis-camera.local");
		// Set the resolution
		//camera.setResolution(640, 480);
			
		/*
		visionThread = new Thread(() -> {
			

			
			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(255, 255, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});

		visionThread.setDaemon(true);
		visionThread.start();
		*/
   	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		chassis.setTalonsToVoltageControl();
		chassis.drive(0, 0);
		shooter.disableShooterPID();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		System.out.println(oi.getLeftY() + " " + oi.getRightY());
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		// if (autonomousCommand != null)
		// 	autonomousCommand.start();

		// chassis.resetTalonEncoders();
		// chassis.setTalonsToSpeedControl();
		// chassis.enableTalonsPIDLoop();

		// chassis.setDrivePIDSetpoint(chassis.convertInchesToTicks(60));
		// chassis.resetDrivePID();
        // chassis.enableDrivePID();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		/*
		double targetRPM = chassis.getDrivePIDOutput() * Constants.kMaxDriveRPM;

		chassis.setLeftDriveSpeed(targetRPM);
		chassis.setRightDriveSpeed(targetRPM);

		System.out.println(targetRPM + " " + chassis.getDrivePIDInput() + " " + chassis.convertTicksToInches(chassis.getDrivePIDInput()) + " " + chassis.getDrivePIDSetpoint()
			+ " " + chassis.getLeftDriveSpeed() + " " + chassis.getRightDriveSpeed());
		*/
		//chassis.driveStraightForADistance(4);
		//chassis.drive(1, 0);
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		//Disable drive PID from the autonomous phase
		chassis.disableDrivePID();	
		chassis.resetTalonEncoders();
		chassis.setTalonsToVoltageControl();
		chassis.enableTalonsControl();
		//turn on shooter
		shooter.enableShooterPID();

		// Robot.chassis.compressorOn();

		// Robot.climber.climberInit();
		// Robot.roller.rollerInit();
		// Robot.geararm.gearArmInit();
	}
	
    boolean aLastVal = false;
    boolean currVal = false;
	
    boolean last1Val = false;
    boolean curr1Val = false;

    boolean intakeToggle = false;
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		while(isOperatorControl() && isEnabled()){

			

			//Check sensors, inputs, safety
			//Update buttons' current value
			oi.getButtonCurrentValues();

			// System.out.println(chassis.isTalonControlEnabled());

			chassis.driveWithJoysticks(oi.getLeftY(), oi.getRightY());
			// chassis.drive(0.5, 0.5);
			//Hande controller input
			//Button A
			if(oi.buttonA().edgeTrigger()){
				System.out.println("A");
			}
			else{

			}
			//Button B
			if(oi.buttonB().edgeTrigger()){
				System.out.println("B");
			}
			else{
				
			}
			//Button X
			if(oi.buttonX().edgeTrigger()){
				System.out.println("X");
			}
			else{
				
			}
			//Button Y
			if(oi.buttonY().edgeTrigger()){
				System.out.println("Y");
			}
			else{
				
			}
			//Button LB
			if(oi.buttonLB().edgeTrigger()){
				System.out.println("LB");
			}
			else{
				
			}
			//Button RB
			if(oi.buttonRB().edgeTrigger()){
				System.out.println("RB");
			}
			else{
				
			}
			/*
			//Left Trigger 
			if(oi.buttonA.edgeTrigger()){

			}
			else{
				
			}
			//Right Trigger
			if(oi.buttonA.edgeTrigger()){

			}
			else{
				
			}
			*/

			//shooter.shooterOn();
			// shooter.setShooterVoltage(.9);
			//System.out.println(shooter.getShooterSpeed() + " " + shooter.getShooterSetpoint());
			//Go thru robot states
			//Chassis
			//Shooter
			//Gear Arm
			//Rollers
			//Climber

			//Loop ending
			//Update buttons' previous value
			oi.updateButtonPreviousValues();
			
/*
			

	        currVal = Robot.oi.buttonAPressed();
	        curr1Val = Robot.oi.buttonBPressed();

			//GEARARM
			if(currVal == true && aLastVal == false){
				//Robot.geararm.getGear();
				dropGearAndMoveAway();
//				getGearToggle = !getGearToggle;
			}
			else if(curr1Val == true && last1Val == false){
				Robot.geararm.restGear();
			}
			
//			if(getGearToggle = true){
//				Robot.geararm.getGear();
//				getGearToggle = false;
//			}
			//else if(Robot.oi.buttonBPressed()){
				//Robot.geararm.dropGear();
			//}
			else{
				//Robot.geararm.restGear();
			}
			*/
			//MANUAL SHIFTING
			// if(Robot.oi.buttonLTPressed()){
			// 	Robot.chassis.setHighGear();
			// }
			// else if(Robot.oi.buttonRTPressed()){
			// 	Robot.chassis.setLowGear();
			// }
			/*
			//SHOOTER
			/*
			if(Robot.oi.buttonXPressed()){
				shoot();
			}
			else{
				shooterOff();
			}
			*/
			/*
			//SHOOTER REVERSE
//			boolean shouldIReverseShooter = false;
			if(Robot.oi.buttonStartPressed()){
				slowShooterReverse();
//				shouldIReverseShooter = true;
//				timer.start();
//				System.out.println("starting to flush out shooter!!!!");
//			}
//			if(timer.get() < 2000 && shouldIReverseShooter == true){
//				slowShooterReverse();
//				System.out.println("flushing out shooter!");
//			}else if(timer.get() > 2000){
//				System.out.println("done flushing shooter out!");
//				shooterOff();
//				timer.reset();
//				shouldIReverseShooter = false;
//			}
			}
				
			*/	
			//INTAKE
			/*
			if(Robot.oi.buttonLBPressed()){
				//intakeToggle = !intakeToggle;
				Robot.roller.intakeOn();
				if(intakeToggle == true){
					
				}
			}
			else{
				Robot.roller.intakeOff();
			}
			*/
			/*
			//CLIMBER
			/*
			if(Robot.oi.buttonYPressed()){
				Robot.climber.climberOn();
			}
			else {
				Robot.climber.climberOff();
			}
			*/
			/*
			//AUTOSHIFT
			//autoshift should be always on, so this if statement makes it so.
			if(isEnabled()){
				//Robot.chassis.autoShift();
			}
		}
		*/
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	boolean on = false;
	
	private void shoot(){
		//Robot.roller.conveyorOn();
		Robot.shooter.shooterOn();
		if(on == false){
			Timer.delay(2.0);
			Robot.shooter.serializerOn();
			on = true;
		}
	}
	
	private void shooterOff(){
		Robot.shooter.shooterOff();
		//Robot.roller.conveyorOff();
		Robot.shooter.serializerOff();
		on = false;
	}

	private void slowShooterReverse(){
		// this function is to flush out the shooter when there is a ball inside of its chamber
		Robot.shooter.setShooterVoltage(-0.3);
		Robot.shooter.setSerializerSpeed(-0.3);
	}

	private void dropGearAndMoveAway(){
		double angle = 0;

		while(angle <= 0.2){
			//System.out.println(angle);
			Robot.chassis.drive(0.14, 0.14);
			Robot.geararm.setDesiredAngle(angle);
			
			angle+=0.0005;
		}
		Robot.chassis.drive(0.0,0.0);

		while(angle >= 0){
			Robot.chassis.drive(-0.15, -0.15);
			angle-=0.0003;
		}

		Robot.geararm.restGear();
		Robot.chassis.drive(0.0,0.0);
	}
}
