package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.database.Cursor;
import android.util.Log;
import android.util.Pair;

import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat; // 导入SimpleDateFormat类
import java.text.ParseException;



public class DatabaseHelper extends SQLiteOpenHelper {
    //定义数据库名和版本号
    private static final String DBNAME="student.db";
    private static final int VERSION=1;

    // 数据库设计
    public static final String TABLE_NAME = "MYTABLE";
    public static final String COLUMN_ID = "id"; // 自增主键
    public static final String COLUMN_TIME = "time"; // 时间
    public static final String COLUMN_AMOUNT = "payamount"; // 金额
    public static final String COLUMN_TYPE = "paytype";// 消费类型
    public static final String COLUMN_DETAIL = "paydetail";//消费细节
    // 创建数据表的SQL语句
    private static final String CREATE_TABLE_STA_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TIME + " TEXT NOT NULL," +
                    COLUMN_AMOUNT + " FLOAT," +
                    COLUMN_TYPE + " TEXT," +
                    COLUMN_DETAIL + " TEXT" +
                    ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
    // 创建数据表
        Log.d("logging", "yesyes4");
        db.execSQL(CREATE_TABLE_STA_TABLE);

        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_TIME + ", " +
                COLUMN_AMOUNT + ", " +
                COLUMN_TYPE + ", " +
                COLUMN_DETAIL + ") VALUES ('2024-11-14 10:00:00', 100, 'Income', 'Initial deposit')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_TIME + ", " +
                COLUMN_AMOUNT + ", " +
                COLUMN_TYPE + ", " +
                COLUMN_DETAIL + ") VALUES ('2024-11-14 14:00:00', 100, 'output', 'food')");
    }

    // 构造函数
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }
    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }




    // 返回一个哈希表，最近七天的消费总额和对应的时间
    public Map<String,Float> getLastFiveD(){
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Float> spendingData = new HashMap<>();
        Cursor cursor = db.rawQuery(
//                "SELECT * FROM MYTABLE"
//                "SELECT time,payamount  FROM MYTABLE"


                "SELECT DATE(time) AS transaction_date, " +
                        "SUM(payamount) AS total_spending " +
                        "FROM MYTABLE "
                        +
                        "WHERE DATE(time) >= DATE('now', '-7 days') " +
                        "GROUP BY DATE(time) " +
                        "ORDER BY transaction_date DESC"
                ,
                null
        );


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                Float totalAmount = cursor.getFloat(1);
                spendingData.put(date, totalAmount);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spendingData;
    }

    // 返回一个哈希表，最近一个月的消费方式的计数
    public Map<String,Integer> getTypeInfo(){
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String,Integer> spendingTypeData = new HashMap<>();
        Cursor cursor = db.rawQuery(
                "SELECT paytype,COUNT(*) AS count " +
                        "FROM MYTABLE "
                        +
                        "WHERE DATE(time) >= DATE('now', '-30 days') "+
                        "GROUP BY paytype;"
                ,
                null
        );


        if (cursor.moveToFirst()) {
            do {
                String paytype = cursor.getString(0);
                int num = cursor.getInt(1);
                spendingTypeData.put(paytype, num);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spendingTypeData;
    }


    // 返回日均消费
    public Float getAverageC(){
        SQLiteDatabase db = this.getReadableDatabase();
        Float averageC = 0.0f;
        Log.d("tsinfo: ", "yesyes");
        Cursor cursor = db.rawQuery(
                "SELECT AVG(payamount) " +
                        "FROM MYTABLE "
                        +
                        "WHERE DATE(time) >= DATE('now', '-7 days') ;"
                ,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                averageC = cursor.getFloat(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return averageC;
    }



    // 增删改查方法
    // query
    // 1. 查看总表
    public List<String> getAllData() {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM MYTABLE", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // 获取第一列（id）
                String date = cursor.getString(1); // 获取第二列（name）
                Float amount = cursor.getFloat(2);
                String type = cursor.getString(3);
                String detail = cursor.getString(4);

                dataList.add("ID: " + id + ", Date: " + date + ", Amount: "+amount.toString() + ", Type: "+type + ", Detail: "+detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }

    // 2. 查询最近n条消费记录
    public List<String> getAllData(int num) {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM MYTABLE LIMIT 10", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // 获取第一列（id）
                String date = cursor.getString(1); // 获取第二列（name）
                Float amount = cursor.getFloat(2);
                String type = cursor.getString(3);
                String detail = cursor.getString(4);

                dataList.add("ID: " + id + ", Date: " + date + ", Amount: "+amount.toString() + ", Type: "+type + ", Detail: "+detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }


    // insert
    void insertData(ItemInfo iteminfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, iteminfo.date);
        values.put(COLUMN_AMOUNT, iteminfo.amount);
        values.put(COLUMN_TYPE, iteminfo.type);
        values.put(COLUMN_DETAIL, iteminfo.detail);
        long newRowId = db.insert(TABLE_NAME, null, values);

        db.close();
    }

    // delete
    void deleteALLData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM MYTABLE");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='MYTABLE'");
        db.close();
    }

    // modify
    void updateData(int id,String time,double amount, String type,String detail){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DETAIL, detail);

        // 更新条件：这里假设 id 是唯一标识符
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }
}