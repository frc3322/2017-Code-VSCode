package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystem.Climber.ClimbState;

import static frc.robot.Robot.climber;

public class ForceClimb extends Command{

    public ForceClimb(){

        requires(climber);

    }

    @Override
    protected void initialize() {
        climber.climbStatus = ClimbState.FORCE_CLIMB;
    }

    @Override
    protected void execute() {
        climber.forceClimb();
    }

    @Override
    protected boolean isFinished() {
        //return climber.climbStatus == ClimbState.STOP;
        return false;
    }

}
