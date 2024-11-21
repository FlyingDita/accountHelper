package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ModifyActivity extends AppCompatActivity {
    private EditText editTextId,editTextDate,editTextAmount,editTextType,editTextDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        GetAll(databaseHelper);
        ItemInfo ITEMinfo = new ItemInfo();



        // 获取EditText对象
        editTextId = findViewById(R.id.editTextId);
        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextType = findViewById(R.id.editTextType);
        editTextDetail = findViewById(R.id.editTextDetail);
        Button submitButton = findViewById(R.id.submit); // 假设有一个提交按钮

        // 设置按钮点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打印调试信息
                Log.d("ITEMinfo.date", "开始获取输入");

                // 尝试获取amount并转换为Float
                try {
                    String amountStr = editTextAmount.getText().toString();
                    if (amountStr.isEmpty()) {
                        throw new NumberFormatException("金额不能为空");
                    }
                    ITEMinfo.amount = Float.parseFloat(amountStr);
                    Log.d("ITEMinfo.amount", "Amount: " + ITEMinfo.amount);
                } catch (NumberFormatException e) {
                    Log.e("ITEMinfo.date", "无效的金额输入: " + e.getMessage());
                    Toast.makeText(ModifyActivity.this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
                    return; // 退出点击事件，避免继续执行
                }

                // 获取类型和细节
                ITEMinfo.id = Integer.parseInt(editTextId.getText().toString());
                ITEMinfo.date = editTextDate.getText().toString();
                ITEMinfo.type = editTextType.getText().toString();
                ITEMinfo.detail = editTextDetail.getText().toString();
                ModifyByDate(ITEMinfo,databaseHelper);
            }
        });


        // 返回主界面
        Button buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(ModifyActivity.this, ts.class);
                startActivity(intent);
            }
        });
    }

    private void ModifyByDate(ItemInfo iteminfo,DatabaseHelper databaseHelper) {
        // 实现修改操作
        databaseHelper.updateData(iteminfo.id, iteminfo.date, iteminfo.amount,iteminfo.type,iteminfo.detail);
        String datainfo = String.valueOf(iteminfo.id) + " "+ iteminfo.date + " "+ String.valueOf(iteminfo.amount) + " "+ iteminfo.type + " "+ iteminfo.detail;
        Toast.makeText(ModifyActivity.this, "数据修改成功:  "+ datainfo, Toast.LENGTH_SHORT).show();
    }

    private void GetAll(DatabaseHelper databaseHelper) {
        // 实现查询操作
        List<String> stringList= databaseHelper.getAllData(10);

        StringBuilder stringBuilder = new StringBuilder();
        for (String item : stringList) {
            stringBuilder.append(item).append("\n"); // 添加换行符
        }
        TextView textView = findViewById(R.id.txtOne);
        textView.setText(stringBuilder.toString());
        for (String item : stringList) {
            Log.d("StringList", item);
        }
    }
}