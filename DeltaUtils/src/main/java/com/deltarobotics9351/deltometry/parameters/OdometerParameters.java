/*
 * Created by FTC team Delta Robotics #9351
 *  Source code licensed under the MIT License
 *  More info at https://choosealicense.com/licenses/mit/
 */

package com.deltarobotics9351.deltometry.parameters;

public class OdometerParameters {

    /**
     * The Odometer encoder ticks per revolution
     * It is probably specified in the page you bought the encoder from.
     */
    public double TICKS_PER_REV = 0;

    /**
     * The Odometer wheel diameter, in inches
     */
    public double WHEEL_DIAMETER_INCHES = 0;

    /**
     * Set to true if the Odometer is returning inverted tick values.
     */
    public boolean RETURNS_FLIPPED_VALUES = false;

    /**
     * This is < 1 if geared up!
     */
    public int GEAR_REDUCTION = 0;

}
