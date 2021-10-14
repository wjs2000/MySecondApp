package com.example.mysecondapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateListActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener {

    private static final String TAG = "RateListActivity";
    Handler handler = new Handler();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list);

        Thread thread = new Thread(this);
        thread.start();
        //数据
//        List<HashMap<String,String>> listItem = new ArrayList<>();
//        for(int i =0 ; i<10 ; i++){
//            HashMap<String,String> map = new HashMap<>();
//            map.put("item","Rate"+(i+1));
//            map.put("detail","detail:"+(i+1));
//            listItem.add(map);
//        }
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 0){
                    List<HashMap<String,String>> listItem =(List<HashMap<String,String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(RateListActivity.this,
                            listItem,  //数据集合
                            R.layout.list_item,  //子activity
                            new String[]{"title","detail"},  //键值
                            new int[]{R.id.itemTitle,R.id.itemDetail});  //控件id

                    listView = findViewById(R.id.listview_rate);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(RateListActivity.this);//给listview添加点击事件监听
                }
                super.handleMessage(msg);
            }
        };
        //适配器

    }

    @Override
    public void run() {
        Document doc = null;
        List<HashMap<String,String>> listItem = new ArrayList<>();
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
                    String val =table.getElementsByTag("td").get(5).text();
                    HashMap<String,String> map = new HashMap<>();
                    map.put("title",name);
                    map.put("detail",val);
                    listItem.add(map);
                }
            }
            Message msg = handler.obtainMessage(0);
            msg.obj = listItem;
            handler.sendMessage(msg);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "run: wrong");
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>) itemAtPosition;
        String title = map.get("title");
        String detail = map.get("detail");
        Log.i(TAG, "onItemClick: "+title + "\n" + detail);

        Intent intent = new Intent(this,RateList2Activity.class);
        intent.putExtra("title",title);
        intent.putExtra("detail",detail);
        int requestCode = 1000;
        startActivityForResult(intent,requestCode);
    }
}