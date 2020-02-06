package org.firstinspires.ftc.teamcode.autonomous;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.github.deltarobotics9351.deltamath.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo Completo Rojo", group="Final")
public class AutonomoCompletoRojo extends IMUEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    Hardware hdw;

    public SkystonePatternPipelineRojo pipelineRojo = new SkystonePatternPipelineRojo();

    public OpenCvCamera cvCamera;

    @Override
    public void _runOpMode(){

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        cvCamera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        cvCamera.openCameraDevice();

        cvCamera.setPipeline(pipelineRojo);

        cvCamera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        imuParameters.ROTATE_CORRECTION_POWER = 0.15; //definimos los parametros del imu
        imuParameters.ROTATE_MAX_CORRECTION_TIMES = 3;

        encoderParameters.LEFT_WHEELS_TURBO = 1; //definimos los parametros de los encoders
        encoderParameters.RIGHT_WHEELS_TURBO = 1;
        encoderParameters.COUNTS_PER_REV = 537.6;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        String[] s = MotivateTelemetry.doMotivateGlobal();

        while(!isStarted()){ //mientras no se ha presionado play, se mostrara un mensaje telemetry con el pattern detectado
            telemetry.addData(s[0], s[1]);
            telemetry.addData("Pattern", pipelineRojo.pattern.toString());
            //telemetry.addData("si", Rotation2d.fromDegrees(-90));

            telemetry.update();
        }

        cvCamera.closeCameraDevice();

        SkystonePatternPipelineRojo.Pattern pattern = pipelineRojo.pattern;



        switch(pattern) {
            case ND: //no se ha detectado ningun pattern

                telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                telemetry.update();

                while(opModeIsActive());

                break;
            case A: //Pattern A

                break;
            case B: //Pattern B

                strafeLeft(9, 0.4, 10); //nos deslizamos hacia la skystone 2

                forward(2, 0.5, 5);

                backwards(30, 0.7, 10); //avanzamos hacia ella

                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Grab(); //cerramos la articulacion
                hdw.SSAUp(); // subimos el brazo

                forward(25, 0.7, 10); //avanzamos hacia la pared

                rotate(Rotation2d.fromDegrees(-90), 0.3, 10); //giramos hacia el skybridge
                backwards(75, 0.7, 10); //nos movemos hacia la building zone

                rotate(Rotation2d.fromDegrees(90), 0.3, 10);
                backwards(30, 0.7, 10);

                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Release(); //abrimos la articulacion
                hdw.SSAUp(); // subimos el brazo
                hdw.SSA2Grab(); // cerramos la articulacion

                break;
            case C: //Pattern C

                break;
        }
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
