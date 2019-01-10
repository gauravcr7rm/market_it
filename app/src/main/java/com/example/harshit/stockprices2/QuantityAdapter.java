package com.example.harshit.stockprices2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuantityAdapter extends ArrayAdapter<QuantityData> {
    public QuantityAdapter(@NonNull Context context, ArrayList<QuantityData> data) {
        super(context, 0, data);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item2, parent, false);
        }

        QuantityData quantityData = getItem(position);
        int qty = quantityData.getQuantity();

        TextView qtyView = (TextView) listItemView.findViewById(R.id.Stock_Quantity);
        qtyView.setText(String.valueOf(qty));

        return listItemView;
    }
}
