/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package org.firstinspires.ftc.teamcode.teleop;

import com.deltarobotics9351.deltadrive.drive.mecanum.JoystickDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.utils.Invert;
import com.deltarobotics9351.deltainput.gamepad.GamepadDataPacket;
import com.deltarobotics9351.deltainput.gamepad.button.Button;
import com.deltarobotics9351.deltainput.gamepad.SuperGamepad;
import com.deltarobotics9351.deltainput.event.GamepadEvent;
import com.deltarobotics9351.deltainput.gamepad.button.Buttons;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

//@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="SuperGamepad TEST", group="TeleOps") //se define que la clase se trata de un teleop con una annotation
public class SuperGamepad_TeleOp extends LinearOpMode { //la clase extendera a otra llamada 'LinearOpMode'

    //objeto que contiene el hardware del robot
    Hardware hdw;

    long runmillis;
    long disappearmillis;

    JoystickDriveMecanum mecanumWheels; //en este objeto se contiene el codigo para las llantas mecanum
    DeltaHardwareMecanum deltaHardware;

    public SuperGamepad superGamepad1;
    public SuperGamepad superGamepad2;

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap); //init hardware
        hdw.initHardware(false);

        superGamepad1 = new SuperGamepad(gamepad1);
        superGamepad2 = new SuperGamepad(gamepad2);

        hdw.useSleeps = false;

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, Invert.RIGHT_SIDE);

        deltaHardware.initHardware(hdw.wheelFrontLeft, hdw.wheelFrontRight, hdw.wheelBackLeft, hdw.wheelBackRight, true);

        mecanumWheels = new JoystickDriveMecanum(deltaHardware);

        String[] s = MotivateTelemetry.doMotivateGlobal();

        telemetry.addData(s[0], s[1]);
        telemetry.update();

        waitForStart(); //espera hasta que se presione <play> en la driver station

        runmillis = System.currentTimeMillis();
        disappearmillis = runmillis + (3000); //el tiempo en el que desaparecera el mensaje "GO!!!" (milisegundos)


        // - - - - EMPIEZA CODIGO DEL START A - - - -

        superGamepad1.registerEvent(new GamepadEvent() {

            @Override
            public void buttonsPressed(Buttons buttons) {
                if (buttons.is(DPAD_UP)) hdw.SSAUp(); //subir arti de skystones

                if (buttons.is(DPAD_DOWN)) hdw.SSADown(); //bajar arti de skystones

                if (buttons.is(DPAD_LEFT)) hdw.SSA2Release(); //abrir garra de skystones

                if (buttons.is(DPAD_RIGHT)) hdw.SSA2Grab(); //cerrar garra de skystones
            }

            @Override
            public void loop(GamepadDataPacket gdp) {
                //Calculos para las llantas mecanum
                if (left_trigger > 0.2) {
                    mecanumWheels.joystick(gdp.gamepad, 1 - Range.clip(left_trigger, 0, 0.7));
                } else if (right_trigger > 0.2) {
                    mecanumWheels.joystick(gdp.gamepad, 1 - Range.clip(right_trigger, 0, 0.7));
                } else {
                    mecanumWheels.joystick(gdp.gamepad, 1);
                }
            }

        });

        // - - - - TERMINA CODIGO DEL START A - - - -

        // - - - - EMPIEZA CODIGO DEL START B - - - -

        superGamepad2.registerEvent(new GamepadEvent() {

            @Override
            public void buttonsBeingPressed(Buttons buttons) {

                if (buttons.is(A)) {
                    double minusPowerLeft = Range.clip(left_trigger, 0,0.5);
                    double minusPowerRight = Range.clip(right_trigger, 0,0.5);
                    hdw.motorIntakeLeft.setPower(1 +  minusPowerLeft);
                    hdw.motorIntakeRight.setPower(1 +  minusPowerRight);
                } else if (buttons.is(B)) {
                    double minusPowerLeft = Range.clip(left_trigger, 0,0.5);
                    double minusPowerRight = Range.clip(right_trigger, 0,0.5);
                    hdw.motorIntakeLeft.setPower(-1 +  minusPowerLeft);
                    hdw.motorIntakeRight.setPower(-1 +  minusPowerRight);
                }

                if(buttons.is(DPAD_UP)){
                    hdw.saveCapstone();
                }else if (buttons.is(DPAD_DOWN)){
                    hdw.putCapstone();
                }

                if(buttons.is(DPAD_LEFT)){
                    hdw.releaseFoundation();
                }else if(buttons.is(Button.DPAD_RIGHT)){
                    hdw.grabFoundation();
                }

            }

            @Override
            public void buttonsReleased(Buttons buttons){
                if(buttons.is(A) || buttons.is(B)){
                    hdw.motorIntakeLeft.setPower(0);
                    hdw.motorIntakeRight.setPower(0);
                }
            }

            @Override
            public void loop(GamepadDataPacket gdp){
                //sliders
                //invertimos el valor del joystick ya que por alguna razon el eje y esta invertida por default.
                if(-left_stick_y > 0.1 || -left_stick_y < -0.1) {
                    hdw.motorSliders.setPower(-left_stick_y);
                }else if(-right_stick_y > 0.1 || -right_stick_y < -0.1){
                    hdw.motorSliders.setPower(-right_stick_y);
                }else{
                    hdw.motorSliders.setPower(0);
                }
            }

        });

        // - - - - TERMINA CODIGO DEL START B - - - -


        while (opModeIsActive()) {

            if (System.currentTimeMillis() < disappearmillis) { //se ejecuta mientras que no hayan pasado mas de 3 segundos (3000 ms) desde que se dio a <play>
                telemetry.addData("[>]", "GO!!!");
            }

            superGamepad1.update(); //actualizamos los dos gamepads, los cuales ya tienen declarados los eventos (arriba)
            superGamepad2.update(); //que haran que el robot funcione.

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

        telemetry.addData("[Sobas]", "Mucha suerte cracks, nos vemos en la proxima partida =)");
        telemetry.update();

        sleep(800);

    }

}

//ayuda dijo que mataria a mi fxmilia si no le syudo, ya viene ayuda me tenngo que ir aaaaaa

//aqui tambien

//aqui se crashea el programa

//muy brutal

//aqui esta muy vacio, eso no es brutal