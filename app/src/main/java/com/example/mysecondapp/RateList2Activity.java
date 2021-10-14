package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RateList2Activity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "RateList2Activity";
    double rate; //汇率
    TextView subject;
    TextView input;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String detail = intent.getStringExtra("detail");
        Log.i(TAG, "onCreate: "+ title + "\n" + detail);

        rate = Double.parseDouble(detail)/100.0;//1外汇可以换多少元
        subject = findViewById(R.id.rate_list2_subject);
        input = findViewById(R.id.rate_list2_input);
        show = findViewById(R.id.rate_list2_show);

        subject.setText(title);
        input.addTextChangedListener(this);//设置监听
    }
    @Override
    public void afterTextChanged(Editable s) {
        double inputValue = Double.parseDouble(s.toString());//改变后的值
        show.setText(inputValue*rate+"元");
        Log.i(TAG, "afterTextChanged: "+rate);
    }

    public void click(View btn){
        Intent intent = getIntent();
        setResult(2000,intent);
        finish();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }//空实现

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }//空实现
}