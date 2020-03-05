package org.firstinspires.ftc.teamcode.autonomous.azul;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUPIDEncoderMecanumLinearOpMode;
import com.deltarobotics9351.deltadrive.motors.andymark.NeveRest_Orbital_20;
import com.deltarobotics9351.pid.PIDCoefficients;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Autonomous(name="Estacionarse SKYBRIDGE Azul", group="Azul")
public class AutonomoEstacionarseSkybridgeAzul extends IMUPIDEncoderMecanumLinearOpMode {

    public Hardware hdw;

    @Override
    public void _runOpMode(){

        setPID(new PIDCoefficients(0.0152, 0, 0));
        imuParameters.DEAD_ZONE = 0.15;
//encoders
        encoderParameters.TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;
//esto te motiva
        String[] s = MotivateTelemetry.doMotivateGlobal();
        telemetry.addData(s[0], s[1]);

        waitForStart();

        if(!isStarted()) return;

        strafeLeft(18, 0.2, 5);

        forward(18, 0.5, 5);

    }

    @Override
    public void setup(){
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

}
