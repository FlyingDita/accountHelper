package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class queryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_query_data);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Button queryAllBotton = findViewById(R.id.queryAll);
        queryAllBotton.setOnClickListener(v -> GetAll(databaseHelper));

        // 返回主界面
        Button buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(queryActivity.this, ts.class);
                startActivity(intent);
            }
        });

    }


    private void GetAll(DatabaseHelper databaseHelper) {
        // 实现查询操作
        List<String> stringList= databaseHelper.getAllData();

        StringBuilder stringBuilder = new StringBuilder();
        for (String item : stringList) {
            stringBuilder.append(item).append("\n\n\n"); // 添加换行符
        }
        TextView textView = findViewById(R.id.txtOne);
        textView.setText(stringBuilder.toString());
    }
}


