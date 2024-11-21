package com.example.myapplication;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.graphics.Color;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.util.ArrayList;
import java.util.Map;
import android.content.Intent;




public class ts extends AppCompatActivity {
    private TextView textView;

    Map<String, Float> spendingData = new HashMap<>();

    Map<String, Integer> spendingTypeData = new HashMap<>();
    private PieChart pieChart;

    private Button checkSitButton, deleteButton, modifyButton;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ts);


        // 直方图设计
        BarChart barChart = findViewById(R.id.barChart);
        barChart.setDescription(null);
        getStaticInfo();
        ArrayList<BarEntry> entries = new ArrayList<>();

        List<String> Categories = new ArrayList<>();
        List<Float> amounts = new ArrayList<>();

        for (Map.Entry<String, Float> entry : spendingData.entrySet()) {

            String category = entry.getKey();
            String s1 = category.substring(category.length() - 5);
            Float amount = entry.getValue();
            amounts.add(amount);
            Categories.add(s1);
        }
        int i = -1;
        for (int j = amounts.size() - 1; j >= 0; j--)
        {
            i++;
            entries.add(new BarEntry(i, amounts.get(j)));
        }
        Collections.reverse(Categories);


//        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable/1.png); // 确保是 JPG 文件
//        lineChart.setBackground(new BitmapDrawable(getResources(), background))


        if (entries.isEmpty()) {
            // 设置没有数据时显示的文本
            barChart.setNoDataText("没有数据");
        }
        else {
            // 创建数据条目
            // 设置 X 轴标签
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(Categories));
            xAxis.setGranularity(1f); // 设置粒度为1，确保X轴只显示整数
            xAxis.setGranularityEnabled(true); // 启用粒度
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴位置
            // 创建数据集
            BarDataSet dataSet = new BarDataSet(entries, "当日消费总金额 （x：日期 y：当日消费总额）");
            dataSet.setColor(Color.BLUE); // 设置条形颜色
            // 创建 BarData
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.1f);
            barChart.setData(barData);
            barChart.getAxisLeft().setDrawGridLines(false); // 取消左侧Y轴网格线
            barChart.getXAxis().setDrawGridLines(false); // 取消X轴网格线
            barChart.invalidate(); // 刷新图表
        }

        // 饼图
        setupPieChart(spendingTypeData);

        // 更新近一周日均消费
        getAverageC(databaseHelper);


        // 设置跳转
        // insert
        Button buttonNextInsert = findViewById(R.id.button_next_insert);
        buttonNextInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(ts.this, InsertActivity.class);
                startActivity(intent);
            }
        });


        // query
        Button buttonNextQuery = findViewById(R.id.button_next_query);
        buttonNextQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(ts.this, queryActivity.class);
                startActivity(intent);
            }
        });

        // delete
        deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(ts.this, DeleteActivity.class);
                startActivity(intent);
            }
        });


        // modify
        modifyButton = findViewById(R.id.modify);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent以启动SecondActivity
                Intent intent = new Intent(ts.this, ModifyActivity.class);
                startActivity(intent);
            }
        });
    };


    // 获取统计数据
    private void getStaticInfo() {
        spendingData = databaseHelper.getLastFiveD();
        spendingTypeData = databaseHelper.getTypeInfo();
    }

    private void setupPieChart(Map<String, Integer> spendingTypeData) {
        // 创建数据项
        pieChart = findViewById(R.id.pieChart1);

        ArrayList<PieEntry> entries = new ArrayList<>();


        for (Map.Entry<String, Integer> entry : spendingTypeData.entrySet()) {

            String type = entry.getKey();

            int num = entry.getValue();
            entries.add(new PieEntry(num, type));
        }

        // 创建数据集
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.parseColor("#FFA500"),

                Color.parseColor("#FFC0CB"),
                //浅蓝色
                Color.parseColor("#ADD8E6")

        }); // 设置颜色
        PieData data = new PieData(dataSet);

        // 设置数据到饼图
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(false); // 取消在饼图上显示类型
        pieChart.setData(data);
        pieChart.invalidate(); // 刷新图表
    }

    private void getAverageC(DatabaseHelper databaseHelper) {
        // 实现查询操作
        Float averageC= databaseHelper.getAverageC();
        Log.d("tsinfo: ", averageC.toString());
        TextView textView = findViewById(R.id.txtWeekAverageAm);
        String currentText = textView.getText().toString();
        StringBuilder stringBuilder = new StringBuilder(currentText);
        stringBuilder.append("\n").append(averageC.toString()); // 添加换行符
        Log.d("tsinfo: ", stringBuilder.toString());
        textView.setText(stringBuilder.toString());
    }
}



