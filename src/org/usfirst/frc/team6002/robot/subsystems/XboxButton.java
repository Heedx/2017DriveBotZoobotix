package org.usfirst.frc.team6002.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxButton()
{
	private bool PrevButtonVal = false, CurrButtonVal = false;
	JoystickButton button;

	public XboxButton(JoystickButton inputButton)
	{
		button = inputButton;
		PrevButtonVal = button.get();

	}

	public void PrevButtonVal(JoystickButton inputButton)
	{
		PrevButtonVal = button.get();
	}

	public void setCurrButtonVal(JoystickButton inputButton)
	{
		CurrButtonVal = button.get();
	}

	public bool returnCorrectDecision()
	{
		if(PrevButtonVal==false && CurrButtonVal==true)
		{
			return true;
		}
		return false;
	}
	
	
}