package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.command.DriveControl;

public class Drivetrain extends Subsystem {

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private WPI_TalonSRX drive_left_1, drive_left_2, drive_right_1, drive_right_2, indenturedServantL, indenturedServantR;
    public Encoder enc_left, enc_right;

    private double totalError = 0;

    int numSamples,
            cooldown,
            shiftCounter = 0;
    double robotSpeed,
            lowThreshold,
            highThreshold,
            previousError = 0;
    double previousThrottle = 0,
            previousTurn = 0,
            maxTurnDelta = .05,
            maxThrottleDelta = .05;

    
    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    public static final double DIAMETER_WHEEL = 0.5;

    public Drivetrain(){

        this(6.75, 8.5, 3, 50);

    }

    Drivetrain(double lowThreshold, double highThreshold, int numSamples, int cooldown) {
        this(lowThreshold, highThreshold, numSamples, cooldown, false, false);
    }

    Drivetrain(double lowThreshold, double highThreshold, int numSamples, int cooldown, boolean left_inv, boolean right_inv) {
        drive_left_1 = new WPI_TalonSRX(RobotMap.driveLeft_1);
        drive_left_2 = new WPI_TalonSRX(RobotMap.driveLeft_2);
        drive_right_1 = new WPI_TalonSRX(RobotMap.driveRight_1);
        drive_right_2 = new WPI_TalonSRX(RobotMap.driveRight_2);
        indenturedServantL = new WPI_TalonSRX(RobotMap.indenturedServantL);
        indenturedServantR = new WPI_TalonSRX(RobotMap.indenturedServantR);

        drive_left_1.setInverted(left_inv);
        drive_left_2.setInverted(left_inv);
        indenturedServantL.setInverted(left_inv);
        drive_right_1.setInverted(right_inv);
        drive_right_2.setInverted(right_inv);
        indenturedServantR.setInverted(right_inv);

        // This could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);
        //indenturedServantL.changeControlMode(CANTalon.TalonControlMode.Follower);//setting indentured servants to follow the master talon drive_right || left_1
        //indenturedServantR.changeControlMode(CANTalon.TalonControlMode.Follower);
        //indenturedServantL.set(drive_left_1.getDeviceID());//getting the device ID for the thingies
        //indenturedServantR.set(drive_right_1.getDeviceID());
        indenturedServantL.follow(drive_left_1);
        indenturedServantR.follow(drive_right_1);
        shifter = new DoubleSolenoid(1, RobotMap.shifter_1, RobotMap.shifter_2);
        enc_left = new Encoder(RobotMap.encLeft_1, RobotMap.encLeft_2);
        enc_right = new Encoder(RobotMap.encRight_1, RobotMap.encRight_2);

        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        this.numSamples = numSamples;
        this.cooldown = cooldown;
        leftSamples = new double[numSamples];
        rightSamples = new double[numSamples];
        sampleIndex = 0;

        for (int i = 0; i < numSamples; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }

        SmartDashboard.putNumber("low_gear", lowThreshold);
        SmartDashboard.putNumber("high_gear", highThreshold);
        SmartDashboard.putNumber("num_samples", numSamples);
        SmartDashboard.putNumber("cooldown", cooldown);
    }

