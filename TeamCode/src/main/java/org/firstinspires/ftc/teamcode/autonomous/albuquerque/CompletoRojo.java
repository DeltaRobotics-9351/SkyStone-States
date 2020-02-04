package org.firstinspires.ftc.teamcode.autonomous.albuquerque;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.github.deltarobotics9351.deltadrive.hardware.DeltaHardware;
import com.github.deltarobotics9351.deltadrive.utils.ChassisType;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class CompletoRojo extends IMUEncoderMecanumLinearOpMode {

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

        imuParameters.ROTATE_CORRECTION_POWER = 0.15;
        imuParameters.ROTATE_MAX_CORRECTION_TIMES = 3;

        encoderParameters.LEFT_WHEELS_TURBO = 0.7;
        encoderParameters.RIGHT_WHEELS_TURBO = 0.7;
        encoderParameters.COUNTS_PER_REV = 537.6;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        while(!isStarted()){ //mientras no se ha presionado play, se mostrara un mensaje telemetry con el pattern detectado
            MotivateTelemetry.doMotivateRed(telemetry);
            telemetry.addData("Pattern", pipelineRojo.pattern);
            telemetry.update();
        }

        switch(pipelineRojo.pattern) {
            case 0: //no se ha detectado ningun pattern

                telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                telemetry.update();

                while(opModeIsActive());

                break;
            case 1: //Pattern A

                break;
            case 2: //Pattern B

                break;
            case 3: //Pattern C

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
