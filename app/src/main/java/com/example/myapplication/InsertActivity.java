package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.time.LocalDate;

public class InsertActivity extends AppCompatActivity {

//    LocalDate localDate;

    String selectedDate;
    private EditText editTextAmount,editTextType,editTextDetail;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert);

        ItemInfo ITEMinfo = new ItemInfo();

        Button buttonGetdate = findViewById(R.id.buttonGetdate);
        // 弹出日期选择 并 获取日期
        buttonGetdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDatePickerDialog();
            }
        });

//        Log.d("ITEMinfo.date","yesyes");
//        editTextAmount = findViewById(R.id.editTextAmount);
//        editTextType = findViewById(R.id.editTextType);
//        editTextDetail = findViewById(R.id.editTextDetail);
//        Log.d("ITEMinfo.date","yesyes1");
//        ITEMinfo.amount = Float.parseFloat(editTextAmount.getText().toString());
//        Log.d("ITEMinfo.date","yesyes2");
//        ITEMinfo.type = editTextType.getText().toString();
//        ITEMinfo.detail = editTextDetail.getText().toString();



        // 获取EditText对象
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
                    Toast.makeText(InsertActivity.this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
                    return; // 退出点击事件，避免继续执行
                }

                // 获取类型和细节
                ITEMinfo.type = editTextType.getText().toString();
                ITEMinfo.detail = editTextDetail.getText().toString();

                // 假设selectedDate已经定义
                ITEMinfo.date = selectedDate;

                // 打印最终信息
                Log.d("ITEMinfo.date", "信息已更新: " + ITEMinfo.date);

                // 写入数据
                databaseHelper.insertData(ITEMinfo);
                Toast.makeText(InsertActivity.this, "数据插入成功"+ITEMinfo.date, Toast.LENGTH_SHORT).show();
                editTextAmount.setText("");
                editTextType.setText("");
                editTextDetail.setText("");
            }
        });





        // 返回主界面
        Button buttonBack = findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(InsertActivity.this, ts.class);
                startActivity(intent);
            }
        });
    }



    private void showDatePickerDialog() {
        // 获取当前日期
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        // 创建日期选择器
        @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(this,2,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // 处理用户选择的日期

                        selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        Log.d("selectedDate",selectedDate);

                        // 在这里可以更新 UI 或处理选中的日期
                    }
                }, year, month, day);

        // 显示日期选择器
        datePickerDialog.show();
    }

}