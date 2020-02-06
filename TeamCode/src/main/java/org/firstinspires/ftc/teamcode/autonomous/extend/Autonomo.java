package org.firstinspires.ftc.teamcode.autonomous.extend;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;

public class Autonomo {

    public OpMode opMode;
    public Hardware hdw;

    public Autonomo(OpMode opMode, Hardware hdw){
        this.opMode = opMode;
        this.hdw = hdw;
    }

    public void run(){ }

}
