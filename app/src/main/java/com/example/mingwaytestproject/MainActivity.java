package com.example.mingwaytestproject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button button, button2;
    private Handler mHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        mHander = new Handler();

        ThreadFactory factory = Executors.defaultThreadFactory();


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHander.removeCallbacks(task);
                mHander.postDelayed(task,50);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHander.removeCallbacks(task);
            }
        });
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Log.d("Mingway", ccc++ + "  === " + Thread.currentThread().getId());
            mHander.postDelayed(this, 1000);
        }
    };

    void shutdownAndAwaitTermination(ExecutorService pool) {

    }
    private volatile boolean interrupt = false;
    private int ccc = 0;
    private int bbb = 1;
    private void initThread() {

    }
}
