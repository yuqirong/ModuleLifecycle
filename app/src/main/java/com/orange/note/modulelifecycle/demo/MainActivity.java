package com.orange.note.modulelifecycle.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orange.note.lifecycle.annotation.Module;

@Module
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
