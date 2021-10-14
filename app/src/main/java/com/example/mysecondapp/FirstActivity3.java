package com.example.mysecondapp;

import static android.media.CamcorderProfile.get;

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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    //edit.putLong("date",0L);//将序列化的时间初始化的,调试用
                    Date date_start = new Date(sharedPreferences.getLong("date",0L));//获取序列化的时间
                    Date now_day = new Date();//获取登录的时间
                    Log.i(TAG, "handleMessage: "+new Timestamp(now_day.getTime()/(1000*3600*24)*(1000*3600*24))
                    +"\n"+ new Timestamp(date_start.getTime()/(1000*3600*24)*(1000*3600*24)));

                    if(((now_day.getTime()- date_start.getTime())/1000/60/60/24)>=1){//判断是否超过一天
                        Bundle data = msg.getData();//获取bundle对象,含有rate
                        String[] rates = data.getStringArray("rate");
                        dollar_rate =Double.parseDouble((String)rates[0]) ;
                        euro_rate =Double.parseDouble((String)rates[1]) ;
                        won_rate =Double.parseDouble((String)rates[2]) ;
                        edit.putString("dollar_rate",dollar_rate+"");
                        edit.putString("euro_rate",euro_rate+"");
                        edit.putString("won_rate",won_rate+"");
                        edit.putLong("date",now_day.getTime()/(1000*3600*24)*(1000*3600*24));//把今天凌晨的时间放入date中
                        edit.apply();
                        Toast.makeText(getApplicationContext(),"今日汇率已更新",Toast.LENGTH_SHORT).show();//弹窗
                    }
                    else
                        Log.i(TAG, "handleMessage: 未超过一天,不更新");
                }
//                if (msg.what == 5){
//                    Object[] rates =  (Object[]) msg.obj;
//                    dollar_rate =Double.parseDouble((String)rates[0]) ;
//                    euro_rate =Double.parseDouble((String)rates[1]) ;
//                    won_rate =Double.parseDouble((String)rates[2]) ;
//
//                }
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
//        URL url = null;
//        try{
//            url = new URL("https://www.usd-cny.com/bankofchina.htm");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//            String html = InputStream2String(in);
//            Log.i(TAG, "run: "+html);
//
//        } catch (IOException malformedURLException){
//            malformedURLException.printStackTrace();
//            Log.i(TAG, "run: 访问出错");
//        }
        //Jsoup
        Document doc = null;
        Message msg = handler.obtainMessage(6);
        String []sum = {"美元","欧元","韩币"};//sum就是要打印的币种汇率
        Set<String> set = new HashSet<>(Arrays.asList(sum));
        try {
            doc = Jsoup.connect("https://usd-cny.com").get();
            Log.i(TAG, "run: "+doc.title());
            Element tables = doc.getElementsByTag("table").first();
            Elements trs = tables.getElementsByTag("tr");
            trs.remove(0);
            List<String> rate = new ArrayList<>();//存汇率
            Bundle bundle = new Bundle();
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                Element td1 = tds.get(0);
                Element td2 = tds.get(4);
                if (set.contains(td1.text())) {
                    Log.i(TAG, "run: 汇率:" + td1.text() + "==>" + td2.text());
                    rate.add(String.valueOf  (transfor(100.0/Double.parseDouble(td2.text())) ) );
                }
            }
            bundle.putStringArray("rate",rate.toArray(new String[0]));
            msg.setData(bundle);
//            Elements ths = firstTable.getElementsByTag("th");
//            for (Element th : ths) {
//                Log.i(TAG, "run: "+th);
//                Log.i(TAG, "run: html= "+th.html());
//                Log.i(TAG, "run: text= "+th.text());//只保留文本项,去除标签
//            }
//            Element th2 = ths.get(1);
//            Log.i(TAG, "run: th2="+th2);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Log.i(TAG, "run: jsoup失败");
        }
        //发送消息
        msg.obj = "HELLO FROM RUN";
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息msg已发送");
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

    private double transfor(double a){//保留两位小数
        BigDecimal b = new BigDecimal(a);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
    }
}