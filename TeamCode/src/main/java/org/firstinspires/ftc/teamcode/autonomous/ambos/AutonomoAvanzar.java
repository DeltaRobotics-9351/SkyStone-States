package org.firstinspires.ftc.teamcode.autonomous.ambos;

import com.github.deltarobotics9351.deltadrive.extendable.opmodes.linear.mecanum.IMUEncoderMecanumLinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.MotivateTelemetry;
import org.firstinspires.ftc.teamcode.autonomous.extend.Autonomo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

@Autonomous(name="Autonomo Avanzar", group="Final")
public class AutonomoAvanzar extends Autonomo { //extendemos una clase que ya contiene todos los metodos de encoders y IMU para optimizar el codigo y el tiempo

    IMUEncoderMecanumLinearOpMode op;

    public AutonomoAvanzar(OpMode opMode, Hardware hdw) {
        super(opMode, hdw);
        op = (IMUEncoderMecanumLinearOpMode) opMode;
    }

    @Override
    public void run(){

        String[] s = MotivateTelemetry.doMotivateGlobal();

        op.telemetry.addData(s[0], s[1]);
        op.telemetry.update();

        op.waitForStart();

        op.forward(30, 1, 10);
    }

}
