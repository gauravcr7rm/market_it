package com.example.harshit.stockprices2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FDeposit extends AppCompatActivity {
    double amount = 0;
    double amountToWithdraw = 0;
    double bal = 0;
    double dep = 0;

    EditText amnt;
    Button cnfrm;
    TextView deposit;
    EditText withdraw;
    Button wdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdeposit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bal = bundle.getDouble("Balance");
        dep = bundle.getDouble("deposit");
        Toast.makeText(this, "" + bal, Toast.LENGTH_SHORT).show();

        amnt = (EditText) findViewById(R.id.fdeposit);
        cnfrm = (Button) findViewById(R.id.confirm);
        deposit = (TextView) findViewById(R.id.deposit);
        withdraw = (EditText) findViewById(R.id.withdraw);
        wdraw = (Button) findViewById(R.id.wdraw);

        deposit.setText(String.valueOf(dep));


        cnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Double.parseDouble(amnt.getText().toString().trim());
                Toast.makeText(FDeposit.this, "" + amount, Toast.LENGTH_SHORT).show();
                if(amount > bal){
                    //Toast.makeText(FDeposit.this, "Not enough balance", Toast.LENGTH_SHORT).show();
                }
                else {
                    bal -= amount;
                }
                Intent intent1 = new Intent();
                intent1.putExtra("t", 1);
                intent1.putExtra("bal", bal);
                intent1.putExtra("amount", amount);
                setResult(1, intent1);
                finish();
            }
        });

        wdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToWithdraw = Double.parseDouble(withdraw.getText().toString().trim());
                if(amountToWithdraw > dep){
                    Toast.makeText(FDeposit.this, "Not Enough Deposit", Toast.LENGTH_SHORT).show();
                }
                else{
                    bal += amountToWithdraw;
                }
                Intent intent1 = new Intent();
                intent1.putExtra("t", 2);
                intent1.putExtra("bal", bal);
                intent1.putExtra("withdraw", amountToWithdraw);
                setResult(1, intent1);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(3, new Intent());
        finish();
    }
}
