/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltometry;

import com.deltarobotics9351.deltamath.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class XYHeadingOdometers {

    Odometer xOdometer;
    Odometer yOdometer;
    Odometer headingOdometer;

    int xTicks;
    int yTicks;
    int headingTicks;

    HardwareMap hdwMap;

    public XYHeadingOdometers(Odometer xOdometer, Odometer yOdometer, Odometer headingOdometer, HardwareMap hdwMap){

        this.xOdometer = xOdometer;
        this.yOdometer = yOdometer;
        this.headingOdometer = headingOdometer;

        this.hdwMap = hdwMap;

    }

    public Pose2d getTravelledTicks(){
        update();
        return new Pose2d(xTicks, yTicks, headingTicks);
    }

    /**
     * Get the X & Y travelled inches
     * @return a Pose2d with the travelled inches
     */
    public Pose2d getTravelledInches(){

        update();

        double TICKS_PER_INCH_X = (xOdometer.parameters.TICKS_PER_REV * xOdometer.parameters.GEAR_REDUCTION) /
                (xOdometer.parameters.WHEEL_DIAMETER_INCHES * Math.PI);

        double TICKS_PER_INCH_Y = (yOdometer.parameters.TICKS_PER_REV * yOdometer.parameters.GEAR_REDUCTION) /
                (yOdometer.parameters.WHEEL_DIAMETER_INCHES * Math.PI);

        double TICKS_PER_INCH_HEADING = (headingOdometer.parameters.TICKS_PER_REV * headingOdometer.parameters.GEAR_REDUCTION) /
                (headingOdometer.parameters.WHEEL_DIAMETER_INCHES * Math.PI);

        double xInches = TICKS_PER_INCH_X * xTicks;
        double yInches = TICKS_PER_INCH_Y * yTicks;
        double headingInches = TICKS_PER_INCH_HEADING * headingTicks;

        return new Pose2d(xInches, yInches, headingInches);

    }

    public void resetCount(){
        DcMotorEx xMotor = hdwMap.get(DcMotorEx.class, xOdometer.deviceName);
        DcMotorEx yMotor = hdwMap.get(DcMotorEx.class, yOdometer.deviceName);
        DcMotorEx headingMotor = hdwMap.get(DcMotorEx.class, headingOdometer.deviceName);

        xMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        headingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Update the ticks count with a bulk read.
     */
    private void update(){

        List<LynxModule> allModules = hdwMap.getAll(LynxModule.class);

        for (LynxModule lynxModule : allModules){
            lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            lynxModule.clearBulkCache();
        }

        DcMotorEx xMotor = hdwMap.get(DcMotorEx.class, xOdometer.deviceName);
        DcMotorEx yMotor = hdwMap.get(DcMotorEx.class, yOdometer.deviceName);
        DcMotorEx headingMotor = hdwMap.get(DcMotorEx.class, headingOdometer.deviceName);


        xMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        yMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        headingMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        xTicks = xMotor.getCurrentPosition();
        yTicks = yMotor.getCurrentPosition();
        headingTicks = headingMotor.getCurrentPosition();

        for (LynxModule lynxModule : allModules){
            lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

    }


}
