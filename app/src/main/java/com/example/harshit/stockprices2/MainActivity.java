package com.example.harshit.stockprices2;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<StockData>> {
    double t1, t2, tp;

    public static final int RC_SIGN_IN = 1;
    public double cost = 0;
    public int q = 0;
    int hoursPassed = 0;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mRateDatabaseReference;
    private ValueEventListener mValueEventListener;
    private FirebaseUser user;

    private static final String LOG_TAG = MainActivity.class.getName();
    private static String REQUEST_URL = "https://forex.1forge.com/1.0.3/quotes?pairs=EURUSD,GBPJPY,AUDUSD&api_key=ebknY2tgVI9Z3PEazmbNDKbJvQoXuDz8";

    private static final int LOADER_ID = 1;

    private StockAdapter mAdapter;
    private QuantityAdapter qAdapter;

    private TextView mEmptyStateTextView;
    private TextView userNameTextView;
    private TextView balanceTextView;
    private TextView netWorthTextView;
    private TextView fDepositTextView;
    private Button fdeposit;
    double balance = 0;
    double networth = 0;
    double fDeposit = 0;
    double amount = 0;
    double amountToWithdraw = 0;
    String username = "";
    String com = "";
    int eurusd = 0;
    int audusd = 0;
    int gbpjpy = 0;
    double cmsnRate = 0;
    double instRate = 0;
    private ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    UserStockInfo userStockInfo;
    RateInfo rateInfo;
    List<QuantityData> quantities = new ArrayList<QuantityData>();

    //private String[] stockNames = {"AAPL", "GOOGL", "MSFT", "AMZN", "FB", "BRK-A", "BABA", "JNJ", "JPM", "XOM"};

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = System.currentTimeMillis();

        userNameTextView = (TextView) findViewById(R.id.username);
        balanceTextView = (TextView) findViewById(R.id.balance);
        netWorthTextView = (TextView) findViewById(R.id.net_worth);
        fDepositTextView = (TextView) findViewById(R.id.fixed_deposit);
        fdeposit = (Button) findViewById(R.id.fdeposit);

        final ListView stockListView = (ListView) findViewById(R.id.list1);
        mAdapter = new StockAdapter(this, new ArrayList<StockData>());
        stockListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        stockListView.setEmptyView(mEmptyStateTextView);

        final ListView quantityListView = (ListView) findViewById(R.id.list2);
        qAdapter = new QuantityAdapter(this, new ArrayList<QuantityData>());
        quantityListView.setAdapter(qAdapter);

        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUsersDatabaseReference = mFirebaseDatabase.getReference("Users");
            mRateDatabaseReference = mFirebaseDatabase.getReference("Rate");

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        // User is signed in
                        //Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    /*mUsersDatabaseReference.child(user.getUid()).child("userName").setValue(user.getDisplayName());
                    userNameTextView.setText(user.getDisplayName());
                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("balance").setValue(balance);
                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("netWorth").setValue(networth);
                    balanceTextView.setText(String.valueOf(balance));
                    netWorthTextView.setText(String.valueOf(networth));*/

                        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                            /*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                UserStockInfo userInfo = postSnapshot.getValue(UserStockInfo.class);
                                //balanceTextView.setText(userInfo.getBalance());
                                //netWorthTextView.setText(userInfo.getNetWorth());
                            }*/
                                if (dataSnapshot.hasChild(user.getUid()))
                                {
                                    userStockInfo = dataSnapshot.child(user.getUid()).getValue(UserStockInfo.class);

                                    username = userStockInfo.getuserName();
                                    balance = userStockInfo.getbalance();
                                    networth = userStockInfo.getnetWorth();
                                    eurusd = userStockInfo.getEURUSD();
                                    //Log.d(LOG_TAG, eurusd + "eurusd");

                                    audusd = userStockInfo.getAUDUSD();
                                    //Log.d(LOG_TAG, audusd + "audusd");
                                    gbpjpy = userStockInfo.getGBPJPY();
                                    //Log.d(LOG_TAG, gbpjpy + "gbpjpy");
                                    fDeposit = userStockInfo.getFdeposit();

                                } else {
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("userName").setValue(user.getDisplayName());
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("balance").setValue(100000);
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("netWorth").setValue(100000);
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("fdeposit").setValue(0);
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("eurusd").setValue(0);
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("audusd").setValue(0);
                                    mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("gbpjpy").setValue(0);
                                    balance = 100000;
                                    networth = 100000;
                                    username = user.getDisplayName();
                                    eurusd = 0;
                                    audusd = 0;
                                    gbpjpy = 0;
                                    //Toast.makeText(MainActivity.this, "When signed out : " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                /*userNameTextView.setText(user.getDisplayName());
                                balanceTextView.setText(String.valueOf(0));
                                netWorthTextView.setText(String.valueOf(networth));*/
                                }
                                userNameTextView.setText(username);
                                balanceTextView.setText(String.valueOf(balance));
                                netWorthTextView.setText(String.valueOf(networth));
                                fDepositTextView.setText(String.valueOf(fDeposit));
                                //Toast.makeText(MainActivity.this, eurusd + " " + audusd + " " + gbpjpy + " " + com + " " + username, Toast.LENGTH_SHORT).show();
                                qAdapter.clear();
                                quantities.clear();

                                QuantityData quantityData = new QuantityData(eurusd);
                                quantities.add(quantityData);
                                quantityData = new QuantityData(gbpjpy);
                                quantities.add(quantityData);
                                quantityData =new QuantityData(audusd);
                                quantities.add(quantityData);
                                qAdapter.addAll(quantities);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        mRateDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                rateInfo = dataSnapshot.getValue(RateInfo.class);
                                cmsnRate = rateInfo.getCR();
                                instRate = rateInfo.getIR();
                                //Toast.makeText(MainActivity.this, "cmsn & inst" + cmsnRate + " " + instRate, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        // User is signed out
                        userNameTextView.setText("");
                        balanceTextView.setText("");
                        netWorthTextView.setText("");
                        detachDatabaseReadListener();
                        startActivityForResult(AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(
                                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
            };

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
            /*Timer t = new Timer();
            try {
                for (int i = 1; i <= 5;i++) {
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getLoaderManager().restartLoader(0, null, MainActivity.this);
                            //Toast.makeText(MainActivity.this, "awsm", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "awsm");
                        }
                    }, 0, 5000);
                    Toast.makeText(this, "abcd", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        //mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("balance").setValue(balance);
        //mUsersDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("netWorth").setValue(networth);

        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockData currentStock = mAdapter.getItem(position);
                double price = currentStock.getStockPrice();
                com = currentStock.getCompany();
                Intent intent;
                intent = new Intent(MainActivity.this, Stock.class);
                intent.putExtra("price", price);
                intent.putExtra("company", com);
                startActivityForResult(intent, 2);
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        fdeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FDeposit.class);
                i.putExtra("Balance", balance);
                i.putExtra("deposit", fDeposit);
                startActivityForResult(i, 3);
            }
        });
    }

    @Override
    public Loader<List<StockData>> onCreateLoader(int id, Bundle bundle) {
        return new StockLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<StockData>> loader, List<StockData> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_stocks);
        mAdapter.clear();
        user = mFirebaseAuth.getCurrentUser();
        if(data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            networth = balance;
            for(int i = 0; i < quantities.size(); i++){
                networth += data.get(i).getStockPrice()*quantities.get(i).getQuantity();
            }
            netWorthTextView.setText(String.valueOf(networth));
            if(user != null) {
                mUsersDatabaseReference.child(user.getUid()).child("netWorth").setValue(networth);
            }
        }
        t2 = System.currentTimeMillis();
        tp = (t2-t1)/(1000*60);
        Log.d(LOG_TAG, "" + tp);
        if(tp > 1){
            t1 = t2;
            hoursPassed += 1;
            fDeposit += fDeposit*instRate;
            mUsersDatabaseReference.child(user.getUid()).child("fdeposit").setValue(fDeposit);
            Toast.makeText(this, hoursPassed + " min passed", Toast.LENGTH_SHORT).show();
        }
        getLoaderManager().restartLoader(0, null, MainActivity.this);
    }

    @Override
    public void onLoaderReset(Loader<List<StockData>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == 2){
            user = mFirebaseAuth.getCurrentUser();
            cost = data.getDoubleExtra("cost", 0);
            q = data.getIntExtra("quantity", 0);
            int t = data.getIntExtra("t", 0);

            if(t == 1) {
                balance = balance - cost * cmsnRate;
                mUsersDatabaseReference.child(user.getUid()).child("balance").setValue(balance);
            } else if(t == 2){
                balance = balance + cost * cmsnRate;
                mUsersDatabaseReference.child(user.getUid()).child("balance").setValue(balance);
            }
            //Log.d(LOG_TAG, " " + q);
            switch (com) {
                case "EURUSD":
                    //Toast.makeText(this, eurusd + "  " + q, Toast.LENGTH_SHORT).show();
                    if(t == 1) {
                        eurusd += q;
                    }else if(t == 2){
                        if(eurusd >= q) {
                            eurusd -= q;
                        }
                        else{
                            Toast.makeText(this, "Not enough Stocks", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mUsersDatabaseReference.child(user.getUid()).child("eurusd").setValue(eurusd);
                    //Log.d("eurusd", "" + eurusd);
                    //Toast.makeText(this, com + " " +  eurusd + " " + q, Toast.LENGTH_SHORT).show();
                    break;
                case "AUDUSD":
                    if(t == 1) {
                        audusd += q;
                    }else if(t == 2){
                        if(audusd >= q) {
                            audusd -= q;
                        }
                        else{
                            Toast.makeText(this, "Not enough Stocks", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mUsersDatabaseReference.child(user.getUid()).child("audusd").setValue(audusd);
                    //Log.d("audusd", audusd + " ");
                    //Toast.makeText(this, com + " " + audusd, Toast.LENGTH_SHORT).show();
                    break;
                case "GBPJPY":
                    if(t == 1) {
                        gbpjpy += q;
                    }else if(t == 2){
                        if(gbpjpy >= q) {
                            gbpjpy -= q;
                        } else {
                            Toast.makeText(this, "Not enough Stocks", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mUsersDatabaseReference.child(user.getUid()).child("gbpjpy").setValue(gbpjpy);
                    //Log.d("gbpjpy", gbpjpy  + " ");
                    //Toast.makeText(this, com + gbpjpy, Toast.LENGTH_SHORT).show();
                    break;
            }
            //Toast.makeText(MainActivity.this, eurusd + " " + audusd + " " + gbpjpy + " " + com + " " + username, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 3){
            user = mFirebaseAuth.getCurrentUser();
            balance = data.getDoubleExtra("bal", balance);
            amount = data.getDoubleExtra("amount", 0);
            amountToWithdraw = data.getDoubleExtra("withdraw", 0);
            fDeposit -= amountToWithdraw;
            fDeposit += amount;
            mUsersDatabaseReference.child(user.getUid()).child("balance").setValue(balance);
            mUsersDatabaseReference.child(user.getUid()).child("fdeposit").setValue(fDeposit);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkInfo != null && networkInfo.isConnected()) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
        else{
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mValueEventListener != null) {

            mUsersDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                if(networkInfo != null && networkInfo.isConnected()) {
                    AuthUI.getInstance().signOut(this);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}