package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;

public class FirstActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first3);
    }


    public void click4(View btn){
        TextView subjectView = findViewById(R.id.first_subject);
        TextView showView = findViewById(R.id.first_show);
        double rmb = Double.parseDouble(subjectView.getText().toString());
        if(btn.getId() == R.id.first_btn_dollar){showView.setText(transfor(rmb*0.1548)+" 美元");}
        else if(btn.getId() == R.id.first_btn_euro){showView.setText(transfor(rmb*0.1321)+" 欧元");}
        else if(btn.getId() == R.id.first_btn_won){showView.setText(transfor(rmb*181.46)+" 韩元");}
    }

    private double transfor(double a){//保留两位小数
        BigDecimal b = new BigDecimal(a);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
    }
}