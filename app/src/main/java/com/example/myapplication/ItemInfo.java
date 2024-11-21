
package com.example.myapplication;

//import java.time.LocalDate;

public class ItemInfo {
    public int id;
    public String date;
    public Float amount;
    public String type;
    public String detail;

    public ItemInfo() {
        this.id = -1;
        this.date =  "";
        this.amount = 0.0f;
        this.type = "";
        this.detail = "";
    }
}