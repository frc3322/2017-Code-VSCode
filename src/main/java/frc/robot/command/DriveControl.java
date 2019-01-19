package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap;

import static frc.robot.Robot.drivetrain;

public class DriveControl extends Command {

    private final int SPEED_AXIS;
    private final int ROTATION_AXIS;

    public DriveControl(){

        requires(drivetrain);

        SPEED_AXIS = RobotMap.L_YAXIS;
        ROTATION_AXIS = RobotMap.R_XAXIS;

    }

    

    @Override
    protected boolean isFinished() {
        return false;
    }
    
    @Override
    protected void end() {
        drivetrain.drive(0, 0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

}