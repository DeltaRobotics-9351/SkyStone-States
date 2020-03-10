/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum;

import com.deltarobotics9351.LibraryData;
import com.deltarobotics9351.deltadrive.drive.mecanum.IMUDrivePIDMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.TimeDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.extendable.linearopmodes.ExtendableLinearOpMode;
import com.deltarobotics9351.deltadrive.parameters.IMUDriveParameters;
import com.deltarobotics9351.deltadrive.utils.Invert;
import com.deltarobotics9351.deltadrive.utils.RobotHeading;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.deltamath.geometry.Twist2d;
import com.deltarobotics9351.pid.PIDCoefficients;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Remember to override setup() and define the 4 DcMotor variables in there!
 */
public class IMUPIDTimeMecanumLinearOpMode extends ExtendableLinearOpMode {

    private IMUDrivePIDMecanum imuDrive;

    private TimeDriveMecanum timeDrive;

    /**
     * IMU parameters that can be defined
     */
    public IMUDriveParameters imuParameters = new IMUDriveParameters();

    @Override
    public final void runOpMode() {
        setup();

        if(RESET_ROBOT_HEADING){
            RobotHeading.reset();
        }

        if(UPDATE_ROBOT_HEADING){
            RobotHeading.start(ROBOT_INITIAL_HEADING, hardwareMap);
        }

        if(frontLeft == null || frontRight == null || backLeft == null || backRight == null){
            telemetry.addData("[/!\\]", "OpMode will not start in order to avoid Robot Controller crash.");
            telemetry.addData("frontLeft", (frontLeft == null) ? "is null" : "OK");
            telemetry.addData("frontRight", (frontRight == null) ? "is null" : "OK");
            telemetry.addData("backLeft", (backLeft == null) ? "is null" : "OK");
            telemetry.addData("backRight", (backRight == null) ? "is null" : "OK");
            telemetry.addData("POSSIBLE SOLUTION 1", "Override setup() method in your OpMode class and\ndefine the null motor variables specified above.");
            telemetry.addData("POSSIBLE SOLUTION 2", "Check that all your motors are correctly named and\nthat they are get from the hardwareMap");
            telemetry.update();
            while(opModeIsActive());
            return;
        }

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, Invert.RIGHT_SIDE);

        deltaHardware.initHardware(frontLeft, frontRight, backLeft, backRight, WHEELS_BRAKE);

        imuDrive = new IMUDrivePIDMecanum(deltaHardware, telemetry);
        imuDrive.initIMU(imuParameters);

        timeDrive = new TimeDriveMecanum(deltaHardware, telemetry);

        while(!imuDrive.isIMUCalibrated() && !isStopRequested()){
            telemetry.addData("[/!\\]", "Calibrating IMU Gyro sensor, please wait...");
            telemetry.addData("[Status]", imuDrive.getIMUCalibrationStatus() + "\n\nDeltaUtils v" + LibraryData.VERSION);
            telemetry.update();
        }

        _runOpMode();

        RobotHeading.stop();
    }

    public final void forward(double power, double timeSecs){
        timeDrive.forward(power, timeSecs);
    }

    public final void backwards(double power, double timeSecs){
        timeDrive.backwards(power, timeSecs);
    }

    public final void strafeLeft(double power, double timeSecs){
        timeDrive.strafeLeft(power, timeSecs);
    }

    public final void strafeRight(double power, double timeSecs){
        timeDrive.strafeRight(power, timeSecs);
    }

    public final void turnLeft(double power, double timeSecs){
        timeDrive.turnLeft(power, timeSecs);
    }

    public final void turnRight(double power, double timeSecs){
        timeDrive.strafeRight(power, timeSecs);
    }


    /**
     * The side of the chassis which has its motors inverted
     * @param invert the wheels invert enum
     */
    public final void setWheelsInvert(Invert invert){
        deltaHardware.invert = invert;
    }

    /**
     * Overridable void to be executed after all required variables are initialized
     */
    @Override
    public void _runOpMode(){

    }

    /**
     * Overridable void to define all wheel motors, and the uppercase variables
     * Define frontLeft, frontRight, backLeft and backRight DcMotor variables here!
     */
    @Override
    public void setup(){

    }

    /**
     * Set the PID coefficients
     * @param pid the PID coefficients
     */
    public final void setPID(PIDCoefficients pid){
        imuDrive.setPID(pid);
    }

    /**
     * @return the P coefficient
     */
    public final double getP(){
        return imuDrive.getP();
    }

    /**
     * @return the I coefficient
     */
    public final double getI(){
        return imuDrive.getI();
    }

    /**
     * @return the D coefficient
     */
    public final double getD(){
        return imuDrive.getD();
    }

    /**
     * @return the current PIDCoefficients object
     */
    public final PIDCoefficients getPID(){ return imuDrive.getPID(); }

    public final Twist2d rotate(Rot2d rot, double power, double timeoutSecs){
        return imuDrive.rotate(rot, power, timeoutSecs);
    }

    public final Rot2d getRobotAngle(){
        return imuDrive.getRobotAngle();
    }

}
