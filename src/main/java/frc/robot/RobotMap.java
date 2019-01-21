package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static final int
        // Talons
        driveLeft_1 = 3,
        driveLeft_2 = 2,
        indenturedServantL = 1,
        driveRight_1 = 6,
        driveRight_2 = 5,
        indenturedServantR = 4,

        climbTalon_1 = 1,
        climbTalon_2 = 2,

        // Solenoids
        shifter_1 = 3,
        shifter_2 = 2,

        gearHolder_1 = 0,
        gearHolder_2 = 1,

        // DIGITAL INPUT
        encLeft_1 = 0,
        encLeft_2 = 1,
        encRight_1 = 2,
        encRight_2 = 3,

        gearSensor = 4;

}
