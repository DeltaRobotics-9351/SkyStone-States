package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Calendar;
import java.util.Random;

public class MotivateTelemetry {

    public static String[] messagesGlobal = {"GO DELTA ROBOTICS!!", "VAMOS CRACKSSS!", "Ya lo han demostrado en otras ocasiones, ustedes pueden!", "(enigma rifa)", "Vamos hasta las finales CON TODOOOO!"};

    public static String[] messagesRed = { "GO RED ALLIANCE!", "Don Cangrejo is ready"};

    public static String[] messagesBlue = {"GO BLUE ALLIANCE!", "*Don Cangrejo esta listo para agarrar esas skystones*"};

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


}