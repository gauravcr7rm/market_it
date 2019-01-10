package com.example.harshit.stockprices2;

public class StockData {
    private String company;
    private double price;

    public StockData(String sCompany, double sPrice){
        company = sCompany;
        price = sPrice;
    }

    public double getStockPrice(){
        return price;
    }

    public String getCompany() {
        return company;
    }
}
