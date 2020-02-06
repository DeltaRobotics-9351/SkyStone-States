package com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum;

import com.github.deltarobotics9351.deltadrive.drive.mecanum.IMUDriveMecanum;
import com.github.deltarobotics9351.deltadrive.drive.mecanum.TimeDriveMecanum;
import com.github.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.github.deltarobotics9351.deltadrive.parameters.IMUDriveParameters;
import com.github.deltarobotics9351.deltadrive.utils.Invert;
import com.github.deltarobotics9351.deltamath.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class IMUTimeMecanumLinearOpMode extends LinearOpMode {

    private IMUDriveMecanum imuDrive;
    private TimeDriveMecanum timeDrive;

    private DeltaHardwareMecanum deltaHardware;

    public IMUDriveParameters imuParameters = new IMUDriveParameters();

    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public Invert WHEELS_INVERT = Invert.RIGHT_SIDE;

    public boolean WHEELS_BRAKE = true;

    @Override
    public final void runOpMode() {
        defineHardware();

        if(frontLeft == null || frontRight == null || backLeft == null || backRight == null){
            telemetry.addData("[/!\\]", "OpMode will not start in order to avoid Robot Controller crash.");
            telemetry.addData("frontLeft", (frontLeft == null) ? "is null" : "OK");
            telemetry.addData("frontRight", (frontRight == null) ? "is null" : "OK");
            telemetry.addData("backLeft", (backLeft == null) ? "is null" : "OK");
            telemetry.addData("backRight", (backRight == null) ? "is null" : "OK");
            telemetry.addData("POSSIBLE SOLUTION 1", "Override defineHardware() method in your OpMode class and\ndefine the null motor variables specified above.");
            telemetry.addData("POSSIBLE SOLUTION 2", "Check that all your motors are correctly named and\nthat they are get from the hardwareMap");
            telemetry.update();
            while(opModeIsActive());
        }

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, WHEELS_INVERT);

        deltaHardware.initHardware(frontLeft, frontRight, backLeft, backRight, WHEELS_BRAKE);

        imuDrive = new IMUDriveMecanum(deltaHardware, this);
        imuDrive.initIMU(imuParameters);

        while(!imuDrive.isIMUCalibrated() && !isStopRequested()){
            telemetry.addData("[/!\\]", "Calibrating IMU Gyro sensor, please wait...");
            telemetry.addData("[Status]", imuDrive.getIMUCalibrationStatus());
            telemetry.update();
        }

        timeDrive = new TimeDriveMecanum(deltaHardware, telemetry);

        Thread t = new Thread(new ParametersCheck());

        t.start();

        _runOpMode();
    }


    public void _runOpMode(){

    }

    public void defineHardware(){

    }

    public final void rotate(Rotation2d rot, double power, double timeoutS){
        imuDrive.rotate(rot, power, timeoutS);
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
