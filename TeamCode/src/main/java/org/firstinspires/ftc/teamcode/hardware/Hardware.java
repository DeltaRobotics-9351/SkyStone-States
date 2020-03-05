package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware {

    //Constructor
    public Hardware(HardwareMap m){ hwMap = m; }

    //hardwaremap que se obtuvo en el constructor
    public HardwareMap hwMap;

    //llantas
    public DcMotor wheelFrontRight = null;
    public DcMotor wheelFrontLeft = null;
    public DcMotor wheelBackRight = null;
    public DcMotor wheelBackLeft = null;

    //otros motores
    public DcMotor motorIntakeLeft = null;
    public DcMotor motorIntakeRight = null;
    public DcMotor motorSliders = null;

    //servos
    public Servo servoStoneAutonomous = null;
    public Servo servoStoneAutonomous2 = null;
    public Servo servoCapstone = null;
    public Servo servoCapstone2 = null;
    public Servo servoFoundationLeft = null;
    public Servo servoFoundationRight = null;

    public boolean useSleeps = true;

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)

    //si el boolean "invertedChassis" es true, las llantas de atras se convierten en las de adelante.
    public void initHardware(boolean invertedChassis){

        //se obtienen todos los motores, servos y sensores del hardwaremap dado
        if(invertedChassis) {
            wheelFrontRight = hwMap.get(DcMotor.class, "BL");
            wheelFrontLeft = hwMap.get(DcMotor.class, "BR");
            wheelBackRight = hwMap.get(DcMotor.class, "FL");
            wheelBackLeft = hwMap.get(DcMotor.class, "FR");
        }else{
            wheelFrontRight = hwMap.get(DcMotor.class, "FR");
            wheelFrontLeft = hwMap.get(DcMotor.class, "FL");
            wheelBackRight = hwMap.get(DcMotor.class, "BR");
            wheelBackLeft = hwMap.get(DcMotor.class, "BL");
        }
        motorIntakeLeft = hwMap.get(DcMotor.class, "IL");
        motorIntakeRight = hwMap.get(DcMotor.class, "IR");
        motorSliders = hwMap.get(DcMotor.class, "SL");

        servoStoneAutonomous = hwMap.servo.get("FS");
        servoStoneAutonomous2 = hwMap.servo.get("FS2");
        servoCapstone = hwMap.servo.get("SC");
        servoFoundationLeft = hwMap.servo.get("SFL");
        servoFoundationRight = hwMap.servo.get("SFR");
        servoCapstone2 = hwMap.servo.get("SC2");

        //La direccion por default de estos motores sera FORWARD
        motorIntakeRight.setDirection(DcMotor.Direction.FORWARD);
        motorSliders.setDirection(DcMotor.Direction.FORWARD);
        //La direccion por default de estos motores sera REVERSE
        motorIntakeLeft.setDirection(DcMotor.Direction.REVERSE);

        //el power de todos los motores se define a 0
        wheelFrontRight.setPower(0);
        wheelBackRight.setPower(0);
        wheelFrontLeft.setPower(0);
        wheelBackLeft.setPower(0);
        motorIntakeLeft.setPower(0);
        motorIntakeRight.setPower(0);
        motorSliders.setPower(0);

        //estos motores frenaran si su power es 0
        motorSliders.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorIntakeLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorIntakeRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //se define la posicion por default de estos servos
        servoStoneAutonomous.setPosition(0.1);
        servoCapstone.setPosition(0.9);
        servoStoneAutonomous2.setPosition(1);
        servoFoundationLeft.setPosition(1);
        servoFoundationRight.setPosition(0);
        servoCapstone2.setPosition(0.5);

        //definimos los motores que correran con y sin encoders 
        wheelFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorIntakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorSliders.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //servos set position
    public void SSAUp(){
        servoStoneAutonomous.setPosition(0.07); sleep(500);
    }

    public void SSADown(){
        servoStoneAutonomous.setPosition(0.45); sleep(500);
    }

    public void SSA2Grab(){
        servoStoneAutonomous2.setPosition(0.55); sleep(600);
    }

    public void SSA2Release(){
        servoStoneAutonomous2.setPosition(1); sleep(600);
    }

    public void putCapstone(){
        servoCapstone.setPosition(0.25); sleep(500);
    }
//la wea buena
    public void saveCapstone(){
        servoCapstone.setPosition(0.9); sleep(500);
    }

    public void grabFoundation(){
        servoFoundationLeft.setPosition(0);
        servoFoundationRight.setPosition(1);
        sleep(500);
    }

    public void releaseFoundation(){
        servoFoundationLeft.setPosition(1);
        servoFoundationRight.setPosition(0);
        sleep(500);
    }

    public void releaseCapstone2(){
        servoCapstone2.setPosition(0);
        sleep(500);
    }

    public void saveCapstone2(){
        servoCapstone2.setPosition(0.5);
        sleep(500);
    }

    public final void sleep(long milliseconds) {
        if(!useSleeps) return;

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

//el vacio