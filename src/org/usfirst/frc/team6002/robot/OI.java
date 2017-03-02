package org.usfirst.frc.team6002.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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
	private Joystick controller;
	private ArrayList<XboxButton> buttons;
	private ArrayList<XboxTrigger> triggers;

	public OI(){
		controller = new Joystick(0);
		buttons = new ArrayList<XboxButton>();
		//Set up buttons with edge trigger
		initializeButtons();
	}

	/*
	BUTTON INDEXING:
	1 - A
	2 - B
	3 - X
	4 - Y
	5 - LB
	6 - RB
	*/

	//Add the buttons into the array list
	public void initializeButtons(){
		//7 buttons
		for(int i = 1; i <= 7; i++){
			// JoystickButton button = new JoystickButton(controller, i);
			// XboxButton xButton = new XboxButton(button);
			buttons.add(new XboxButton(new JoystickButton(controller, i)));
		}
	}

	public void initializeTriggers(){
		//Left Trigger
		triggers.add(new XboxTrigger(controller, 2));	

		//Right Trigger
		triggers.add(new XboxTrigger(controller, 3));
	}

	public void getTriggerCurrentValues(){
		for(XboxTrigger trig : triggers){
			trig.updateCurrentValue();
		}
	}

	public void updateTriggerPreviousValues(){
		for(XboxTrigger trig : triggers){
			trig.updatePreviousValue();
		}
	}

	public void getButtonCurrentValues(){
		for(XboxButton button : buttons){
			button.updateCurrentValue();
		}
	}

	public void updateButtonPreviousValues(){
		for(XboxButton button : buttons){
			button.updatePreviousValue();
		}
	}

	public double getLeftY(){
		return controller.getRawAxis(1);
	}
	public double getRightY() {
		return controller.getRawAxis(5);
	}

	public XboxButton buttonA(){
		return buttons.get(0);
	}
	public XboxButton buttonB(){
		return buttons.get(1);
	}
	public XboxButton buttonX(){
		return buttons.get(2);
	}
	public XboxButton buttonY(){
		return buttons.get(3);
	}
	public XboxButton buttonLB(){
		return buttons.get(4);
	}
	public XboxButton buttonRB(){
		return buttons.get(5);
	}
	public XboxButton buttonStart(){
		return buttons.get(6);
	}

	public XboxTrigger triggerLT(){
		return triggers.get(0);
	}
	public XboxTrigger triggerRT(){
		return triggers.get(1);
	}

	public boolean buttonStartPressed(){
		return controller.getRawButton(8);
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
