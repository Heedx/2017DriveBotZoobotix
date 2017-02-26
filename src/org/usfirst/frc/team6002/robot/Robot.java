
package org.usfirst.frc.team6002.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6002.robot.commands.ExampleCommand;
import org.usfirst.frc.team6002.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team6002.robot.subsystems.Drive;
import org.usfirst.frc.team6002.robot.subsystems.Climber;
import org.usfirst.frc.team6002.robot.subsystems.Rollers;
import org.usfirst.frc.team6002.robot.subsystems.Shooter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import org.usfirst.frc.team6002.robot.subsystems.GearArm;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	public static Drive chassis;
	public static Climber climber;
	public static Rollers roller;
	public static Shooter shooter;
	public static GearArm geararm;
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	CANTalon Test = new CANTalon(6);
	
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
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);

   	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

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
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		Robot.chassis.motorInit();
		Robot.chassis.compressorOn();
		Robot.climber.climberInit();
		Robot.roller.rollerInit();
		Robot.shooter.shooterInitialize();
		Robot.geararm.gearArmInit();
	}

	int counter = 0;
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		while(isOperatorControl() && isEnabled()){
				Robot.chassis.drive(Robot.oi.getLeftY(), Robot.oi.getRightY());
	        
			
			//GEARARM
			if(Robot.oi.buttonAPressed()){
				Robot.geararm.getGear();
			}
			else if(Robot.oi.buttonBPressed()){
				Robot.geararm.dropGear();
			}
			else{
				Robot.geararm.restGear();
			}
			
			//MANUAL SHIFTING
			if(Robot.oi.buttonLTPressed()){
				Robot.chassis.setHighGear();
			}
			else if(Robot.oi.buttonRTPressed()){
				Robot.chassis.setLowGear();
			}
			
			//SHOOTER
			if(Robot.oi.buttonXPressed()){
				shoot();
			}
			else{
				shooterOff();
			}
				
			//INTAKE
			boolean intakeToggle = false;
			if(Robot.oi.buttonLBPressed()){
				//counter = counter + 1;
				intakeToggle = !intakeToggle;
				if(intakeToggle){
					Robot.roller.intakeOn();
				}
				else{
					Robot.roller.intakeOff();
				}
			}
			
			//CLIMBER
			if(Robot.oi.buttonYPressed()){
				Robot.climber.climberOn();
			}
			else {
				Robot.climber.climberOff();
			}
			//AUTOSHIFT
			//autoshift should be always on, so this if statement makes it so.
			if(isEnabled()){
				Robot.chassis.autoShift();
			}
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	private void shoot(){
		Robot.roller.conveyorOn();
		Robot.shooter.shooterOn();
		Robot.shooter.serializerOn();
	}
	
	private void shooterOff(){
		Robot.shooter.shooterOff();
		Robot.roller.conveyorOff();
		Robot.shooter.serializerOff();
	}
}
