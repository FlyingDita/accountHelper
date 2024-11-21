package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Button DeleteAllBotton = findViewById(R.id.deteleAll);
        DeleteAllBotton.setOnClickListener(v -> deleteAllData(databaseHelper));


        // 返回主界面
        Button buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(DeleteActivity.this, ts.class);
                startActivity(intent);
            }
        });
    }


    private void deleteAllData(DatabaseHelper databaseHelper){
        databaseHelper.deleteALLData();
        Toast.makeText(DeleteActivity.this, "所有数据删除成功", Toast.LENGTH_SHORT).show();
    }
}