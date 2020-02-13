package org.firstinspires.ftc.teamcode.teleop;

import com.deltarobotics9351.deltadrive.drive.mecanum.JoystickDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.utils.Invert;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Control Manual de Don Cangrejo", group="TeleOps") //se define que la clase se trata de un teleop con una annotation
public class TeleOp extends LinearOpMode { //la clase extendera a otra llamada 'LinearOpMode'

    //objeto que contiene el hardware del robot
    Hardware hdw;

    long runmillis;
    long disappearmillis;

    JoystickDriveMecanum mecanumWheels; //en este objeto se contiene el codigo para las llantas mecanum
    DeltaHardwareMecanum deltaHardware;

    @Override
    public void runOpMode(){
        hdw = new Hardware(hardwareMap); //init hardware
        hdw.initHardware(false);

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, Invert.RIGHT_SIDE);

        deltaHardware.initHardware(hdw.wheelFrontLeft, hdw.wheelFrontRight, hdw.wheelBackLeft, hdw.wheelBackRight, true);

        mecanumWheels = new JoystickDriveMecanum(deltaHardware);

        String[] s = MotivateTelemetry.doMotivateGlobal();

        telemetry.addData(s[0], s[1]);
        telemetry.update();

        waitForStart(); //espera hasta que se presione <play> en la driver station

        runmillis = System.currentTimeMillis();
        disappearmillis = runmillis + (3000); //el tiempo en el que desaparecera el mensaje "GO!!!" (milisegundos)

        while(opModeIsActive()){

            if(System.currentTimeMillis() < disappearmillis) { //se ejecuta mientras que no hayan pasado mas de 3 segundos (3000 ms) desde que se dio a <play>
                telemetry.addData("[>]", "GO!!!");
            }

            startA(gamepad1); //movimientos del start A

            startB(gamepad2);//movimientos del start B

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

        telemetry.addData("[>]", "Mucha suerte cracks, nos vemos en la proxima partida =)");
        telemetry.update();

        sleep(300);

    }

    public void startA(Gamepad gamepad) {
        //si cualquiera de los 2 triggers es presionado (mayor que 0), el robot avanzara al 30%
        //de velocidad. el fin de esto es para que el arrastrar la foundation en el endgame no sea
        //tan arriesgado y haya menos probabilidad de que tiremos cualquier stone
        if (gamepad.left_trigger > 0.2) {
            mecanumWheels.joystick(gamepad,  1 - Range.clip(gamepad.left_trigger, 0,0.7));
        }else if(gamepad.right_trigger > 0.2){
            mecanumWheels.joystick(gamepad, 1 -  Range.clip(gamepad.right_trigger, 0,0.7));
        } else {
            mecanumWheels.joystick(gamepad, 1);
        }

        //servos para agarrar las stones
        if(gamepad.dpad_up){
            hdw.servoStoneAutonomous.setPosition(0.1);
        }else if(gamepad.dpad_down){
            hdw.servoStoneAutonomous.setPosition(0.45);
        }
//ayuda dijo que mataria a mi fxmilia si no le syudo, ya viene ayuda me tenngo que ir aaaaaa
        if(gamepad.dpad_left){
            hdw.servoStoneAutonomous2.setPosition(0.6);
         }else if(gamepad.dpad_right){
            hdw.servoStoneAutonomous2.setPosition(0.9);
        }
    }

    public void startB(Gamepad gamepad) {
        //intake
        if (gamepad.a) {
            double minusPowerLeft = Range.clip(gamepad.left_trigger, 0,0.5);
            double minusPowerRight = Range.clip(gamepad.right_trigger, 0,0.5);
            hdw.motorIntakeLeft.setPower(1 +  minusPowerLeft);
            hdw.motorIntakeRight.setPower(1 +  minusPowerRight);
        } else if (gamepad.b) {
            double minusPowerLeft = Range.clip(gamepad.left_trigger, 0,0.5);
            double minusPowerRight = Range.clip(gamepad.right_trigger, 0,0.5);
            hdw.motorIntakeLeft.setPower(-1 +  minusPowerLeft);
            hdw.motorIntakeRight.setPower(-1 +  minusPowerRight);
        }else{
            hdw.motorIntakeLeft.setPower(0);
            hdw.motorIntakeRight.setPower(0);
        }

        if(gamepad.dpad_up){
            hdw.servoCapstone.setPosition(1);
        }else if (gamepad.dpad_down){
            hdw.servoCapstone.setPosition(0.25);
        }

        //slider del intake
        //invertimos el valor del joystick ya que por alguna razon el eje y esta invertida por default.
        if(-gamepad.left_stick_y > 0.1 || -gamepad.left_stick_y < -0.1) {
            hdw.motorSliders.setPower(-gamepad.left_stick_y);
        }else if(-gamepad.right_stick_y > 0.1 || -gamepad.right_stick_y < -0.1){
            hdw.motorSliders.setPower(-gamepad.right_stick_y);
        }else{
            hdw.motorSliders.setPower(0);
        }

    }

}
