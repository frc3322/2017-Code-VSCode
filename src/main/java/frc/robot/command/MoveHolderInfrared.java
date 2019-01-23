package frc.robot.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.subsystem.Climber.ClimbState;
import frc.robot.Robot;
import static frc.robot.Robot.holder;
import static frc.robot.Robot.xbox;

public class MoveHolderInfrared extends Command {
    public MoveHolderInfrared() {

        requires(holder);

    }

    @Override
    protected void execute() {
        Robot.drivetrain.drive(.7, 0);
        if(Robot.infrared.get()){
            holder.toggleExtension();
            System.out.println("Shooter activated");
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}