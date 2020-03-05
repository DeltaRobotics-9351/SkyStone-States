/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package org.firstinspires.ftc.teamcode.teleop;

import com.deltarobotics9351.deltadrive.drive.mecanum.JoystickDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.JoystickMecanumLinearOpMode;
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
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Control Manual de Don Cangrejo", group="Final") //se define que la clase se trata de un teleop con una annotation
public class TeleOp extends JoystickMecanumLinearOpMode { //la clase extendera a un 'LinearOpMode'

    //objeto que contiene el hardware del robot
    Hardware hdw;

    long runmillis;
    long disappearmillis;

    @Override
    public void _runOpMode() {
        hdw.useSleeps = false;

        String[] s = MotivateTelemetry.doMotivateGlobal();

        telemetry.addData(s[0], s[1]);
        telemetry.update();

        waitForStart(); //espera hasta que se presione <play> en la driver station

        hdw.SSA2Grab();

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
                joystick(gdp.gamepad, true, 0.8);
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
                    hdw.motorIntakeLeft.setPower(1 - minusPowerLeft);
                    hdw.motorIntakeRight.setPower(1 - minusPowerRight);
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

                if(buttons.is(X)){
                    hdw.releaseCapstone2();
                }else if(buttons.is(Y)){
                    hdw.saveCapstone2();
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

            superGamepad1.update(); //actualizamos los dos gamepads, los cuales ya tienen declarados los eventos (mas arriba esta el codigo)
            superGamepad2.update();

            telemetry.addData("wheelFrontRightPower", joystick.wheelFrontRightPower);
            telemetry.addData("wheelFrontLeftPower", joystick.wheelFrontLeftPower);
            telemetry.addData("wheelBackRightPower", joystick.wheelBackRightPower);
            telemetry.addData("wheelBackLeftPower", joystick.wheelBackLeftPower);
            telemetry.addData("wheels turbo", joystick.turbo);
            telemetry.addData("intake power right", hdw.motorIntakeRight.getPower());
            telemetry.addData("intake power left", hdw.motorIntakeLeft.getPower());
            telemetry.addData("servoStoneAutonomous position", hdw.servoStoneAutonomous.getPosition());
            telemetry.addData("servoStoneAutonomous2 position", hdw.servoStoneAutonomous2.getPosition());

            telemetry.update();  //manda los mensajes telemetry a la driver station
        }

        telemetry.addData("[Sobas]", "GG =)");
        telemetry.update();

    }

    @Override
    public void setup(){

        hdw = new Hardware(hardwareMap); //init hardware
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;

    }

}

//ayuda dijo que mataria a mi fxmilia si no le syudo, ya viene ayuda me tenngo que ir aaaaaa

//aqui tambien

//aqui se crashea el programa

//muy brutal

//aqui esta muy vacio, eso no es brutal