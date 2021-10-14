package com.example.mysecondapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BallActivity2 extends AppCompatActivity {

    private static final String TAG = "BallActivity2";
    int score = 0;
    int score2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball2);
    }

    public void click(View btn){
        Log.i(TAG, "click: ");
        if (btn.getId() == R.id.but_add3){
            score += 3 ;
        }
        else if(btn.getId() == R.id.but_add2){
            score += 2;
        }
        else if(btn.getId() == R.id.but_add1){
            score += 1;
        }

        TextView ballShowView = findViewById(R.id.ball_show);
        ballShowView.setText(score+"");
    }

    public void click2(View btn){
        Log.i(TAG, "click: ");
        if (btn.getId() == R.id.but_add32){
            score2 += 3 ;
        }
        else if(btn.getId() == R.id.but_add22){
            score2 += 2;
        }
        else if(btn.getId() == R.id.but_add12){
            score2 += 1;
        }

        TextView ballShowView2 = findViewById(R.id.ball_show2);
        ballShowView2.setText(score2+"");
    }

    public void click3(View btn){
        TextView ballShowView = findViewById(R.id.ball_show);
        TextView ballShowView2 = findViewById(R.id.ball_show2);
        ballShowView2.setText(0+"");
        ballShowView.setText(0+"");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score1",score);
        outState.putInt("score2",score2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int score1 = savedInstanceState.getInt("score1", 0);
        int score2 = savedInstanceState.getInt("score2", 0);
        TextView ballShowView = findViewById(R.id.ball_show);
        TextView ballShowView2 = findViewById(R.id.ball_show2);
        ballShowView2.setText(score1);
        ballShowView.setText(score2);

    }
}