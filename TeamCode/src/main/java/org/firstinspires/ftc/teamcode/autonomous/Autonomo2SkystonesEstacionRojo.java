package org.firstinspires.ftc.teamcode.autonomous;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUEncoderMecanumLinearOpMode;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo 2Skystones Estacionarse ROJO", group="Final")
public class Autonomo2SkystonesEstacionRojo extends IMUEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

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
        imuParameters.ROTATE_CORRECTION_POWER = 0.15; //definimos los parametros del imu
        imuParameters.ROTATE_MAX_CORRECTION_TIMES = 2;
//Ivan no estuvo aqui
        encoderParameters.LEFT_WHEELS_TURBO = 1; //definimos los parametros de los encoders
        encoderParameters.RIGHT_WHEELS_TURBO = 1;
        encoderParameters.TICKS_PER_REV = 537.6;
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
        cvCamera.closeCameraDevice();
//aqui decia java, porque no me sale minecraft
        SkystonePatternPipelineRojo.Pattern pattern = pipelineRojo.pattern;
//me programas un juego
//ignora esto
//sigue programando
        switch(pattern) {
            case ND: //no se ha detectado ningun pattern
//gfhgfhfn
                telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                telemetry.update();
//aaaaaa
                while(opModeIsActive());
//Hola
                break;
            case A: //Pattern A
//iVAN
                break;
            case B: //Pattern B
//ESTUVO
                strafeLeft(10, 0.4, 10); //nos deslizamos hacia la skystone 2
//AQUI
                forward(3, 1, 2);
//iVAN ESTUVO AQUI
                backwards(25, 1, 10); //avanzamos hacia ella
//iVAN estuvo aqui
                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Grab(); //cerramos la articulacion
                hdw.SSAUp(); // subimos el brazo
//Ivan estuvo aqui
                forward(23, 1, 10); //avanzamos hacia la pared
//hola
                rotate(Rot2d.fromDegrees(-90), 0.3, 4); //giramos hacia el skybridge
                backwards(6, 1, 10); //nos movemos hacia la building zone

                hdw.SSA2Release(); //abrimos la articulacion
                hdw.SSA2Grab(); // cerramos la articulacion

                forward(108, 1, 10);

                rotate(Rot2d.fromDegrees(90), 0.3, 4);

//iVAN ESTUVO AQUI
                backwards(25, 1, 10); //avanzamos hacia ella
//iVAN estuvo aqui

                hdw.SSA2Release();
                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Grab(); //cerramos la articulacion
                hdw.SSAUp(); // subimos el brazo
//Ivan estuvo aqui

//que hacemos si el robot se vuelve malo?
                forward(23, 1, 10);

                rotate(Rot2d.fromDegrees(-90), 0.3, 4);

                backwards(75, 0.8, 10);

                hdw.SSA2Release();
                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Grab(); //cerramos la articulacion
                hdw.SSAUp(); // subimos el brazo

                forward(30, 1, 5);
                break;
            case C: //Pattern C

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
