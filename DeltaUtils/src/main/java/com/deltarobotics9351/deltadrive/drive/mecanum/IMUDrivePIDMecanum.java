/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltadrive.drive.mecanum;

import com.deltarobotics9351.LibraryData;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.parameters.IMUDriveParameters;
import com.deltarobotics9351.deltamath.MathUtil;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.deltamath.geometry.Twist2d;
import com.deltarobotics9351.pid.PIDConstants;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Turning using the IMU sensor integrated in the Expansion Hub and a PID loop, which slows the motors speed the more closer the robot is to the target.
 */
public class IMUDrivePIDMecanum {

    public BNO055IMU imu;
    DeltaHardwareMecanum hdw;

    DcMotor frontleft;
    DcMotor frontright;
    DcMotor backleft;
    DcMotor backright;

    Orientation lastAngles = new Orientation();
    double globalAngle;

    Telemetry telemetry;

    private ElapsedTime runtime = new ElapsedTime();

    private IMUDriveParameters parameters;

    private double P = 0;
    private double I = 0;
    private double D = 0;

    private double deadZone = 0;

    private boolean invertRotations = false;

    private boolean isInitialized = false;

    /**
     * Constructor for the IMU drive class
     * (Do not forget to call initIMU() before the OpMode starts!)
     * @param hdw The initialized hardware containing all the chassis motors
     * @param telemetry Current OpMode telemetry to show movement info
     */
    public IMUDrivePIDMecanum(DeltaHardwareMecanum hdw, Telemetry telemetry) {
        this.hdw = hdw;
        this.telemetry = telemetry;
    }

    public void initIMU(IMUDriveParameters parameters) {

        this.parameters = parameters;

        frontleft = hdw.wheelFrontLeft;
        frontright = hdw.wheelFrontRight;
        backleft = hdw.wheelBackLeft;
        backright = hdw.wheelBackRight;

        BNO055IMU.Parameters param = new BNO055IMU.Parameters();

        param.mode = BNO055IMU.SensorMode.IMU;
        param.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        param.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        param.loggingEnabled = false;

        imu = hdw.hdwMap.get(BNO055IMU.class, "imu");

        imu.initialize(param);

        isInitialized = true;
    }

    /**
     * Dead zone is the minimum motor "power" value in which the robot has motion, in order to avoid it getting stuck during P loop.
     *
     * @param deadZone the dead zone said above
     */
    public void setDeadZone(double deadZone) {
        this.deadZone = Math.abs(deadZone);
    }

    public double getDeadZone() {
        return deadZone;
    }

    /**
     * @param coefficients the coefficients, in a PIDConstants object
     */
    public void setPID(PIDConstants coefficients) {
        this.P = Math.abs(coefficients.p);
        this.I = Math.abs(coefficients.i);
        this.D = Math.abs(coefficients.d);
    }

    public PIDConstants getPID(){ return new PIDConstants(P, I, D); }

    public double getP() {
        return P;
    }

    public double getI() {
        return I;
    }

    public double getD() {
        return D;
    }

    /**
     * Enter in a while loop until the IMU reports it is calibrated or until the opmode stops
     */
    public void waitForIMUCalibration() {
        while (!imu.isGyroCalibrated() && !Thread.interrupted()) {
            telemetry.addData("[/!\\]", "Calibrating IMU Gyro sensor, please wait...");
            telemetry.addData("[Status]", getIMUCalibrationStatus() + "\n\nDeltaUtils v" + LibraryData.VERSION);
            telemetry.update();
        }
    }

    /**
     * @return the IMU calibration status as a String
     */
    public String getIMUCalibrationStatus() {
        return imu.getCalibrationStatus().toString();
    }

    public boolean isIMUCalibrated() {
        return imu.isGyroCalibrated();
    }

