package frc.robot.command;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.subsystem.Climber.ClimbState;

import static frc.robot.Robot.holder;
import static frc.robot.Robot.xbox;

public class MoveHolder extends Command {
    public MoveHolder() {

        requires(holder);

    }

    @Override
    protected void initialize() {
        if (!holder.isExtended()) {
            holder.extend();
        } else {
            holder.retract();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
