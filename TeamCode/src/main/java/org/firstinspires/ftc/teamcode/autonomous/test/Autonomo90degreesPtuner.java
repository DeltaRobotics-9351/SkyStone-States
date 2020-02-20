package org.firstinspires.ftc.teamcode.autonomous.test;

import com.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUPIDMecanumLinearOpMode;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.pid.PIDConstants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Autonomous(name="Autonomo 90 degrees P loop tuner", group="Test")
public class Autonomo90degreesPtuner extends IMUPIDMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    Hardware hdw;

    @Override
    public void _runOpMode() {
//Ivan no estuvo aqui
//hola, ya volvi
        double P = 0.01;

        while(!isStarted()){
            if(gamepad1.dpad_up){
                P += 0.0001;
            } else if (gamepad1.dpad_down) {
                P -= 0.0001;
            }

            telemetry.addData("P", P);
            telemetry.update();

            sleep(60);
        }

        setPID(new PIDConstants(P, 0, 0));

//que fue primero, el huevo o la gallina
        rotate(Rot2d.fromDegrees(90), 0.7, 5);
//manual para cuando se rompe el programador
        sleep(1000);
//comprobar si reacciona a las mordidas de Fernanda
        telemetry.addData("final robot angle", this.getRobotAngle());
        telemetry.update();
//si no, se pone el sombrero de programador
        while(opModeIsActive());
//se le cierra android studios
    }
//se le da una cobija

    @Override
    public void setup(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);
//se activa el autonomo
        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
//listo, has reiniciado a sobas. Si sigue sin funcionr se vende un esclavo
        this.UPDATE_ROBOT_HEADING = false;
        this.ROBOT_INITIAL_HEADING = Rot2d.fromDegrees(0);
    }

}