    private double getAngle() {
        // We have to process the angle because the imu works in euler angles so the axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = null;

        switch(parameters.IMU_AXIS) {
            case X:
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                break;
            case Y:
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);
                break;
            default:
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                break;
        }

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    private void resetAngle() {
        switch(parameters.IMU_AXIS) {
            case X:
                lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                break;
            case Y:
                lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);
                break;
            default:
                lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                break;
        }
        globalAngle = 0;
    }

    /**
     * Invert the turning direction.
     */
    public void invert(){ invertRotations = !invertRotations; }

    /**
     * Invert the turning direction
     * @param invert inverted or not.
     */
    public void invert(boolean invert){ invertRotations = invert; }

    public Rot2d getRobotAngle() {
        return Rot2d.fromDegrees(getAngle());
    }

    /**
     * Rotate by a Rot2d with a PID loop.
     * @param rotation The Rot2d to rotate by (use Rot2d.fromDegrees() to create a new Rot2d from degrees)
     * @param power The initial power to rotate
     * @param timeoutS The max time the rotation can take, to avoid robot getting stuck.
     * @return
     */
    public Twist2d rotate(Rot2d rotation, double power, double timeoutS) {

        parameters.secureParameters();

        if(!isInitialized){
            telemetry.addData("[/!\\]", "Call initIMU() method before rotating.");
            telemetry.update();
            sleep(2000);
            return new Twist2d();
        }

        if (!isIMUCalibrated()) return new Twist2d();

        resetAngle();

        double setpoint = rotation.getDegrees();

        if(invertRotations) setpoint = -setpoint;

        runtime.reset();

        if (timeoutS == 0) {
            timeoutS = 411495121;
        }

        power = Math.abs(power);

        double prevErrorDelta = 0;
        double prevMillis = 0;
        double prevIntegral = 0;

        double velocityDelta = -1;
        double errorDelta = -1;

        double backleftpower, backrightpower, frontrightpower, frontleftpower;

        // rotaremos hasta que se complete la vuelta
        if (setpoint < 0) {
            while (getAngle() == 0 && !Thread.interrupted() && (runtime.seconds() < timeoutS)) { //al girar a la derecha necesitamos salirnos de 0 grados primero
                telemetry.addData("IMU Angle", getAngle());
                telemetry.addData("Setpoint", setpoint);
                telemetry.addData("Delta", "Not calculated yet");
                telemetry.addData("Power", power);

                backleftpower = power;
                backrightpower = -power;
                frontleftpower = power;
                frontrightpower = -power;
                defineAllWheelPower(frontleftpower, frontrightpower, backleftpower, backrightpower);
            }

            while ((velocityDelta != 0 && errorDelta != 0) && !Thread.interrupted() && (runtime.seconds() < timeoutS)) { //entramos en un bucle hasta que los setpoint sean los esperados
                double nowMillis = System.currentTimeMillis();

                errorDelta = -((-getAngle()) + setpoint);

                velocityDelta = errorDelta - prevErrorDelta;

                double divBy = Math.abs(setpoint / 90);

                prevIntegral += errorDelta;

                double proportional = (errorDelta * (P * divBy)) ;
                double integral = (prevIntegral * (I * divBy));
                double derivative = (velocityDelta * (D * divBy));

                double turbo = MathUtil.clamp(proportional + integral + derivative, -1, 1);
                double powerF;

                if (turbo > deadZone) {
                    powerF = power * MathUtil.clamp(turbo, deadZone, 1);
                } else {
                    powerF = power * MathUtil.clamp(turbo, -1, deadZone);
                }

                backleftpower = powerF;
                backrightpower = -powerF;
                frontleftpower = powerF;
                frontrightpower = -powerF;

                defineAllWheelPower(frontleftpower, frontrightpower, backleftpower, backrightpower);

                telemetry.addData("IMU Angle", getAngle());
                telemetry.addData("Setpoint", setpoint);
                telemetry.addData("Error", errorDelta);
                telemetry.addData("Turbo", turbo);
                telemetry.addData("Power", powerF);
                telemetry.update();

                prevErrorDelta = errorDelta;
                prevMillis = nowMillis;
            }
        } else
            while ((velocityDelta != 0 && errorDelta != 0) && !Thread.interrupted() && (runtime.seconds() < timeoutS)) { //entramos en un bucle hasta que los setpoint sean los esperados

                double nowMillis = System.currentTimeMillis();

                errorDelta = setpoint - getAngle();

                velocityDelta = errorDelta - prevErrorDelta;

                double divBy = Math.abs(setpoint / 90);

                prevIntegral += errorDelta;

                double proportional = (errorDelta * (P *divBy));
                double integral = (prevIntegral * (I * divBy));
                double derivative = (velocityDelta * (D * divBy));

                double turbo = MathUtil.clamp(proportional + integral + derivative, -1, 1);

                double powerF;

                if (turbo > deadZone) {
                    powerF = power * MathUtil.clamp(turbo, deadZone, 1);
                } else {
                    powerF = power * MathUtil.clamp(turbo, -1, -deadZone);
                }

                backleftpower = -powerF;
                backrightpower = powerF;
                frontleftpower = -powerF;
                frontrightpower = powerF;

                defineAllWheelPower(frontleftpower, frontrightpower, backleftpower, backrightpower);

                telemetry.addData("IMU Angle", getAngle());
                telemetry.addData("Setpoint", setpoint);
                telemetry.addData("Error", errorDelta);
                telemetry.addData("Turbo", turbo);
                telemetry.addData("Power", powerF);
                telemetry.update();

                prevErrorDelta = errorDelta;
                prevMillis = nowMillis;
            }

        // stop the movement
        defineAllWheelPower(0, 0, 0, 0);

        sleep(100);
        return new Twist2d(0, 0, Rot2d.fromDegrees(getAngle()));
    }

    private void defineAllWheelPower(double frontleft, double frontright, double backleft, double backright){
        switch(hdw.invert) {
            case RIGHT_SIDE:
                hdw.wheelFrontLeft.setPower(frontleft);
                hdw.wheelFrontRight.setPower(-frontright);
                hdw.wheelBackLeft.setPower(backleft);
                hdw.wheelBackRight.setPower(-backright);
                break;
            case LEFT_SIDE:
                hdw.wheelFrontLeft.setPower(-frontleft);
                hdw.wheelFrontRight.setPower(frontright);
                hdw.wheelBackLeft.setPower(-backleft);
                hdw.wheelBackRight.setPower(backright);
                break;
            case BOTH_SIDES:
                hdw.wheelFrontLeft.setPower(-frontleft);
                hdw.wheelFrontRight.setPower(-frontright);
                hdw.wheelBackLeft.setPower(-backleft);
                hdw.wheelBackRight.setPower(-backright);
                break;
            case NO_INVERT:
                hdw.wheelFrontLeft.setPower(frontleft);
                hdw.wheelFrontRight.setPower(frontright);
                hdw.wheelBackLeft.setPower(backleft);
                hdw.wheelBackRight.setPower(backright);
                break;
        }
    }

    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}