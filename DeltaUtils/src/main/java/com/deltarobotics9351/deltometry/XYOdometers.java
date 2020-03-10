/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltometry;

import com.deltarobotics9351.deltamath.geometry.Pose2d;
import com.deltarobotics9351.deltamath.geometry.Vec2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XYOdometers {

    Odometer xOdometer;
    Odometer yOdometer;

    int xTicks;
    int yTicks;

    HardwareMap hdwMap;

    public XYOdometers(Odometer xOdometer, Odometer yOdometer, HardwareMap hdwMap){

        this.xOdometer = xOdometer;
        this.yOdometer = yOdometer;

        this.hdwMap = hdwMap;

    }

    public Pose2d getTravelledTicks(){
        update();
        return new Pose2d(xTicks, yTicks, 0);
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

        double xInches = TICKS_PER_INCH_X * xTicks;
        double yInches = TICKS_PER_INCH_Y * yTicks;

        return new Pose2d(xInches, yInches, 0);

    }


    public void resetCount(){
        DcMotorEx xMotor = hdwMap.get(DcMotorEx.class, xOdometer.deviceName);
        DcMotorEx yMotor = hdwMap.get(DcMotorEx.class, yOdometer.deviceName);

        xMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

        xMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        yMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        xTicks = xMotor.getCurrentPosition();
        yTicks = yMotor.getCurrentPosition();

        for (LynxModule lynxModule : allModules){
            lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

    }


}
