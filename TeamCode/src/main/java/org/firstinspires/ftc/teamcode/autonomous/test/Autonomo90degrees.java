package org.firstinspires.ftc.teamcode.autonomous.test;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUMecanumLinearOpMode;
import com.deltarobotics9351.deltadrive.utils.RobotHeading;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.openftc.easyopencv.OpenCvCamera;

@Autonomous(name="Autonomo 90 degrees", group="Test")
public class Autonomo90degrees extends IMUMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    Hardware hdw;

    public SkystonePatternPipelineRojo pipelineRojo = new SkystonePatternPipelineRojo();

    public OpenCvCamera cvCamera;

    @Override
    public void _runOpMode() {
//inserte chiste del programa crasheando aqui

//Ivan no estuvo aqui
//hola, ya volvi
        waitForStart();
//que fue primero, el huevo o la gallina
        rotate(Rot2d.fromDegrees(90), 0.7, 5);
//manual para cuando se rompe el programador

        sleep(1000);

        telemetry.addData("a", this.getRobotAngle().getDegrees());
        telemetry.addData("b", RobotHeading.getHeading());
        telemetry.update();
//
        while(opModeIsActive());

    }

    @Override
    public void setup(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;

        this.UPDATE_ROBOT_HEADING = false;
        this.ROBOT_INITIAL_HEADING = Rot2d.fromDegrees(0);
    }

}
