package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.command.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.*;

import java.nio.charset.StandardCharsets;


public class OI {
    static Joystick joystick = new Joystick(0);

    public static final int
            // Buttons
            ABUTTON = 1,
            BBUTTON = 2,
            XBUTTON = 3,
            YBUTTON = 4,
            LBUMPER = 5,
            RBUMPER = 6,
            BACK = 7,
            START = 8,
            LSTICK = 9,
            RSTICK = 10,
            DPADVERT = 7, // not reliable
            DPADHORIZ = 6,

            // Stick axes
            L_YAXIS = 1,
            L_XAXIS = 2,
            R_YAXIS = 3,
            R_XAXIS = 4;

    private Button buttonA = new JoystickButton(joystick, ABUTTON);
    private Button buttonB = new JoystickButton(joystick, BBUTTON);
    private Button rightBumper = new JoystickButton(joystick, RBUMPER);
    private Button leftBumper = new JoystickButton(joystick, LBUMPER);
    private Button buttonY = new JoystickButton(joystick, YBUTTON);
    private Button startButton = new JoystickButton(joystick, START);
    private Button backButton = new JoystickButton(joystick, BACK);

    int buttonState, toggleState;

    public OI() {
        buttonA.whenPressed(new Climb());

        buttonB.whenPressed(new StopClimber());

        rightBumper.whenPressed(new MoveHolder());

        leftBumper.whenPressed(new ForceClimb());
        leftBumper.whenReleased(new StopClimber());

        startButton.whenPressed(new ShiftHigh());

        backButton.whenPressed(new ShiftLow());
    }

	// Track button states for pressedOnce
    private void setButtonDown(int button) {
        // Set binary value of button to (0 or 1) or (1 or 1) - which means down
        buttonState |= (1 << button);
    }
    private void setButtonUp(int button) {
        // Set binary value of button to (0 and 0) or (1 and 0) - which means up
        // NOTE: ~ represents the opposite binary integer
        buttonState &= ~(1 << button);
    }
    private boolean isDown(int button) {
        return 0 != (buttonState & (1 << button));
    }

    public void toggleButtonState(int button) {
        toggleState ^= (1 << button);
    }

    public void setToggled(int button, boolean value) {
        if (value) {
            // Toggle down
            toggleState |= (1 << button);
        } else {
            // Toggle up
            toggleState &= ~(1 << button);
        }
    }

    public boolean isToggled(int button) {
        if (pressedOnce(button)) {
            toggleButtonState(button);
        }
        return 0 != (toggleState & (1 << button));
    }

    public boolean heldDown(int button) {
        return joystick.getRawButton(button);
    }
    public boolean pressedOnce(int button) {
        // Returns true only once, and will not return true again until the button is released and pressed again.
        if (heldDown(button)) {
            if (!isDown(button)) {
                setButtonDown(button);
                return true;
            } else {
                return false;
            }
        } else {
            setButtonUp(button);
            return false;
        }
    }

	public double getAxis(int axis) {
            return joystick.getRawAxis(axis);
    }
    public double getFineAxis(int axis, double pow) {
        return Math.abs(Math.pow(joystick.getRawAxis(axis), pow - 1)) * joystick.getRawAxis(axis);
    }

    public void setVibrate(double left, double right) {
        joystick.setRumble(GenericHID.RumbleType.kLeftRumble, left);
        joystick.setRumble(GenericHID.RumbleType.kRightRumble, right);
    }

    public static void LEDWrite(String data) {
        //byte[] writeData = data.getBytes(StandardCharsets.UTF_8);
        //Robot.LEDs.transaction(writeData, writeData.length, null, 0);
    }
}
