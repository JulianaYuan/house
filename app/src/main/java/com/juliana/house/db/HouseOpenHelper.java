package com.juliana.house.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

public class HouseOpenHelper extends SQLiteOpenHelper{
    /**
     * house create table string
     */
    public static final String CREATE_HOUSE = "create table House ("
            +"id integer primary key autoincrement, "
            +"time_stamp NOT NULL DEFAULT (datetime('now','localtime')), "
            +"district_name text,"
            +"area real,"
            +"quoted_price real,"
            +"real_price real,"
            +"buying_price real,"
            +"total_floors integer,"
            +"real_floor integer,"
            +"location_type integer,"
            +"is_first_sell integer,"
            +"is_normal_house integer,"
            +"is_over5years integer,"
            +"is_only_house integer,"
            +"is_over2years integer,"
            +"deed_tax_rate real,"
            +"value_added_tax_rate real,"
            +"income_tax_rate real,"
            +"agency_fee_rate real,"
            +"other_handling_fee real,"
            +"describe text,"
            +"house_design text)";

    public HouseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_HOUSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        switch (oldVersion){
            case 1:
                String sql = "Alter table House add column time_stamp TEXT ";
                db.execSQL(sql);
            default:
        }

    }
}
