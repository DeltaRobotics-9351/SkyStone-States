package com.github.deltarobotics9351.deltadrive.drive.mecanum.hardware;

import com.github.deltarobotics9351.deltadrive.utils.ChassisType;
import com.github.deltarobotics9351.deltadrive.utils.Invert;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DeltaHardwareMecanum {

	public DcMotor wheelFrontLeft = null;
    public DcMotor wheelFrontRight = null;
    public DcMotor wheelBackLeft = null;
    public DcMotor wheelBackRight = null;

    public DcMotor wheelHDriveMiddle = null;

    public Invert invert = null;

    public HardwareMap hdwMap = null;

    //Constructor
    public DeltaHardwareMecanum(HardwareMap hdwMap, Invert invert){
        this.invert = invert;
        this.hdwMap = hdwMap;
    }

    public void initHardware(DcMotor frontleft, DcMotor frontright, DcMotor backleft, DcMotor backright, boolean brake){

		wheelFrontLeft = frontleft;
		wheelFrontRight = frontright;
		wheelBackLeft = backleft;
		wheelBackRight = backright;

        wheelFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        wheelFrontRight.setDirection(DcMotor.Direction.FORWARD);
        wheelBackLeft.setDirection(DcMotor.Direction.FORWARD);
        wheelBackRight.setDirection(DcMotor.Direction.FORWARD);

        wheelFrontRight.setPower(0);
        wheelBackRight.setPower(0);
        wheelFrontLeft.setPower(0);
        wheelBackLeft.setPower(0);

        if(brake) {

            wheelFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            wheelBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            wheelFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            wheelBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }else{

            wheelFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            wheelBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            wheelFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            wheelBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        }

        wheelFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

}
