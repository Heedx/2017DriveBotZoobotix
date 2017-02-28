package org.usfirst.frc.team6002.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxButton
{
	private boolean prevButtonVal = false;
	private boolean currButtonVal = false;
	JoystickButton button;

	public XboxButton(JoystickButton inputButton)
	{
		button = inputButton;
		prevButtonVal = button.get();

	}

	public void PrevButtonVal(JoystickButton inputButton)
	{
		prevButtonVal = button.get();
	}

	public void setCurrButtonVal(JoystickButton inputButton)
	{
		currButtonVal = button.get();
	}

	public boolean returnCorrectDecision()
	{
		if(prevButtonVal==false && currButtonVal==true)
		{
			return true;
		}
		return false;
	}
	
	
}