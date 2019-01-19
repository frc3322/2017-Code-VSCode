package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;

import static frc.robot.Robot.drivetrain;

public class AutoShift extends Command{

    public AutoShift(){

        requires(drivetrain);

    }

    @Override
    protected void execute() {
        drivetrain.autoShift();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

}