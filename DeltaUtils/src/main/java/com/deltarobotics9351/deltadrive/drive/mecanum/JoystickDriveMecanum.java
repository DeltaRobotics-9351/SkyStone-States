package com.deltarobotics9351.deltadrive.drive.mecanum;

import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

/**
 * Class to control a mecanum chassis during teleop using a gamepad's joysticks.
 */
public class JoystickDriveMecanum {

    //wheel motor power
    public double wheelFrontRightPower = 0;
    public double wheelFrontLeftPower = 0;
    public double wheelBackRightPower = 0;
    public double wheelBackLeftPower = 0;

    public double turbo = 0;

    private DeltaHardwareMecanum hdw;

    /**
     * Constructor for the Joystick Drive
     * @param hdw The initialized hardware containing all the chassis motors
     */
    public JoystickDriveMecanum(DeltaHardwareMecanum hdw){ this.hdw = hdw; }


    /**
     * Control a mecanum chassis using a gamepad's joysticks.
     * Use left stick to go forward, backwards and strafe, and right stick to turn
     * This method should be called always in the teleop loop to update the motor powers
     * @param gamepad the gamepad used to control the chassis.
     * @param turbo the chassis max speed, from 0 to 1
     */
    public void joystick(Gamepad gamepad, double turbo){

        turbo = Math.abs(turbo);
        turbo = Range.clip(Math.abs(turbo), 0, 1);

        this.turbo = turbo;

        double y1 = -gamepad.left_stick_y;
        double x1 = gamepad.left_stick_x;
        double x2 = gamepad.right_stick_x;

        switch(hdw.invert) {

            case RIGHT_SIDE:

                wheelFrontRightPower = -(y1 - x2 - x1);
                wheelBackRightPower = -(y1 - x2 + x1);
                wheelFrontLeftPower = (y1 + x2 + x1);
                wheelBackLeftPower = (y1 + x2 - x1);
                break;

            case LEFT_SIDE:

                wheelFrontRightPower = (y1 - x2 - x1);
                wheelBackRightPower = (y1 - x2 + x1);
                wheelFrontLeftPower = -(y1 + x2 + x1);
                wheelBackLeftPower = -(y1 + x2 - x1);
                break;

            case BOTH_SIDES:

                wheelFrontRightPower = -(y1 - x2 - x1);
                wheelBackRightPower = -(y1 - x2 + x1);
                wheelFrontLeftPower = -(y1 + x2 + x1);
                wheelBackLeftPower = -(y1 + x2 - x1);
                break;

            case NO_INVERT:

                wheelFrontRightPower = (y1 - x2 - x1);
                wheelBackRightPower = (y1 - x2 + x1);
                wheelFrontLeftPower = (y1 + x2 + x1);
                wheelBackLeftPower = (y1 + x2 - x1);
                break;
        }

        double max = Math.max(Math.abs(wheelFrontRightPower), Math.max(Math.abs(wheelBackRightPower),
                Math.max(Math.abs(wheelFrontLeftPower), Math.abs(wheelBackLeftPower))));

        if (max > 1.0)
        {
            wheelFrontRightPower /= max;
            wheelBackRightPower  /= max;
            wheelFrontLeftPower  /= max;
            wheelBackLeftPower   /= max;
        }

        wheelFrontRightPower *= turbo;
        wheelBackRightPower  *= turbo;
        wheelFrontLeftPower  *= turbo;
        wheelBackLeftPower   *= turbo;

        hdw.wheelFrontRight.setPower(wheelFrontRightPower);
        hdw.wheelFrontLeft.setPower(wheelFrontLeftPower);
        hdw.wheelBackRight.setPower(wheelBackRightPower);
        hdw.wheelBackLeft.setPower(wheelBackLeftPower);
    }

}
