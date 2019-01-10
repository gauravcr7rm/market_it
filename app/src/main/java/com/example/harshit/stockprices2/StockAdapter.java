package com.example.harshit.stockprices2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StockAdapter extends ArrayAdapter<StockData> {
    public StockAdapter(Activity context, ArrayList<StockData> stockData){
        super(context, 0, stockData);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        StockData stockData = getItem(position);
        String stockName = stockData.getCompany();
        double stockPrice = stockData.getStockPrice();
        String price = "$" + Double.toString(stockPrice);


        TextView stockNameTextView = (TextView) listItemView.findViewById(R.id.Stock_Name);
        stockNameTextView.setText(stockName);

        TextView stockPriceTextView = (TextView) listItemView.findViewById(R.id.Stock_Price);
        stockPriceTextView.setText(price);


        return listItemView;
    }
}
