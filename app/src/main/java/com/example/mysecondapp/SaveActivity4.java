package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SaveActivity4 extends AppCompatActivity {

    static final String TAG = "SaveActivity";
    TextView dollarView;
    TextView euroView;
    TextView wonView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save4);
        Intent dispatcher = getIntent();
        double dollar_rate = dispatcher.getDoubleExtra("dollar_rate",0.0);
        double euro_rate = dispatcher.getDoubleExtra("euro_rate",0.0);
        double won_rate = dispatcher.getDoubleExtra("won_rate",0.0);

        Log.i(TAG, "receive: dollar_rate="+dollar_rate);
        Log.i(TAG, "receive: euro_rate="+euro_rate);
        Log.i(TAG, "receive: won_rate="+won_rate);

        dollarView =  findViewById(R.id.save_ipt_dollar);
        euroView = findViewById(R.id.save_ipt_euro);
        wonView = findViewById(R.id.save_ipt_won);

        dollarView.setText(dollar_rate+"");
        euroView.setText(euro_rate+"");
        wonView.setText(won_rate+"");
    }

    public void click5(View btn){
        Intent redirect = getIntent();
        double dollar_rate = Double.parseDouble(dollarView.getText().toString());
        double euro_rate = Double.parseDouble(euroView.getText().toString());
        double won_rate = Double.parseDouble(wonView.getText().toString());
        redirect.putExtra("dollar_rate2",dollar_rate);
        redirect.putExtra("euro_rate2",euro_rate);
        redirect.putExtra("won_rate2",won_rate);

        Log.i(TAG, "redirect: dollar_rate="+dollar_rate);
        Log.i(TAG, "redirect: euro_rate="+euro_rate);
        Log.i(TAG, "redirect: won_rate="+won_rate);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("dollar_rate",dollar_rate+"");
        edit.putString("euro_rate",euro_rate+"");
        edit.putString("won_rate",won_rate+"");
        edit.apply(); // edit.commit() 是异步方法提交

        setResult(2000,redirect);
        finish();
    }
}