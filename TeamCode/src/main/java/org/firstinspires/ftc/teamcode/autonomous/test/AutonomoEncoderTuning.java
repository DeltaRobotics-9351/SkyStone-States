package org.firstinspires.ftc.teamcode.autonomous.test;


import com.deltarobotics9351.deltadrive.drive.mecanum.EncoderDriveMecanum;
import com.deltarobotics9351.deltadrive.drive.mecanum.hardware.DeltaHardwareMecanum;
import com.deltarobotics9351.deltadrive.parameters.EncoderDriveParameters;

import com.deltarobotics9351.deltadrive.utils.Invert;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Disabled
@Autonomous(name="Autonomo Encoder Tuning", group="TEST")
public class AutonomoEncoderTuning extends LinearOpMode {

    private Hardware hdw;

    private DeltaHardwareMecanum deltaHardware;

    private EncoderDriveMecanum encoderDrive;

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap); //creamos el hardware
        hdw.initHardware(false); //lo inicializamos

        deltaHardware = new DeltaHardwareMecanum(hardwareMap, Invert.RIGHT_SIDE);
        deltaHardware.initHardware(hdw.wheelFrontLeft, hdw.wheelFrontRight, hdw.wheelBackLeft, hdw.wheelBackRight, true);

        EncoderDriveParameters parameters = new EncoderDriveParameters();
        parameters.LEFT_WHEELS_TURBO = 0.6;
        parameters.RIGHT_WHEELS_TURBO = 0.9;
        parameters.COUNTS_PER_REV = 537.6;
        parameters.DRIVE_GEAR_REDUCTION = 1;
        parameters.WHEEL_DIAMETER_INCHES = 4;

        //Si no eres sebas no se que haces en la programacion//

        encoderDrive = new EncoderDriveMecanum(deltaHardware, telemetry, parameters);

        telemetry.addData("[/!\\]", "AUTONOMO DE PRUEBA! NO ESTA HECHO PARA SER USADO EN UNA COMPETENCIA.");
        telemetry.update();

        while(!isStarted()){

            if(gamepad1.dpad_up){
                parameters.LEFT_WHEELS_TURBO += 0.05;
            }else if(gamepad1.dpad_down){
                parameters.LEFT_WHEELS_TURBO -= 0.05;
            }

            if(gamepad1.dpad_right){
                parameters.RIGHT_WHEELS_TURBO += 0.05;
            }else if(gamepad1.dpad_left){
                parameters.RIGHT_WHEELS_TURBO -= 0.05;
            }

            //Espera esto no es Hopscotch//

            telemetry.addData("right", parameters.RIGHT_WHEELS_TURBO);
            telemetry.addData("left", parameters.LEFT_WHEELS_TURBO);
            telemetry.update();

            sleep(500);
        }

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        //imuDrive.rotate(90, 0.4);
        //Salu2

        //porque las palabars son de colores//
        encoderDrive.forward(10, 0.5, 10);
    }


}