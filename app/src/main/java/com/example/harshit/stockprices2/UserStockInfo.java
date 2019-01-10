package com.example.harshit.stockprices2;

public class UserStockInfo {
    private String username;
    private double balance;
    private double networth;
    private double fdeposit;
    private int eurusd;
    private int audusd;
    private int gbpjpy;

    public UserStockInfo() {
    }

    public UserStockInfo(String userName, double balance, double netWorth, int EURUSD, int AUDUSD, int GBPJPY, double fDeposit) {
        this.username = userName;
        this.balance = balance;
        this.networth = netWorth;
        this.eurusd = EURUSD;
        this.audusd = AUDUSD;
        this.gbpjpy = GBPJPY;
        this.fdeposit = fDeposit;
    }

    double getbalance() {
        return balance;
    }

    public void setbalance(double Balance) {
        balance = Balance;
    }

    double getnetWorth() {
        return networth;
    }

    public void setnetWorth(double NetWorth) {
        networth = NetWorth;
    }

    String getuserName(){return username;}

    public void setuserName(String UserName){username = UserName;}

    int getEURUSD(){
        return eurusd;
    }

    public void setEURUSD(int Eurusd){
        this.eurusd = Eurusd;
    }

    int getAUDUSD(){
        return audusd;
    }

    public void setAUDUSD(int AUDUSD){
        audusd = AUDUSD;
    }

    int getGBPJPY(){
        return gbpjpy;
    }

    public void setGBPJPY(int GBPJPY){
        gbpjpy = GBPJPY;
    }

    public double getFdeposit() {
        return fdeposit;
    }

    public void setFdeposit(double fdeposit) {
        this.fdeposit = fdeposit;
    }
}