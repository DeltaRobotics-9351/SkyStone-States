package org.firstinspires.ftc.teamcode.autonomous.rojo;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.github.deltarobotics9351.deltamath.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.autonomous.extend.Autonomo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class AutonomoCompletoLadoPared extends Autonomo {

    IMUEncoderMecanumLinearOpMode op;

    public SkystonePatternPipelineRojo pipelineRojo = new SkystonePatternPipelineRojo();

    public OpenCvCamera cvCamera;


    public AutonomoCompletoLadoPared(OpMode opMode, Hardware hdw) {
        super(opMode, hdw);
        op = (IMUEncoderMecanumLinearOpMode) opMode;
    }

    @Override
    public void run(){

        Telemetry telemetry = op.telemetry;

        int cameraMonitorViewId = op.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", op.hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        cvCamera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        cvCamera.openCameraDevice();

        cvCamera.setPipeline(pipelineRojo);

        cvCamera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        String[] s = MotivateTelemetry.doMotivateGlobal();

        while(!op.isStarted()){ //mientras no se ha presionado play, se mostrara un mensaje telemetry con el pattern detectado
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

                while(op.opModeIsActive());

                break;
            case A: //Pattern A

                break;
            case B: //Pattern B

                op.strafeLeft(9, 0.4, 10); //nos deslizamos hacia la skystone 2

                op.forward(2, 0.5, 5);

                op.backwards(30, 0.7, 10); //avanzamos hacia ella

                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Grab(); //cerramos la articulacion
                hdw.SSAUp(); // subimos el brazo

                op.forward(25, 0.7, 10); //avanzamos hacia la pared

                op.rotate(Rotation2d.fromDegrees(-90), 0.3, 10); //giramos hacia el skybridge
                op.backwards(75, 0.7, 10); //nos movemos hacia la building zone

                op.rotate(Rotation2d.fromDegrees(90), 0.3, 10);
                op.backwards(30, 0.7, 10);

                hdw.SSADown(); //bajamos el brazo
                hdw.SSA2Release(); //abrimos la articulacion
                hdw.SSAUp(); // subimos el brazo
                hdw.SSA2Grab(); // cerramos la articulacion

                break;
            case C: //Pattern C

                break;
        }
    }
}
