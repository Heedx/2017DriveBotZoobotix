package org.usfirst.frc.team6002.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DummyPIDOutput implements PIDOutput{
	private double output;

	public DummyPIDOutput() {
		output = 0;
	}

    public void pidWrite(double output) {
        this.output = output;
    }

    public double getOutput() {
        return output;
    }
}