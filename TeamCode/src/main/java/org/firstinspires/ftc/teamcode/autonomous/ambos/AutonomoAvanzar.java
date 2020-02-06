package org.firstinspires.ftc.teamcode.autonomous;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo Avanzar", group="Final")
public class AutonomoAvanzar extends IMUEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    Hardware hdw;

    @Override
    public void _runOpMode(){

        encoderParameters.LEFT_WHEELS_TURBO = 0.7; //definimos los parametros de los encoders
        encoderParameters.RIGHT_WHEELS_TURBO = 0.7;
        encoderParameters.COUNTS_PER_REV = 537.6;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        String[] s = MotivateTelemetry.doMotivateGlobal();

        telemetry.addData(s[0], s[1]);
        telemetry.update();

        waitForStart();

        forward(30, 1, 10);
    }

    @Override
    public void defineHardware(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

}