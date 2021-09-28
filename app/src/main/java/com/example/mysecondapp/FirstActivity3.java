package com.example.mysecondapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirstActivity3 extends AppCompatActivity implements Runnable{


    private static final String TAG = "FirstActivity3";
    double dollar_rate = 0.1548;
    double euro_rate = 0.1321;
    double won_rate = 181.46;
    Handler handler;
    TextView showView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first3);
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); 从管理器中拿取,不推荐使用
        try{
            dollar_rate =Double.parseDouble(sharedPreferences.getString("dollar_rate","0.1548"));
            euro_rate =Double.parseDouble(sharedPreferences.getString("euro_rate","0.1321"));
            won_rate =Double.parseDouble(sharedPreferences.getString("won_rate","181.46"));
        }catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "onCreate: 数据转化失败");
        }
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if(msg.what == 6){
                   String str =(String) msg.obj;
                   showView = findViewById(R.id.first_show);
                   showView.setText(str);
                    Log.i(TAG, "handleMessage: "+str);
                }
                super.handleMessage(msg);
            }
        };
        //启动线程
        Thread thread = new Thread(this);
        thread.start();
    }



    public void click4(View btn){
       // Log.i(TAG, "click4: ");
        TextView subjectView = findViewById(R.id.first_subject);
        TextView showView = findViewById(R.id.first_show);
        if(subjectView.getText().toString().length()>0){
            double rmb = Double.parseDouble(subjectView.getText().toString());
            if(btn.getId() == R.id.first_btn_dollar){showView.setText(transfor(rmb*dollar_rate)+" 美元");}
            else if(btn.getId() == R.id.first_btn_euro){showView.setText(transfor(rmb*euro_rate)+" 欧元");}
            else if(btn.getId() == R.id.first_btn_won){showView.setText(transfor(rmb*won_rate)+" 韩元");}
        }
        else{
            showView.setText(R.string.RMBshow);
        }

    }

    private double transfor(double a){//保留两位小数
        BigDecimal b = new BigDecimal(a);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
    }

    public void open(View btn){
        Intent dispatcher = new Intent(this,SaveActivity4.class);
        dispatcher.putExtra("dollar_rate",dollar_rate);
        dispatcher.putExtra("euro_rate",euro_rate);
        dispatcher.putExtra("won_rate",won_rate);

        Log.i(TAG, "redirect: dollar_rate="+dollar_rate);
        Log.i(TAG, "redirect: euro_rate="+euro_rate);
        Log.i(TAG, "redirect: won_rate="+won_rate);

        startActivityForResult(dispatcher,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1000 && resultCode == 2000){
            dollar_rate = data.getDoubleExtra("dollar_rate2",0.0);
            euro_rate = data.getDoubleExtra("euro_rate2",0.0);
            won_rate = data.getDoubleExtra("won_rate2",0.0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_first,menu);
        return true;//菜单项有效
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting){
            Intent dispatcher = new Intent(this,SaveActivity4.class);
            dispatcher.putExtra("dollar_rate",dollar_rate);
            dispatcher.putExtra("euro_rate",euro_rate);
            dispatcher.putExtra("won_rate",won_rate);

            Log.i(TAG, "redirect: dollar_rate="+dollar_rate);
            Log.i(TAG, "redirect: euro_rate="+euro_rate);
            Log.i(TAG, "redirect: won_rate="+won_rate);

            startActivityForResult(dispatcher,1000);
        }//就是open()里面的方法
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        Log.i(TAG, "run: 线程启动");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = null;
        try{
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = InputStream2String(in);
            Log.i(TAG, "run: "+html);

        } catch (IOException malformedURLException){
            malformedURLException.printStackTrace();
            Log.i(TAG, "run: 访问出错");
        }
        //发送消息
        Message msg = handler.obtainMessage(6);


        msg.obj = "HELLO FROM RUN";
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已发送");
    }

    private String InputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}