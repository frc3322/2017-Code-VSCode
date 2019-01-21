/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.command;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.LimeLight;

/**
 * Add your docs here.
 */
public class LimeAlign extends Command{

    private double turnConstant = .05;

    public void LimeAlign() {
        requires(Robot.limelight);
        requires(Robot.drivetrain);
    }

    @Override
    protected void execute() {
        Robot.drivetrain.driveClamped(OI.joystick.getRawAxis(1), LimeLight.getTx() * turnConstant);
    }

    @Override
    protected void end() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
