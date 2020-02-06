<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/rojo/AutonomoCompletoLadoPared.java
package org.firstinspires.ftc.teamcode.autonomous.rojo;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.github.deltarobotics9351.deltamath.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
=======
package org.firstinspires.ftc.teamcode.autonomous.albuquerque;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.github.deltarobotics9351.deltadrive.hardware.DeltaHardware;
import com.github.deltarobotics9351.deltadrive.utils.ChassisType;
>>>>>>> parent of f3637b7... autonomos & rotaciones (timeout):TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/albuquerque/CompletoRojo.java

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.autonomous.extend.Autonomo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/rojo/AutonomoCompletoLadoPared.java
public class AutonomoCompletoLadoPared extends Autonomo {
=======
public class CompletoRojo extends IMUEncoderMecanumLinearOpMode {
>>>>>>> parent of f3637b7... autonomos & rotaciones (timeout):TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/albuquerque/CompletoRojo.java

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

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/rojo/AutonomoCompletoLadoPared.java
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
=======
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
>>>>>>> parent of f3637b7... autonomos & rotaciones (timeout):TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/albuquerque/CompletoRojo.java

                telemetry.addData("[/!\\]", "No se ha podido detectar un patron.");
                telemetry.update();

                while(op.opModeIsActive());

                break;
            case 1: //Pattern A

                break;
<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/rojo/AutonomoCompletoLadoPared.java
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
=======
            case 2: //Pattern B
>>>>>>> parent of f3637b7... autonomos & rotaciones (timeout):TeamCode/src/main/java/org/firstinspires/ftc/teamcode/autonomous/albuquerque/CompletoRojo.java

                break;
            case 3: //Pattern C

                break;
        }
    }
}
