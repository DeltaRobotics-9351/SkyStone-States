package org.firstinspires.ftc.teamcode.autonomous;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.ambos.AutonomoAvanzar;
import org.firstinspires.ftc.teamcode.autonomous.extend.Autonomo;
import org.firstinspires.ftc.teamcode.autonomous.rojo.AutonomoCompletoLadoPared;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Autonomous(name="Selector de Autonomos ROJO", group="Final")
public class SelectorRojo extends IMUEncoderMecanumLinearOpMode { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo


    Hardware hdw;

    public Autonomo[] autonomos = { new AutonomoCompletoLadoPared(this, hdw),
    new AutonomoAvanzar(this, hdw)};

    Autonomo seleccionado = null;

    //empieza seccion de funciones

    @Override
    public void _runOpMode(){


        imuParameters.ROTATE_CORRECTION_POWER = 0.15; //definimos los parametros del imu
        imuParameters.ROTATE_MAX_CORRECTION_TIMES = 3;

        encoderParameters.LEFT_WHEELS_TURBO = 1; //definimos los parametros de los encoders
        encoderParameters.RIGHT_WHEELS_TURBO = 1;
        encoderParameters.COUNTS_PER_REV = 537.6;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        selectorMenu(); //entramos en modo selector, en el que mostramos mensajes telemetry y usamos
                        //los gamepads para seleccionar un autonomo.

        seleccionado.run();

    }

    public void selectorMenu(){
        int selecIndex = -1;

        while(!gamepad1.a || !gamepad2.a){
            if(seleccionado == null){
                telemetry.addData("[/!\\]", "d-pad izquierda y derecha para cambiar la seleccion\n(A) para aceptar (En cualquier control)");
                telemetry.update();
            }

            if(gamepad1.dpad_right || gamepad2.dpad_right){
                selecIndex += 1;
                if(selecIndex > autonomos.length){
                    selecIndex = 0;
                }
                seleccionado = autonomos[selecIndex];
            }

            if(gamepad1.dpad_left || gamepad2.dpad_left){
                selecIndex += 1;
                if(selecIndex < 0){
                    selecIndex = autonomos.length;
                }
                seleccionado = autonomos[selecIndex];
            }

            telemetry.addData("[/!\\]", seleccionado.getClass().getName() + "\n\nD-pad izquierda y derecha para cambiar la seleccion\n(A) para aceptar (En cualquier control)");
            telemetry.update();

            sleep(100);
        }
    }

    @Override
    public void defineHardware(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

    //termina seccion de init & selector

}