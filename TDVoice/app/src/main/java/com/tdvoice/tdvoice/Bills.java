package com.tdvoice.tdvoice;

/**
 * Created by Pavle on 2017-01-29.
 */

public class Bills {
    private double balance;
    private String type;

    public Bills(double x, String z){
        balance = x;
        type = z;
    }
    public String getType() {
        return type;
    }
    public double getBalance(){
        return balance;
    }
    public double removeBalance(){
        double temp = balance;
        balance=0;
        return temp;
    }
}
