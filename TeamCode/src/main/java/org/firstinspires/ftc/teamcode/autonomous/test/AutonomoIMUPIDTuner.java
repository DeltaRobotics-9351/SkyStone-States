package org.firstinspires.ftc.teamcode.autonomous.test;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUPIDTunerLinearOpMode;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Autonomous(name="Autonomo IMU PID Tuner", group="Test")
public class AutonomoIMUPIDTuner extends IMUPIDTunerLinearOpMode {

    Hardware hdw;

//Ivan no estuvo aqui
//hola, ya volvi

//que fue primero, el huevo o la gallina

//manual para cuando se rompe el programador

//comprobar si reacciona a las mordidas de Fernanda

//si no, se pone el sombrero de programador

//se le cierra android studios

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
//listo, has reiniciado a sobas. Si sigue sin funcionr se vende como esclavo
        this.UPDATE_ROBOT_HEADING = false;
        this.ROBOT_INITIAL_HEADING = Rot2d.fromDegrees(0);
    }

}
