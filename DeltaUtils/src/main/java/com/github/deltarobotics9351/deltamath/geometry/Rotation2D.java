package com.github.deltarobotics9351.deltamath.geometry;

public class Rotation2d {

    private double radians;
    private double cos;
    private double sin;

    public Rotation2d(){
        radians = 0;
        cos = 1;
        sin = 0;
    }

    public Rotation2d(double x, double y){
        double hy = Math.hypot(x, y);
        if(hy > 0.00001){
            sin = y / hy;
            cos = x / hy;
        }else{
            sin = 0;
            cos = 1;
        }
    }

    public Rotation2d(double radians){
        this.radians = radians;
        this.cos = Math.cos(radians);
        this.sin = Math.sin(radians);
    }

    public void setDegrees(double degrees){
        this.radians = Math.toRadians(degrees);
        this.cos = Math.cos(radians);
        this.sin = Math.sin(radians);
    }

    public static final Rotation2d fromDegrees(double degrees){
        return new Rotation2d(Math.toRadians(degrees));
    }

    public static final Rotation2d fromRadians(double radians){
        return new Rotation2d(radians);
    }

    public void setRadians(double radians){
        this.radians = radians;
        this.cos = Math.cos(radians);
        this.sin = Math.sin(radians);
    }

    public double toDegrees(){
        return Math.toDegrees(radians);
    }

    public double deltaRadians(Rotation2d other){
        double deltaAngle = toDegrees() - other.toDegrees();

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        return Math.toRadians(deltaAngle);
    }

    public double deltaDegrees(Rotation2d other){
        double deltaAngle = toDegrees() - other.toDegrees();

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        return deltaAngle;
    }

    public double calculateTan(){
        return sin / cos;
    }

    public double getSin(){
        return sin;
    }

    public double getCos(){
        return cos;
    }

    public double getRadians(){
        return radians;
    }

    public void rotate(Rotation2d o){
        double x = cos * o.cos - sin * o.sin;
        double y = cos * o.sin + sin * o.cos;

        double hy = Math.hypot(
                x,
                y);

        if(hy > 0.00001){
            sin = y / hy;
            cos = x / hy;
        }else{
            sin = 0;
            cos = 1;
        }
    }

    @Override
    public String toString(){
        return "Rot2d(rad " + radians + ", deg " + Math.toDegrees(radians) + ")";
    }

}
