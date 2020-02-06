package org.firstinspires.ftc.team9351.teleop;

import com.github.deltarobotics9351.deltadrive.drive.mecanum.JoystickDriveMecanum;
import com.github.deltarobotics9351.deltadrive.hardware.DeltaHardware;
import com.github.deltarobotics9351.deltadrive.utils.ChassisType;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp Antiguo", group="TeleOps") //se define que la clase se trata de un teleop con una annotation
public class TeleOp_Antiguo extends LinearOpMode { //la clase extendera a otra llamada 'LinearOpMode'

    //objeto que contiene el hardware del robot
    Hardware hdw;

    long runmillis;
    long disappearmillis;

    JoystickDriveMecanum mecanumWheels; //en este objeto se contiene el codigo para las llantas mecanum
    DeltaHardware deltaHardware;

    @Override
    public void runOpMode(){
        hdw = new Hardware(hardwareMap); //init hardware
        hdw.initHardware(false);

        deltaHardware = new DeltaHardware(hardwareMap, hdw.wheelFrontLeft, hdw.wheelFrontRight, hdw.wheelBackLeft, hdw.wheelBackRight, ChassisType.mecanum);

        mecanumWheels = new JoystickDriveMecanum(deltaHardware);

        MotivateTelemetry.doMotivateRed(telemetry);

        telemetry.update();

        waitForStart(); //espera hasta que se presione <play> en la driver station

        runmillis = System.currentTimeMillis();
        disappearmillis = runmillis + (3000); //el tiempo en el que desaparecera el mensaje "GO!!!" (milisegundos)

        while(opModeIsActive()){

            if(System.currentTimeMillis() < disappearmillis) { //se ejecuta cuando no hayan pasado mas de 3 segundos (3000 ms) desde que se dio a <play>
                telemetry.addData("[>]", "GO!!!");
            }

            startA(); //movimientos del start A

            startB();//movimientos del start B

            telemetry.addData("wheelFrontRightPower", mecanumWheels.wheelFrontRightPower);
            telemetry.addData("wheelFrontLeftPower", mecanumWheels.wheelFrontLeftPower);
            telemetry.addData("wheelBackRightPower", mecanumWheels.wheelBackRightPower);
            telemetry.addData("wheelBackLeftPower", mecanumWheels.wheelBackLeftPower);
            telemetry.addData("wheels turbo", mecanumWheels.turbo);
            telemetry.addData("intake power right", hdw.motorIntakeRight.getPower());
            telemetry.addData("intake power left", hdw.motorIntakeLeft.getPower());
            telemetry.addData("servoStoneAutonomous position", hdw.servoStoneAutonomous.getPosition());

            telemetry.update();  //manda los mensajes telemetry a la driver station
        }

    }

    public void startA() {
        if (gamepad1.left_trigger > 0.2 || gamepad1.right_trigger > 0.2) {
            mecanumWheels.joystick(gamepad1,  0.3);
        } else {
            mecanumWheels.joystick(gamepad1, 1);
        }
    }

    public void startB() {
        //intake
        if (gamepad2.a) {
            hdw.motorIntakeLeft.setPower(1);
            hdw.motorIntakeRight.setPower(1);
        } else if (gamepad2.b) {
            hdw.motorIntakeLeft.setPower(-0.5);
            hdw.motorIntakeRight.setPower(-0.5);
        }else{
            hdw.motorIntakeLeft.setPower(0);
            hdw.motorIntakeRight.setPower(0);
        }

        if(gamepad2.dpad_up){
            hdw.servoCapstone.setPosition(1);
        }else if (gamepad2.dpad_down){
            hdw.servoCapstone.setPosition(0.25);
        }

        //servo para arrastrar las stones
        if(gamepad2.y){
            hdw.servoStoneAutonomous.setPosition(0);
        }else if(gamepad2.x){
            hdw.servoStoneAutonomous.setPosition(0.5);
        }

        //slider del intake
        //invertimos el valor del joystick ya que por alguna razon esta invertida por default.
        if(gamepad2.right_trigger > 0.1) {
            hdw.motorSliders.setPower(gamepad2.right_trigger);
        }else if(gamepad2.left_trigger > 0.1){
            hdw.motorSliders.setPower(-gamepad2.left_trigger);
        }else{
            hdw.motorSliders.setPower(0);
        }

    }

}
