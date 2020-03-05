/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package org.firstinspires.ftc.teamcode.autonomous.rojo;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUPIDEncoderMecanumLinearOpMode;
import com.deltarobotics9351.deltadrive.motors.andymark.NeveRest_Orbital_20;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.pid.PIDCoefficients;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.Pattern;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Skystone Rojo", group="Rojo")
public class AutonomoSkystoneRojo extends IMUPIDEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo
//Daniel es un otaku
    public Hardware hdw;

    public SkystonePatternPipeline skystonePipeline = new SkystonePatternPipeline();

    public OpenCvCamera cvCamera;

    @Override
    public void _runOpMode(){

        setPID(new PIDCoefficients(0.0152, 0, 0));
        imuParameters.DEAD_ZONE = 0.10;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        cvCamera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        cvCamera.openCameraDevice();
        cvCamera.setPipeline(skystonePipeline);
        cvCamera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        encoderParameters.TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        while(!isStarted()){ //mientras no se ha presionado play, se mostrara un mensaje telemetry con el pattern detectado

            String[] s = MotivateTelemetry.doMotivateAmigo(this);

            telemetry.addData(s[0], s[1]);

            telemetry.addData("Pattern", skystonePipeline.pattern.toString());

            telemetry.update();
        }

        Pattern pattern;

        while(skystonePipeline.pattern == Pattern.ND && opModeIsActive()){
            pattern = skystonePipeline.pattern;
        }

        pattern = skystonePipeline.pattern;

        cvCamera.closeCameraDevice();

        if(!isStarted()) return;//para evitar que el robot se mueva cuando se presiona stop

            switch (pattern) {
                case ND: //no se ha detectado ningun pattern

                    telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                    telemetry.update();

                    while (opModeIsActive());

                    break;
                case A: //Pattern A

                    strafeRight(8, 0.2, 5); //nos deslizamos hacia la skystone

                    forward(4, 1, 2); //nos alineamos con la pared para corregir si el robot se enchueco

                    backwards(24.5, 1, 5); //avanzamos hacia la skystone

                    hdw.SSADown(); //bajamos el brazo
                    hdw.SSA2Grab(); //cerramos la articulacion
                    hdw.SSAUp(); // subimos el brazo

                    forward(2, 0.3, 5); //avanzamos hacia la pared
                    rotate(Rot2d.fromDegrees(-90), 0.7, 2); //giramos hacia el skybridge

                    backwards(60, 0.85, 5); //nos movemos hacia la building zone

                    hdw.SSA2Release();
                    hdw.SSA2Grab();

                    forward(20, 0.85, 5);

                    break;
                case B: //Pattern B

                    strafeLeft(8, 0.2, 5); //nos deslizamos hacia la skystone

                    forward(4, 1, 2); //nos alineamos con la pared para corregir si el robot se enchueco

                    backwards(24.5, 1, 5); //avanzamos hacia la skystone

                    hdw.SSADown(); //bajamos el brazo
                    hdw.SSA2Grab(); //cerramos la articulacion
                    hdw.SSAUp(); // subimos el brazo

                    forward(2, 0.3, 5); //avanzamos hacia la pared

                    rotate(Rot2d.fromDegrees(-90), 0.7, 2); //giramos hacia el skybridge
                    backwards(50, 0.85, 5); //nos movemos hacia la building zone

                    hdw.SSA2Release();
                    hdw.SSA2Grab();

                    forward(20, 0.85, 5);

                    break;
                case C: //Pattern C

                    strafeLeft(16, 0.2, 10); //nos deslizamos hacia la skystone 2

                    forward(4, 1, 2); //nos alineamos con la pared

                    backwards(24.5, 1, 10); //avanzamos hacia ella

                    hdw.SSADown(); //bajamos el brazo
                    hdw.SSA2Grab(); //cerramos la articulacion
                    hdw.SSAUp(); // subimos el brazo

                    forward(2, 0.3, 10); //avanzamos hacia la pared
                    rotate(Rot2d.fromDegrees(-90), 0.7, 2); //giramos hacia el skybridge
                    backwards(56, 0.85, 10); //nos movemos hacia la building zone

                    hdw.SSA2Release();
                    hdw.SSA2Grab();

                    forward(20, 0.85, 5);

                    break;
            }
    }

    @Override
    public void setup(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

}
