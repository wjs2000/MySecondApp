package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity5 extends ListActivity implements Runnable{
    private static final String TAG = "MyListActivity";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        List<String> list1 = new ArrayList<String>();
        for(int i = 1;i<100;i++){
            list1.add("item"+i);
        }
        //创建线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==0){
                    ArrayList<String>list2 = (ArrayList<String>) msg.obj;
                    Log.i("list", "handleMessage: "+list2);
                    ListAdapter adapter = new ArrayAdapter<String>(
                            MyListActivity5.this,
                            android.R.layout.simple_expandable_list_item_1,
                            list2
                    );
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

    }


    @Override
    public void run() {
        Document doc = null;
        List<String> list = new ArrayList<>();
        try {
            for(int i = 0;i<10;i++){//一共有10页,所以10次循环
                if(i==0)
                    doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/index.html").get();
                else
                    doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/index_"+i+".html").get();
                Elements tables = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");
                tables.remove(0);
                for (Element table : tables) {
                    String name = table.getElementsByTag("td").get(0).text();
                    double val =transfor(100.0/Double.parseDouble(table.getElementsByTag("td").get(5).text()));
                    list.add(name+"\t====>\t"+val);
                }
            }
            for (String s : list) {
                Log.i(TAG, s);
            }
            Message msg = handler.obtainMessage(0);
            msg.obj = list;
            handler.sendMessage(msg);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "run: wrong");
        }

    }
    private double transfor(double a){//保留两位小数
        BigDecimal b = new BigDecimal(a);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
    }
}