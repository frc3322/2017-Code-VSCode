package frc.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

import static frc.robot.Robot.drivetrain;
import static frc.robot.Robot.xbox;;

public class DriveControl extends Command {

    private final int SPEED_AXIS;
    private final int ROTATION_AXIS;

    public DriveControl(){

        requires(drivetrain);

        SPEED_AXIS = OI.L_YAXIS;
        ROTATION_AXIS = OI.R_XAXIS;

    }

    @Override
    protected void execute() {

        drivetrain.driveClamped(xbox.getFineAxis(OI.L_YAXIS, 3), xbox.getFineAxis(OI.R_XAXIS, 3));

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