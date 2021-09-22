package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    public void transform(View view){
//        TextView textView = findViewById(R.id.subject);
//        String s = textView.getText().toString();
//        double result = 32+1.8*Integer.parseInt(s);
//
//        TextView textView1 = findViewById(R.id.result);
//        textView1.setText("转换结果为:"+result);
//
//    }
    public void transform2(View view){
        TextView highView = findViewById(R.id.subject);//身高
        TextView weightView = findViewById(R.id.subject2);//体重
        TextView resultView = findViewById(R.id.result);//结果BMI
        TextView resultView2 = findViewById(R.id.result2);//结果建议
        TextView errView = findViewById(R.id.err);//报错信息
        errView.setText("");
        String high = highView.getText().toString();
        String weight = weightView.getText().toString();
        double BMI;
        try{
            BMI = Double.parseDouble(weight) / (Double.parseDouble(high)*Double.parseDouble(high));
        }catch (Exception e){
            errView.setText("输入不和法");
            resultView.setText("BMI结果为:");
            resultView2.setText("您的健康状况为:");
            return;
        }
        BigDecimal b = new BigDecimal(BMI);
        BMI = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数

        String flag;
        if(BMI < 18.5)
            flag = "过轻";
        else if (BMI < 23.9)
            flag = "正常";
        else if(BMI < 27.9)
            flag = "超重";
        else
            flag = "肥胖";

        resultView.setText("BMI结果为:"+BMI);
        resultView2.setText("您的健康状况为:"+flag);
    }
}