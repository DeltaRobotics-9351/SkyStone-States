package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Calendar;
import java.util.Random;

public class MotivateTelemetry {

    public static String[] messagesGlobal = {"GO DELTA ROBOTICS!!", "VAMOS CRACKSSS!", "Ya lo han demostrado en otras ocasiones, ustedes pueden!", "(enigma rifa)", "Vamos hasta las finales CON TODOOOO!"};

    public static String[] messagesRed = { "GO RED ALLIANCE!", "*Don Cangrejo is ready*", "Ojala y no falle el autonomo... Suerte cracks =)"};

    public static String[] messagesBlue = {"GO BLUE ALLIANCE!", "*Don Cangrejo is ready*", "Ojala y no falle el autonomo... Suerte cracks =)"};

    static Calendar cal = Calendar.getInstance();
    static int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); //Obtenemos el dia

    static int DIA_ALBUQUERQUE = 0;

    public static String[] doMotivateGlobal(){
        if(dayOfMonth == DIA_ALBUQUERQUE) {
            String[] s = { "[Sebas]", getRandom(messagesGlobal) };
            return s;
        }else{
            String[] s = { "[>]", "All set?" };
            return s;
        }
    }

    public static String[] doMotivateRed(Telemetry telemetry){
        if(dayOfMonth == DIA_ALBUQUERQUE) {
            String[] s = { "[Sebas]", getRandom(messagesGlobal) };
            return s;
        }else{
            String[] s = { "[>]", "All set?" };
            return s;
        }
    }

    public static String[] doMotivateBlue(Telemetry telemetry){
        if(dayOfMonth == DIA_ALBUQUERQUE) {
            String[] s = { "[Sebas]", getRandom(messagesBlue) };
            return s;
        }else{
            String[] s = { "[>]", "All set?" };
            return s;
        }
    }


    static Random rdm = new Random();

    private static String getRandom(String[] array) {
        int rnd = rdm.nextInt(array.length);
        return array[rnd];
    }

    private static int currIndex = 0;

    public static String[] doMotivateAmigo(LinearOpMode opMode){

        String[] motivateAmigo = { "¡Yo soy!", "¿¡Quien!?", "¡Yo soy!", "¿¡Quien!?", "¡Amigo de", "Delta!", "¡Que si!", "¡Que no!", "¡Amigo de", "Delta!" };

        String[] message = {"[Sebas]", motivateAmigo[currIndex]};

        currIndex += 1;

        if(currIndex == motivateAmigo.length){ currIndex = 0; }

        sleep(950, opMode);

        return message;
    }


    public static void sleep(long millis, LinearOpMode opMode){
        long continueMillis = System.currentTimeMillis() + millis;

        while(System.currentTimeMillis() < continueMillis && !opMode.isStarted());

    }




}