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
	private static Climber climber;
	private static Rollers roller;
	private static Shooter shooter;
	private static GearArm geararm;

	private static Timer timer;

	private double startShootTime;
	
	private int autoSelection;

	//Thread visionThread;
	
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
		climber = new Climber();
		roller = new Rollers();
		shooter = new Shooter();
		geararm = new GearArm();

		timer = new Timer();
		timer.start();

		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
	
		startShootTime = 0;
		
		autoSelection = 0;

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
		// chassis.setTalonsToVoltageControl();
		// chassis.drive(0, 0);
		// shooter.disableShooterPID();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
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

		//Get input from the dashboard
		boolean button1 = SmartDashboard.getBoolean("DB/Button 1", false);
		boolean button2 = SmartDashboard.getBoolean("DB/Button 2", false);
		boolean button3 = SmartDashboard.getBoolean("DB/Button 3", false);

		//Do nothing
		if(button1){
			chassis.setTalonsToVoltageControl();
			chassis.enableTalonsControl();
			chassis.drive(0, 0);
		}
		//2 - Drive straight and drop off gyro, 3 - just drive straight
		else if(button2 || button3){
			chassis.resetTalonEncoders();
			chassis.setTalonsToSpeedControl();
			chassis.enableTalonsPIDLoop();

			if(button2){
				chassis.setDrivePIDSetpoint(chassis.convertInchesToTicks(60));
				autoSelection = 1;	
			}
			else{
				chassis.setDrivePIDSetpoint(chassis.convertInchesToTicks(60));
				autoSelection = 2;
			}
			chassis.resetDrivePID();
	        chassis.enableDrivePID();

	        chassis.setTurnPIDSetpoint(chassis.getGyroAngle());
			chassis.resetTurnPID();
	        chassis.enableTurnPID();
		}

		System.out.println("Auto selection: " + autoSelection);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		double targetDriveRPM = 0;
		double targetTurnRPM = 0;
		boolean getTime = true;
		double startTime = 0;

		if(autoSelection == 1){
			//Steps in the autonomous sequence.
			int step = 1;
			
			//Drive to the airship
			if(step == 1){
				targetDriveRPM = chassis.getDrivePIDOutput();
				targetTurnRPM = chassis.getTurnPIDOutput();

				chassis.setLeftDriveSpeed((targetDriveRPM + targetTurnRPM) * Constants.kMaxDriveRPM);
				chassis.setRightDriveSpeed((targetDriveRPM - targetTurnRPM) * Constants.kMaxDriveRPM);

				if(chassis.convertTicksToInches(chassis.getDrivePIDInput()) >= chassis.getDrivePIDSetpoint() - 2){
					//When the robot drive pass its distance setpoint, start a 1s timer.
					if(getTime){
						startTime = timer.get();
						getTime = false;
					}
					//Once 1s pass when the robot reach its distance setpoint, go to the next step.
					//We wait 1s to let the robot "settle" into the setpoint with PID
					if(timer.get() - startTime >= 1){
						step++;
					}
				}
			}
			//Drop gear
			else if(step == 2){
				dropGearAndMoveAway();
			}
			

			System.out.println("L RPM:" + ((targetDriveRPM + targetTurnRPM) * Constants.kMaxDriveRPM) + "R RPM:" + ((targetDriveRPM - targetTurnRPM) * Constants.kMaxDriveRPM) 
			+ " dist:" + chassis.convertTicksToInches(chassis.getDrivePIDInput()) + " onTarget:" + chassis.isDrivePIDOnTarget());
		}
		else if(autoSelection == 2){
			targetDriveRPM = chassis.getDrivePIDOutput();
			targetTurnRPM = chassis.getTurnPIDOutput();

			chassis.setLeftDriveSpeed((targetDriveRPM + targetTurnRPM) * Constants.kMaxDriveRPM);
			chassis.setRightDriveSpeed((targetDriveRPM - targetTurnRPM) * Constants.kMaxDriveRPM);

			System.out.println("L RPM:" + ((targetDriveRPM + targetTurnRPM) * Constants.kMaxDriveRPM) + "R RPM:" + ((targetDriveRPM - targetTurnRPM) * Constants.kMaxDriveRPM) 
			+ " dist:" + chassis.convertTicksToInches(chassis.getDrivePIDInput()) + " onTarget:" + chassis.isDrivePIDOnTarget());
		}
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
		//shooter.enableShooterPID();

		chassis.compressorOn();
	}
	
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

			//Hande controller input
			//Button A
			if(oi.buttonA().edgeTrigger()){
				System.out.println("A");
				climber.switchToggle();
			}

			//Button B
			if(oi.buttonB().edgeTrigger()){
				System.out.println("B");
				geararm.switchToggle();
			}

			//Button X
			if(oi.buttonX().edgeTrigger()){
				System.out.println("X");
				geararm.switchGearToggle();
			}

			//Button Y
			if(oi.buttonY().edgeTrigger()){
				System.out.println("Y");
				roller.switchIntakeToggle();
				//Make sure reversing is off
				roller.setReverseToggle(false);
			}

			//Button LB
			if(oi.buttonLB().edgeTrigger()){
				System.out.println("LB");
				chassis.switchShiftToggle();
			}

			//Button RB
			if(oi.buttonRB().edgeTrigger()){
				System.out.println("RB");
				shooter.switchToggle();

				//If going to shoot, get the time that it is activing. this is used for the serializer timing
				if(shooter.getToggle()){
					startShootTime = timer.get();
				}
			}

			if(oi.buttonStart().edgeTrigger()){
				System.out.println("Start");
				roller.switchReverseToggle();
				//Make sure intaking is off
				roller.setIntakeToggle(false);
			}

			//Left Trigger 
			if(oi.triggerLT().edgeTrigger()){
				System.out.println("LT");
			}

			//Right Trigger
			if(oi.triggerRT().edgeTrigger()){
				System.out.println("RT");
			}

			
			//Handle the climber output
			if(climber.getToggle()){
				climber.climberOn();
			}
			else{
				climber.climberOff();
			}
			
			//Handle gear arm output
			if(geararm.getGearToggle()){
				geararm.getGear();
			}
			else if(geararm.getInboundToggle()){
				geararm.inboundGear();
			}
			else{
				geararm.homeGear();
			}
			
			//Handle intake output
			if(roller.getIntakeToggle()){
				roller.intakeOn();
			}
			else if(roller.getReverseToggle()){
				roller.reverse();
			}
			else{
				roller.intakeOff();
			}
			
			//Handle shooter
			if(shooter.getToggle()){
				shoot();
			}
			else{
				shooterOff();
			}
			
			//shifting
			if(chassis.getShiftToggle()){
				chassis.setHighGear(); 
			}
			else{
				chassis.setLowGear();
			}
			/*
			//MANUAL SHIFTING
			if(Robot.oi.buttonLTPressed()==true){
				Robot.chassis.setHighGear();
			}
			else if(Robot.oi.buttonRTPressed()==true){
				Robot.chassis.setLowGear();
			}
			*/

			//Handle drive output
			chassis.driveWithJoysticks(oi.getLeftY(), oi.getRightY());

			//Loop ending
			//Update buttons' previous value
			oi.updateButtonPreviousValues();
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	//boolean on = false;
	
	private void shoot(){
		Robot.roller.conveyorOn();
		Robot.shooter.shooterOn();

		//Start the serializer after 2s when the shooter is activated
		if(timer.get() - startShootTime >= 2){
			Robot.shooter.serializerOn();
		}

		/*
		if(on == false){
			Timer.delay(2.0);
			Robot.shooter.serializerOn();
			on = true;
		}
		*/
	}
	
	private void shooterOff(){
		Robot.shooter.shooterOff();
		Robot.roller.conveyorOff();
		Robot.shooter.serializerOff();
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
			Robot.chassis.drive(0.2, 0.2);
			Robot.geararm.setDesiredAngle(angle);
			
			angle+=0.0005;
		}
		Robot.geararm.setGearToggle(true);
		Robot.chassis.drive(0.0,0.0);
	}
}
