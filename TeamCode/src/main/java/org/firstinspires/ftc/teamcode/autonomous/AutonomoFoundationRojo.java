/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package org.firstinspires.ftc.teamcode.autonomous;

import com.deltarobotics9351.deltadrive.extendable.linearopmodes.mecanum.IMUPIDEncoderMecanumLinearOpMode;
import com.deltarobotics9351.deltadrive.motors.andymark.NeveRest_Orbital_20;
import com.deltarobotics9351.deltamath.geometry.Rot2d;
import com.deltarobotics9351.pid.PIDCoefficients;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

public class AutonomoFoundationRojo extends IMUPIDEncoderMecanumLinearOpMode {

    Hardware hdw = new Hardware(hardwareMap);

    @Override
    public void _runOpMode(){

        setPID(new PIDCoefficients(0.0191, 0, 0));
        imuParameters.DEAD_ZONE = 0.15;

        encoderParameters.TICKS_PER_REV = NeveRest_Orbital_20.TICKS_PER_REVOLUTION;
        encoderParameters.DRIVE_GEAR_REDUCTION = 1;
        encoderParameters.WHEEL_DIAMETER_INCHES = 4;

        String[] s = MotivateTelemetry.doMotivateGlobal();
        telemetry.addData(s[0], s[1]);

        waitForStart();

        if(!isStarted()) return;

        strafeLeft(14, 0.2, 4);
        backwards(22, 0.7, 3);

        hdw.grabFoundation();

        forward(5, 0.5, 4);

        rotate(Rot2d.fromDegrees(-90), 0.7, 5);

        hdw.releaseFoundation();

        forward(19, 0.8, 5);

    }

    @Override
    public void setup(){
        hdw.initHardware(false);

        frontLeft = hdw.wheelFrontLeft;
        frontRight = hdw.wheelFrontRight;
        backLeft = hdw.wheelBackLeft;
        backRight = hdw.wheelBackRight;
    }

}
