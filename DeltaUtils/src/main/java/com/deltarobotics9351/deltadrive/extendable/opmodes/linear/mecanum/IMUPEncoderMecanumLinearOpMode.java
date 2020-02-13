package com.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum;

import com.deltarobotics9351.deltadrive.drive.mecanum.EncoderDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.IMUDrivePMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.parameters.EncoderDriveParameters;
import com.deltarobotics9351.deltadrive.utils.Invert;
import com.deltarobotics9351.deltadrive.utils.RobotHeading;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Remember to override setup() and define the 4 DcMotor variables in there!
 */
public class IMUPEncoderMecanumLinearOpMode extends LinearOpMode {

    private IMUDrivePMecanum imuDrive;

    private EncoderDriveMecanum encoderDrive;

    private DeltaHardwareMecanum deltaHardware;

    /**
     * Encoder parameters that can be defined
     */
    public EncoderDriveParameters encoderParameters = new EncoderDriveParameters();

    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    /**
     * Enum that defines which side of the chassis will be inverted (motors)
     */
    public Invert WHEELS_INVERT = Invert.RIGHT_SIDE;

    /**
     * boolean that defines if motors brake when their power is 0
     */
    public boolean WHEELS_BRAKE = true;

    /**
     * boolean that indicates if we'll update the RobotHeading in this OpMode
     * You need 2 Expansion Hubs for this!
     */
    public boolean UPDATE_ROBOT_HEADING = false;

    /**
     * boolean that indicates if we'll reset the RobotHeading
     */
    public boolean RESET_ROBOT_HEADING = false;

    /**
     * Robot's initial heading
     *
     */
    public Rot2d ROBOT_INITIAL_HEADING = new Rot2d();

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

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, WHEELS_INVERT);

        deltaHardware.initHardware(frontLeft, frontRight, backLeft, backRight, WHEELS_BRAKE);

        imuDrive = new IMUDrivePMecanum(deltaHardware, this);
        imuDrive.initIMU();

        encoderDrive = new EncoderDriveMecanum(deltaHardware, telemetry, encoderParameters);

        while(!imuDrive.isIMUCalibrated() && !isStopRequested()){
            telemetry.addData("[/!\\]", "Calibrating IMU Gyro sensor, please wait...");
            telemetry.addData("[Status]", imuDrive.getIMUCalibrationStatus());
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
    public void _runOpMode(){

    }

    /**
     * Overridable void to define all wheel motors, and the uppercase variables
     * Define frontLeft, frontRight, backLeft and backRight DcMotor variables here!
     */
    public void setup(){

    }

    /**
     * Set the Proportional coefficient
     * @param P the Proportional coefficient
     */
    public final void setP(double p){
        imuDrive.setP(p);
    }

    /**
     * Sets the death zone, which is the minimum motor power in which the robot moves.
     * @param P the Proportional coefficient
     */
    public final void setDeadZone(double deadZone){
        imuDrive.setDeadZone(deadZone);
    }

    public final void rotate(Rot2d rot, double power, double timeoutSecs){
        imuDrive.rotate(rot, power, timeoutSecs);
    }

    public final void forward(double inches, double speed, double timeOutSecs){
        encoderDrive.forward(inches, speed, timeOutSecs);
    }

    public final void backwards(double inches, double speed, double timeOutSecs){
        encoderDrive.backwards(inches, speed, timeOutSecs);
    }

    public final void strafeLeft(double inches, double speed, double timeOutSecs){
        encoderDrive.strafeLeft(inches, speed, timeOutSecs);
    }

    public final void strafeRight(double inches, double speed, double timeOutSecs){
        encoderDrive.strafeRight(inches, speed, timeOutSecs);
    }

    public final void turnLeft(double inches, double speed, double timeOutSecs){
        encoderDrive.turnLeft(inches, speed, timeOutSecs);
    }

    public final void turnRight(double inches, double speed, double timeOutSecs){
        encoderDrive.turnRight(inches, speed, timeOutSecs);
    }

    public final Rot2d getRobotAngle(){
        return imuDrive.getRobotAngle();
    }

    class ParametersCheck implements Runnable{

        @Override
        public void run(){
            waitForStart();
            if(!encoderParameters.haveBeenDefined()){
                telemetry.addData("[/!\\]", "Remember to define encoder constants, encoder functions will not work because parameters are 0 by default.");
            }
            telemetry.update();
        }
    }

}
