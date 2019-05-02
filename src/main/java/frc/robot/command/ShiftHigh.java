package frc.robot.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

import static frc.robot.Robot.drivetrain;
import static frc.robot.Robot.xbox;

public class ShiftHigh extends Command {
    public ShiftHigh(){
    }

    @Override
    protected void initialize() {
        drivetrain.shiftHigh();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
