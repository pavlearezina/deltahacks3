package com.tdvoice.tdvoice;

/**
 * Created by Pavle on 2017-01-29.
 */

public class Accounting {
    private double balance;
    private String type;
    private double interest;

    public Accounting(double x, double y, String z){
        balance = x;
        interest = y;
        type = z;
    }
    public Accounting(Accounting x){
        balance = x.getBalance();
        interest = x.getInterest();
        type = x.getType();
    }

    public void subtractBalance(double x){
        balance = balance - x;
    }

    public void addBalance(double x){
        balance += x;
    }

    public String getType() {
        return type;
    }
    public double getBalance(){
        return balance;
    }
    public double getInterest(){
        return interest;
    }


}
