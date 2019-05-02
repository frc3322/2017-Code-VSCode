package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.command.StopClimber;

public class Climber extends Subsystem{
    WPI_TalonSRX climb_talon_1, climb_talon_2;
    public ClimbState climbStatus;

    // value from 0.00 to 1.00
    double climbRate = 1.0,
		    totalCurrent = 0,
            avgCurrent = 0;
    double[] current;
    int timer = 0,
            i = 0;

    public enum ClimbState {
        STOP,
        CLIMB,
        FORCE_CLIMB
    }

    public Climber() {
        climb_talon_1 = new WPI_TalonSRX(RobotMap.climbTalon_1);
        climb_talon_2 = new WPI_TalonSRX(RobotMap.climbTalon_2);
        current = new double[] {0,0,0,0,0};
    }

    public void climb(){

        avgCurrent();

        Robot.holder.retract();

        // Climbing with current threshold
        if (avgCurrent < 50) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        } else {
            stop();
        }

    }

    public void forceClimb(){
        
        Robot.holder.retract();

        climb_talon_1.set(climbRate);
        climb_talon_2.set(climbRate);

    }

    public void stop(){
        
        climbStatus = ClimbState.STOP;
        
        climb_talon_1.set(0);
        climb_talon_2.set(0);

    }

    private void avgCurrent() {
        i = 0;
        current[i] = (climb_talon_1.getOutputCurrent() + climb_talon_2.getOutputCurrent()) / 2;
        if (i < 4) {
            i++;
        } else {
            i = 0;
        }
        totalCurrent = 0;
        for (double amps : current) {
            totalCurrent += amps;
        }
        avgCurrent = totalCurrent / 5.0;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new StopClimber());
    }
}