/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.pid;

/**
 * Class containing the PID Constants needed by some "drives"
 */
public class PIDConstants {

    /**
     * PID Coefficients
     */
    public double p, i, d;

    /**
     * Constructor for PIDConstants class
     * @param p the Proportional coefficient
     * @param i the Integral coefficient
     * @param d the Derivative coefficient
     */
    public PIDConstants(double p, double i, double d){
        this.p = p;
        this.i = i;
        this.d = d;
    }

}
