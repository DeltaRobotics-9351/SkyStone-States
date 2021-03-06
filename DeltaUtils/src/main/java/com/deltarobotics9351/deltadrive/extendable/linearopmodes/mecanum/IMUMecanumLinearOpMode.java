/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum;

import com.deltarobotics9351.LibraryData;
import com.deltarobotics9351.deltadrive.drive.mecanum.IMUDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.extendable.linearopmodes.ExtendableLinearOpMode;
import com.deltarobotics9351.deltadrive.parameters.IMUDriveParameters;
import com.deltarobotics9351.deltadrive.utils.Invert;
import com.deltarobotics9351.deltadrive.utils.RobotHeading;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.deltamath.geometry.Twist2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Remember to override setup() and define the 4 DcMotor variables in there!
 */
public class IMUMecanumLinearOpMode extends ExtendableLinearOpMode {

    private IMUDriveMecanum imuDrive;

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

        imuDrive = new IMUDriveMecanum(deltaHardware, telemetry);
        imuDrive.initIMU(imuParameters);

        while(!imuDrive.isIMUCalibrated() && !isStopRequested()){
            telemetry.addData("[/!\\]", "Calibrating IMU Gyro sensor, please wait...");
            telemetry.addData("[Status]", imuDrive.getIMUCalibrationStatus() + "\n\nDeltaUtils v" + LibraryData.VERSION);
            telemetry.update();
        }

        Thread t = new Thread(new ParametersCheck());

        t.start();

        _runOpMode();

        RobotHeading.stop();
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

    public final Twist2d rotate(Rot2d rot, double power, double timeoutS){
        return imuDrive.rotate(rot, power, timeoutS);
    }

    /**
     * The side of the chassis which has its motors inverted
     * @param invert the wheels invert enum
     */
    public final void setWheelsInvert(Invert invert){
        deltaHardware.invert = invert;
    }

    public final Rot2d getRobotAngle(){
        return imuDrive.getRobotAngle();
    }

    class ParametersCheck implements Runnable{

        @Override
        public void run(){
            waitForStart();
            if(!imuParameters.haveBeenDefined()){
                telemetry.addData("[/!\\]", "Remember to define IMU constants, IMU functions may not work as expected because parameters are 0 by default.");
            }
            telemetry.update();
        }
    }

}
