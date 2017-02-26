package org.usfirst.frc.team6002.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

import org.usfirst.frc.team6002.robot.commands.ExampleCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);
	public Joystick xbox = new Joystick(1);
	
	public double getLeftY(){
		return xbox.getRawAxis(1);
	}
	public double getRightY() {
		return xbox.getRawAxis(5);
	}
	//WWHAT BUTTON AM I PRESIN!!?!
	public boolean buttonAPressed(){
		return Robot.oi.xbox.getRawButton(1);
	}
	public boolean buttonBPressed(){
		return Robot.oi.xbox.getRawButton(2);
	}
	public boolean buttonXPressed(){
		return Robot.oi.xbox.getRawButton(3);
	}
	public boolean buttonYPressed(){
		return Robot.oi.xbox.getRawButton(4);
	}
	public boolean buttonLBPressed(){
		return Robot.oi.xbox.getRawButton(5);
	}
	public boolean buttonRBPressed(){
		return Robot.oi.xbox.getRawButton(6);
	}
	public boolean buttonLTPressed(){
		return (Robot.oi.xbox.getRawAxis(3) == 1);
	}
	public boolean buttonRTPressed(){
		return (Robot.oi.xbox.getRawAxis(2) == 1);
	}
//	public boolean buttonBPressed = Robot.oi.xbox.getRawButton(2);
//	public boolean buttonXPressed = Robot.oi.xbox.getRawButton(3);
//	public boolean buttonYPressed = Robot.oi.xbox.getRawButton(4);
//	public boolean buttonLBPressed = Robot.oi.xbox.getRawButton(5);
//	public boolean buttonRBPressed = Robot.oi.xbox.getRawButton(6);
//	public boolean buttonLTPressed = (Robot.oi.xbox.getRawAxis(3) == 1);
//	public boolean buttonRTPressed = (Robot.oi.xbox.getRawAxis(2) == 1);
	
	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}
