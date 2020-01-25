package org.firstinspires.ftc.teamcode.teleop;

import com.github.deltarobotics9351.deltadrive.drive.mecanum.JoystickDriveMecanum;
import com.github.deltarobotics9351.deltadrive.hardware.DeltaHardware;
import com.github.deltarobotics9351.deltadrive.utils.ChassisType;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="TeleOps") //se define que la clase se trata de un teleop con una annotation
public class TeleOp extends LinearOpMode { //la clase extendera a otra llamada 'LinearOpMode'

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

        MotivateTelemetry.doMotivateGlobal(telemetry);

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
            telemetry.addData("servoStoneAutonomous2 position", hdw.servoStoneAutonomous2.getPosition());

            telemetry.update();  //manda los mensajes telemetry a la driver station
        }

    }

    public void startA() {
        //si cualquiera de los 2 triggers es presionado (mayor que 0), el robot avanzara al 30%
        //de velocidad. el fin de esto es para que el arrastrar la foundation en el endgame no sea
        //tan arriesgado y haya menos probabilidad de que tiremos cualquier stone
        if (gamepad1.left_trigger > 0.2) {
            mecanumWheels.joystick(gamepad1,  1 - Range.clip(gamepad1.left_trigger, 0,0.7));
        }else if(gamepad1.right_trigger > 0.2){
            mecanumWheels.joystick(gamepad1, 1 -  Range.clip(gamepad1.right_trigger, 0,0.7));
        } else {
            mecanumWheels.joystick(gamepad1, 1);
        }

        //servo para arrastrar las stones
        if(gamepad1.dpad_up){
            hdw.servoStoneAutonomous.setPosition(0);
        }else if(gamepad1.dpad_down){
            hdw.servoStoneAutonomous.setPosition(0.5);
        }

        if(gamepad1.dpad_left){
            hdw.servoStoneAutonomous2.setPosition(1);
        }else if(gamepad1.dpad_right){
            hdw.servoStoneAutonomous2.setPosition(0);
        }
    }

    public void startB() {
        //intake
        if (gamepad2.a) {
            double minusPowerLeft = Range.clip(gamepad2.left_trigger, 0,0.5);
            double minusPowerRight = Range.clip(gamepad2.right_trigger, 0,0.5);
            hdw.motorIntakeLeft.setPower(1 +  minusPowerLeft);
            hdw.motorIntakeRight.setPower(1 +  minusPowerRight);
        } else if (gamepad2.b) {
            double minusPowerLeft = Range.clip(gamepad2.left_trigger, 0,0.5);
            double minusPowerRight = Range.clip(gamepad2.right_trigger, 0,0.5);
            hdw.motorIntakeLeft.setPower(-1 +  minusPowerLeft);
            hdw.motorIntakeRight.setPower(-1 +  minusPowerRight);
        }else{
            hdw.motorIntakeLeft.setPower(0);
            hdw.motorIntakeRight.setPower(0);
        }

        if(gamepad2.dpad_up){
            hdw.servoCapstone.setPosition(1);
        }else if (gamepad2.dpad_down){
            hdw.servoCapstone.setPosition(0.25);
        }

        //slider del intake
        //invertimos el valor del joystick ya que por alguna razon esta invertida por default.
        if(-gamepad2.left_stick_y > 0.1 || -gamepad2.left_stick_y < -0.1) {
            hdw.motorSliders.setPower(-gamepad2.left_stick_y);
        }else if(-gamepad2.right_stick_y > 0.1 || -gamepad2.right_stick_y < -0.1){
            hdw.motorSliders.setPower(-gamepad2.right_stick_y);
        }else{
            hdw.motorSliders.setPower(0);
        }

    }

}
