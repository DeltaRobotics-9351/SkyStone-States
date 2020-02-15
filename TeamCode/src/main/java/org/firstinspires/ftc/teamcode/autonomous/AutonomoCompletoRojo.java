package org.firstinspires.ftc.teamcode.autonomous;

import com.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUPEncoderMecanumLinearOpMode;
import com.deltarobotics9351.deltadrive.motors.andymark.NeveRest_Orbital_20;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo Completo Rojo", group="Final")
public class AutonomoCompletoRojo extends IMUPEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    Hardware hdw;

    public SkystonePatternPipelineRojo pipelineRojo = new SkystonePatternPipelineRojo();

    public OpenCvCamera cvCamera;

    @Override
    public void _runOpMode(){
//inserte chiste del programa crasheando aqui
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//aqui se crashea
        //creamos la camara de OpenCV
        cvCamera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
//c
        //la inicializamos
        cvCamera.openCameraDevice();
//b
        cvCamera.setPipeline(pipelineRojo);
//a
        cvCamera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
//mmmmmmmmmm
        setP(0.019);
        setDeadZone(0.15);
//Ivan no estuvo aqui
        encoderParameters.LEFT_WHEELS_TURBO = 1; //definimos los parametros de los encoders
        encoderParameters.RIGHT_WHEELS_TURBO = 1;
        encoderParameters.COUNTS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;
//hhvdgdfg
        String[] s = MotivateTelemetry.doMotivateGlobal();
//ya me dio flo jera escribir
        while(!isStarted()){ //mientras no se ha presionado play, se mostrara un mensaje telemetry con el pattern detectado
            telemetry.addData(s[0], s[1]);
            telemetry.addData("Pattern", pipelineRojo.pattern.toString());
            //telemetry.addData("si", Rot2d.fromDegrees(-90));
//hola
            telemetry.update();
        }
//Daniel es un otaku
        SkystonePatternPipelineRojo.Pattern pattern = pipelineRojo.pattern;
        while(pattern == SkystonePatternPipelineRojo.Pattern.ND && opModeIsActive()){
            pattern = pipelineRojo.pattern;
        }

        cvCamera.closeCameraDevice();

//aqui decia java, porque no me sale minecraft
//me programas un juego
//ignora esto
//sigue programando
        if(isStarted()) { //para evitar que el robot se mueva cuando se presiona stop
            switch (pattern) {
                case ND: //no se ha detectado ningun pattern
//gfhgfhfn
                    telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                    telemetry.update();
//aaaaaa
                    while (opModeIsActive());
//Hola
                    break;
                case A: //Pattern A
//iVAN
                    break;
                case B: //Pattern B
//ESTUVO
                    strafeLeft(6, 0.2, 10); //nos deslizamos hacia la skystone 2
//AQUI
                    forward(3, 1, 2);
//iVAN ESTUVO AQUI
                    backwards(24, 1, 10); //avanzamos hacia ella
//iVAN estuvo aqui
                    hdw.SSADown(); //bajamos el brazo
                    hdw.SSA2Grab(); //cerramos la articulacion
                    hdw.SSAUp(); // subimos el brazo
//Ivan estuvo aqui
                    forward(23, 1, 10); //avanzamos hacia la pared
//hola
                    rotate(Rot2d.fromDegrees(-90), 0.7, 2); //giramos hacia el skybridge
                    backwards(75, 0.7, 10); //nos movemos hacia la building zone
//aaaaaa
                    rotate(Rot2d.fromDegrees(90), 0.7, 2);
                    backwards(21, 1, 10);
//IVAN ESTUVO AUQUI

                    hdw.SSA2Release(); //abrimos la articulacion
                    hdw.SSA2Grab(); // cerramos la articulacion

                    hdw.grabFoundation();

//que hacemos si el robot se vuelve malo?
                    forward(23, 1, 10);

                    rotate(Rot2d.fromDegrees(-90), 0.7, 2);

                    hdw.releaseFoundation();

                    forward(110, 0.8, 10);

                    rotate(Rot2d.fromDegrees(90), 0.7, 2);

                    backwards(21, 0.7, 10);

                    hdw.SSA2Release();
                    hdw.SSADown(); //bajamos el brazo
                    hdw.SSA2Grab(); //cerramos la articulacion
                    hdw.SSAUp(); // subimos el brazo

                    forward(25, 0.7, 10); //avanzamos hacia la pared
//hola
                    rotate(Rot2d.fromDegrees(-90), 0.7, 2); //giramos hacia el skybridge
                    backwards(75, 1, 10); //nos movemos hacia la building zone

                    hdw.SSA2Release(); //abrimos la articulacion
                    hdw.SSA2Grab(); // cerramos la articulacion

                    forward(5, 1, 5);
                    break;
                case C: //Pattern C

                    break;
            }
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