    public void drive(double throttle, double turn) {
        drive.arcadeDrive(throttle, turn);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveControl());
    }

    public void driveAngle(double targetAngle, double speed) { // in degrees
        double kp = .00353;
        double ki = 0;
        double kd = .2;

        double angle = Robot.navx.getYaw();
        double error = targetAngle - angle;

        totalError += error;

        double turn = (targetAngle - angle) * kp + totalError * ki - (kd * (error - previousError));

        drive.arcadeDrive(speed, turn);
        previousError = error;
    }

    public void update(){
        SmartDashboard.putNumber("Shift State", shifter.getAll());
    }

    public void driveClosedLoop(double throttle, double turn) {
        double kp = .70;
        double kd = .75;
        double yawRate = Robot.navx.getRate();

        if(Math.abs(turn) < .15){
            turn =  0;
        }
        double error = (yawRate - (turn * 7));
        turn = -turn;
        double RM = (throttle + .4*turn) - ((error * kp) - kd * (error - previousError));
        double LM = (throttle - .4*turn) + ((error * kp) - kd * (error - previousError));

        if (turn == 0 && Math.abs(throttle) < .1) {
            LM = 0;
            RM = 0;
        }

        drive_left_1.set(LM);
        drive_left_2.set(LM);
        drive_right_1.set(-RM);
        drive_right_2.set(-RM);
        previousError = error;
    }

    public void driveClamped(double throttle, double turn) {
        double deltaThrottle = throttle - previousThrottle;
        double deltaTurn = turn - previousTurn;

        // Limit change in throttle value
        // if current change in throttle value exceeds max, clamp it
        if (Math.abs(deltaThrottle) > maxThrottleDelta && (previousThrottle / deltaThrottle) > 0) {
            throttle = previousThrottle + ((deltaThrottle < 0)? -maxThrottleDelta : maxThrottleDelta);
        }

        // Limit change in turn value
        // if current change in turn value exceeds max, clamp it
        if(Math.abs(deltaTurn) > maxTurnDelta && (previousTurn / deltaTurn) > 0){
            turn = previousTurn + ((deltaTurn < 0)? -maxTurnDelta : maxTurnDelta);
        }

        drive(throttle, turn);

        previousThrottle = throttle;
        previousTurn = turn;
    }

    public void updateSpeed() {
        leftSamples[sampleIndex] = wheelFloorSpeed(enc_left);
        rightSamples[sampleIndex++] = wheelFloorSpeed(enc_right);
        if (sampleIndex >= numSamples) {
            sampleIndex = 0;
        }

        double leftAvg = 0.0, rightAvg = 0.0;
        for (double leftSample : leftSamples) {
            leftAvg += Math.abs(leftSample);
        }
        for (double rightSample : rightSamples) {
            rightAvg += Math.abs(rightSample);
        }

        leftAvg /= (double)numSamples;
        rightAvg /= (double)numSamples;

        robotSpeed = Math.max(leftAvg, rightAvg);

        SmartDashboard.putNumber("robot_speed", robotSpeed);
    }

    public void configFromDashboard() {
        highThreshold = SmartDashboard.getNumber("high_gear", 0);
        lowThreshold = SmartDashboard.getNumber("low_gear", 0);
        numSamples = (int)SmartDashboard.getNumber("num_samples", 0);
        cooldown = (int)SmartDashboard.getNumber("shift_threshold", 0);
    }

    public void resetEncDist() {
        enc_left.reset();
        enc_right.reset();
    }

    public double getLeftDisp() { //returns in inches
        return enc_left.getDistance() / 67;
    }

    public double getRightDisp() { //returns in inches
        return enc_right.getDistance() / 67;
    }

    public double getRobotDisp() { //returns in inches
        return Math.max(getLeftDisp(), getRightDisp());
    }

    public double encoderRPS(Encoder e) {
        return e.getRate() / 256.0;
    }

    public double motorRPS(Encoder e) {
        return encoderRPS(e) / (isHigh() ? 1.0588 : 0.4896);
    }

    public double wheelRPS(Encoder e) {
        return encoderRPS(e) / 3.0 * .55;
    }

    public double wheelFloorSpeed(Encoder e) {
        return wheelRPS(e) * Math.PI * DIAMETER_WHEEL;
    }

    public boolean isHigh() { return shifter.get() == DoubleSolenoid.Value.kReverse; }

    public void shiftHigh() {
        shifter.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putString("shift_state", "high");
    }
    public void shiftLow() {
        shifter.set(DoubleSolenoid.Value.kForward);
        SmartDashboard.putString("shift_state", "low");
    }

    public void autoShift() {
        shiftCounter++;

        if (shiftCounter > cooldown) {
            if (robotSpeed > highThreshold) {
                if (!isHigh()) {
                    shiftHigh();
                    shiftCounter = 0;
                }
            } else if (robotSpeed < lowThreshold) {
                if (isHigh()) {
                    shiftLow();
                    shiftCounter = 0;
                }
            }
        }
    }

}