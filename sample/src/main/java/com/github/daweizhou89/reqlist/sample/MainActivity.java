package com.github.daweizhou89.reqlist.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.daweizhou89.reqlist.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.list1.setOnClickListener(this);
        binding.list2.setOnClickListener(this);
        binding.list3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list1:
                startActivity(new Intent(this, List1Activity.class));
                break;
            case R.id.list2:
                startActivity(new Intent(this, List2Activity.class));
                break;
            case R.id.list3:
                startActivity(new Intent(this, List3Activity.class));
                break;
        }
    }
}
