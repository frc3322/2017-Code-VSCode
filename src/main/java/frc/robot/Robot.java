package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystem.*;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot {
    public static OI xbox;
    public static Drivetrain drivetrain;
    public static Climber climber;
    public static Holder holder;
    public static AHRS navx;
    public static DigitalInput infrared = new DigitalInput(RobotMap.infrared);
    //static Auton auton;
    static Compressor compressor;
    static I2C LEDs = new I2C(I2C.Port.kOnboard, 4);

    @Override
    public void robotInit() {
        // Subsystem init

        drivetrain = new Drivetrain();
        holder = new Holder();
        climber = new Climber();
        xbox = new OI();
        //auton = new Auton();

        // Component init
        compressor = new Compressor(1);
        navx = new AHRS(SerialPort.Port.kMXP);

        SmartDashboard.putNumber("auton", 1);
        OI.LEDWrite("RobotInit");
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        Robot.xbox.setVibrate(0, 0);

        SmartDashboard.putNumber("auton", 1);
        SmartDashboard.putBoolean("enabled", false);
        OI.LEDWrite("DisabledInit");
    }

    @Override
    public void teleopInit() {
        SmartDashboard.putNumber("teleop", 0);
        SmartDashboard.putNumber("auton", 0);
        SmartDashboard.putBoolean("enabled", true);
        OI.LEDWrite("TeleopInit");
    }

    @Override
    public void robotPeriodic() {
        drivetrain.updateSpeed();
        drivetrain.update();
        SmartDashboard.putNumber("left_vel", drivetrain.wheelFloorSpeed(drivetrain.enc_left));
        SmartDashboard.putNumber("right_vel", drivetrain.wheelFloorSpeed(drivetrain.enc_right));
        SmartDashboard.putNumber("left_disp", drivetrain.getLeftDisp());
        SmartDashboard.putNumber("right_disp", drivetrain.getRightDisp());
        SmartDashboard.putNumber("yaw", navx.getYaw());
        SmartDashboard.putNumber("vel_x", navx.getVelocityX());
        SmartDashboard.putNumber("vel_y", navx.getVelocityY());
        SmartDashboard.putNumber("vel_z", navx.getVelocityZ());
        SmartDashboard.putBoolean("Infrared Value", infrared.get());
    }

    @Override
    public void disabledPeriodic() {
        //auton.init();

        OI.LEDWrite("DisabledPeriodic");
    }
    @Override
    public void autonomousInit() {
        //auton.init();

        SmartDashboard.putNumber("auton", 2);
        SmartDashboard.putBoolean("enabled", true);
        OI.LEDWrite("AutonInit");
    }

    @Override
    public void autonomousPeriodic() {
        //auton.run();

        SmartDashboard.putNumber("auton", 1);
        OI.LEDWrite("AutonPeriodic");
    }

    @Override
    public void teleopPeriodic() {

        Scheduler.getInstance().run();

        /*drivetrain.driveClamped(xbox.getFineAxis(OI.L_YAXIS, 3), xbox.getFineAxis(OI.R_XAXIS, 3));
        drivetrain.autoShift();

        climber.climb(OI.ABUTTON, OI.LBUMPER);

        if (xbox.isToggled(OI.RBUMPER)) {
            holder.extend();
        } else {
            holder.retract();
        }

        if (xbox.isToggled(OI.LSTICK)) {
            drivetrain.shiftHigh();
        } else {
            drivetrain.shiftLow();
        }

        // Vibrate controller based on motor current and sensor state
        if (climber.climbStatus != Climber.ClimbState.STOP) {
            xbox.setVibrate(climber.avgCurrent * .02, climber.avgCurrent * .02);
        } else */
        if (!Robot.holder.gearSensor.get()) {
            SmartDashboard.putBoolean("gear_sensor", true);
            xbox.setVibrate(0.5,0.5);
        } else {
            SmartDashboard.putBoolean("gear_sensor", false);
            xbox.setVibrate(0, 0);
        }
        /*
        if (climber.climbStatus == Climber.ClimbState.CLIMB) {
            OI.LEDWrite("Climbing");
        } else if (holder.extended) {
            OI.LEDWrite("HolderForward");
        } else {
            OI.LEDWrite("HolderBack");
        }*/

    }
}