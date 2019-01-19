package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystem.Climber.ClimbState;

import static frc.robot.Robot.climber;

public class StopClimber extends Command {

    public StopClimber() {

        requires(climber);

    }

    @Override
    protected void initialize() {
        climber.stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}