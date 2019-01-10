package com.example.harshit.stockprices2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Stock extends Activity{
    private TextView companyTextView;
    private TextView stockAvailableTextView;
    private TextView stockReqdTextView;
    private Button purchaseButton;
    private Button sellButton;
    private EditText stockReq;
    private EditText stockSell;
    private TextView stockPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_stocks);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String company = bundle.getString("company");
        final double price = bundle.getDouble("price");

        stockPriceTextView = (TextView) findViewById(R.id.stockPrice);
        stockPriceTextView.setText(String.valueOf(price));
        companyTextView = (TextView) findViewById(R.id.company);
        companyTextView.setText(company);

        stockReq = (EditText) findViewById(R.id.stockWanted);
        purchaseButton = (Button) findViewById(R.id.purchase);

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = 0;
                if(!(stockReq.getText().toString().trim().equals(""))){
                    q = Integer.parseInt(stockReq.getText().toString().trim());
                }
                Intent intent2 = new Intent();
                double cost = q*price;
                intent2.putExtra("cost", cost);
                intent2.putExtra("quantity", q);
                intent2.putExtra("t", 1);
                Toast.makeText(Stock.this, q + "  "+ cost, Toast.LENGTH_SHORT).show();
                setResult(1, intent2);
                finish();
            }
        });

        stockSell = (EditText) findViewById(R.id.stockSelled);
        sellButton = (Button) findViewById(R.id.sell);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = 0;
                if(!(stockSell.getText().toString().trim().equals(""))){
                    q = Integer.parseInt(stockSell.getText().toString().trim());
                }
                Intent intent2 = new Intent();
                double cost = q*price;
                intent2.putExtra("cost", cost);
                intent2.putExtra("quantity", q);
                intent2.putExtra("t", 2);
                //Toast.makeText(Stock.this, q + "  "+ cost, Toast.LENGTH_SHORT).show();
                setResult(1, intent2);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(1, new Intent());
        finish();
        //Toast.makeText(this,"back key is pressed", Toast.LENGTH_SHORT).show();
    }
}