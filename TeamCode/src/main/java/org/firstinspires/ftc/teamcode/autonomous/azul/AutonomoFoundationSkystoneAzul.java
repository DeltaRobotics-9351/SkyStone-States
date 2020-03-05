/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipeline;

@Autonomous(name = "Foundation Skystone Azul", group = "Azul")
public class AutonomoFoundationSkystoneAzul extends AutonomoFoundationSkystoneRojo {

    //esta clase extiende el autonomo completo rojo e invierte los giros, los deslices, y el OpenCV
//tambien crashea y rompe el robot
    @Override
    public void setup(){
        skystonePipeline.alliance = SkystonePatternPipeline.Alliance.BLUE; //le decimos al OpenCV que seremos alianza azul.
//este es el resultado de una esclavisacion
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