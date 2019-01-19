package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystem.Climber.ClimbState;

import static frc.robot.Robot.climber;

public class Climb extends Command{

    public Climb(){

        requires(climber);

    }

    @Override
    protected void initialize() {
        climber.climbStatus = ClimbState.CLIMB;
    }

    @Override
    protected void execute() {
        climber.climb();
    }

    @Override
    protected boolean isFinished() {
        return climber.climbStatus == ClimbState.STOP;
    }

}