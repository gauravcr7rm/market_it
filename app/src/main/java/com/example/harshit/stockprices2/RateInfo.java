package com.example.harshit.stockprices2;

public class RateInfo {
    private double cmsnRate;
    private double instRate;

    public RateInfo() {
    }

    public RateInfo(double cmsnRate, double instRate) {
        this.cmsnRate = cmsnRate;
        this.instRate = instRate;
    }

    public double getCR() {
        return cmsnRate;
    }

    public void setCR(double cmsnRate) {
        this.cmsnRate = cmsnRate;
    }

    public double getIR() {
        return instRate;
    }

    public void setIR(double instRate) {
        this.instRate = instRate;
    }
}
