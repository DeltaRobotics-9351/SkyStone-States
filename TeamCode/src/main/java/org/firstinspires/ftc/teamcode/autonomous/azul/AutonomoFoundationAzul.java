package org.firstinspires.ftc.teamcode.autonomous.azul;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.rojo.AutonomoFoundationRojo;
import org.firstinspires.ftc.teamcode.autonomous.rojo.AutonomoFoundationSkystoneRojo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipeline;

@Autonomous(name = "Foundation Azul", group = "Azul")
public class AutonomoFoundationAzul extends AutonomoFoundationRojo {

    //esta clase extiende el autonomo completo rojo e invierte los giros, los deslices, y el OpenCV
    //tambien crashea y rompe el robot
    @Override
    public void setup(){
        imuParameters.INVERT_ROTATION = true; //invertimos las rotaciones

        encoderParameters.RIGHT_WHEELS_STRAFE_TURBO = -1; //invertimos los deslices
        encoderParameters.LEFT_WHEELS_STRAFE_TURBO = -1;

        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

}