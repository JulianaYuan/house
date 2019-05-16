package com.juliana.house.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.juliana.house.model.House;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HousesDB {
    /**
     * database name
     */
    public static final String DB_NAME = "houses.db";
    /**
     * database version
     */
    public static final int VERSION = 2;
    private static HousesDB housesDB;
    private SQLiteDatabase db;

    /**
     *
     */
    private HousesDB(Context context){
        HouseOpenHelper dbHelper = new HouseOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /**
     *
     */
    public synchronized static HousesDB getInstance(Context context){
        if (housesDB == null){
            housesDB = new HousesDB(context);
        }
        return housesDB;
    }
    /**
     * add House into DB
     */
    public void addHouse(House house){
        if (house != null){
            ContentValues values = new ContentValues();
            values.put("district_name",house.getDistrictName());
            values.put("area",house.getArea());
            values.put("quoted_price",house.getQuotedPrice());
            values.put("real_price",house.getRealPrice());
            values.put("buying_price",house.getBuyingPrice());
            values.put("total_floors",house.getTotalFloors());
            values.put("real_floor",house.getRealFloor());
            values.put("location_type",house.getLocationType());
            values.put("is_first_sell",house.getIsFirstSell());
            values.put("is_normal_house",house.getIsNormalHouse());
            values.put("is_over5years",house.getIsOver5years());
            values.put("is_only_house",house.getIsOnlyHouse());
            values.put("is_over2years",house.getIsOver2years());
            values.put("deed_tax_rate",house.getDeedTaxRate());
            values.put("value_added_tax_rate",house.getValueAddedTaxRate());
            values.put("income_tax_rate",house.getIncomeTaxRate());
            values.put("agency_fee_rate",house.getAgencyFeeRate());
            values.put("other_handling_fee",house.getOtherHandlingFee());
            values.put("describe",house.getDescribe());
            values.put("house_design",house.getHouseDesign());
            db.insert("House",null,values);
        }
    }
    /**
     * save House into DB
     */
    public void saveHouse(House house){
        if (house != null){
            ContentValues values = new ContentValues();
            values.put("district_name",house.getDistrictName());
            values.put("area",house.getArea());
            values.put("quoted_price",house.getQuotedPrice());
            values.put("real_price",house.getRealPrice());
            values.put("buying_price",house.getBuyingPrice());
            values.put("total_floors",house.getTotalFloors());
            values.put("real_floor",house.getRealFloor());
            values.put("location_type",house.getLocationType());
            values.put("is_first_sell",house.getIsFirstSell());
            values.put("is_normal_house",house.getIsNormalHouse());
            values.put("is_over5years",house.getIsOver5years());
            values.put("is_only_house",house.getIsOnlyHouse());
            values.put("is_over2years",house.getIsOver2years());
            values.put("deed_tax_rate",house.getDeedTaxRate());
            values.put("value_added_tax_rate",house.getValueAddedTaxRate());
            values.put("income_tax_rate",house.getIncomeTaxRate());
            values.put("agency_fee_rate",house.getAgencyFeeRate());
            values.put("other_handling_fee",house.getOtherHandlingFee());
            values.put("describe",house.getDescribe());
            values.put("house_design",house.getHouseDesign());
            db.update("House",values,"id=?",new String[]{Integer.toString(house.getId())});
        }
    }
    /**
     * read houses info from DB
     */
    public List<House> loadHouse(){
        List<House> list = new ArrayList<House>();
        Cursor cursor = db.query("House",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                House house = new House();
                house.setId(cursor.getInt(cursor.getColumnIndex("id")));
                house.setTimeStamp(cursor.getString(cursor.getColumnIndex("time_stamp")));
                house.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
                house.setArea(cursor.getFloat(cursor.getColumnIndex("area")));
                house.setQuotedPrice(cursor.getFloat(cursor.getColumnIndex("quoted_price")));
                house.setRealPrice(cursor.getFloat(cursor.getColumnIndex("real_price")));
                house.setBuyingPrice(cursor.getFloat(cursor.getColumnIndex("buying_price")));
                house.setTotalFloors(cursor.getInt(cursor.getColumnIndex("total_floors")));
                house.setRealFloor(cursor.getInt(cursor.getColumnIndex("real_floor")));
                house.setLocationType(cursor.getInt(cursor.getColumnIndex("location_type")));
                house.setIsFirstSell(cursor.getInt(cursor.getColumnIndex("is_first_sell")));
                house.setIsNormalHouse(cursor.getInt(cursor.getColumnIndex("is_normal_house")));
                house.setIsOver5years(cursor.getInt(cursor.getColumnIndex("is_over5years")));
                house.setIsOnlyHouse(cursor.getInt(cursor.getColumnIndex("is_only_house")));
                house.setIsOver2years(cursor.getInt(cursor.getColumnIndex("is_over2years")));
                house.setDeedTaxRate(cursor.getDouble(cursor.getColumnIndex("deed_tax_rate")));
                house.setValueAddedTaxRate(cursor.getDouble(cursor.getColumnIndex("value_added_tax_rate")));
                house.setIncomeTaxRate(cursor.getFloat(cursor.getColumnIndex("income_tax_rate")));
                house.setAgencyFeeRate(cursor.getFloat(cursor.getColumnIndex("agency_fee_rate")));
                house.setOtherHandlingFee(cursor.getFloat(cursor.getColumnIndex("other_handling_fee")));
                house.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                house.setHouseDesign(cursor.getString(cursor.getColumnIndex("house_design")));
                list.add(house);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /**
     * read house info by house code from DB
     */
    public House readHouse(int id){
        Cursor cursor = db.query("House",null,"id=?",new String[]{Integer.toString(id)},null,null,null);
        cursor.moveToFirst();
        House house = new House();
        house.setId(cursor.getInt(cursor.getColumnIndex("id")));
        house.setTimeStamp(cursor.getString(cursor.getColumnIndex("time_stamp")));
        house.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
        house.setArea(cursor.getFloat(cursor.getColumnIndex("area")));
        house.setQuotedPrice(cursor.getFloat(cursor.getColumnIndex("quoted_price")));
        house.setRealPrice(cursor.getFloat(cursor.getColumnIndex("real_price")));
        house.setBuyingPrice(cursor.getFloat(cursor.getColumnIndex("buying_price")));
        house.setTotalFloors(cursor.getInt(cursor.getColumnIndex("total_floors")));
        house.setRealFloor(cursor.getInt(cursor.getColumnIndex("real_floor")));
        house.setLocationType(cursor.getInt(cursor.getColumnIndex("location_type")));
        house.setIsFirstSell(cursor.getInt(cursor.getColumnIndex("is_first_sell")));
        house.setIsNormalHouse(cursor.getInt(cursor.getColumnIndex("is_normal_house")));
        house.setIsOver5years(cursor.getInt(cursor.getColumnIndex("is_over5years")));
        house.setIsOnlyHouse(cursor.getInt(cursor.getColumnIndex("is_only_house")));
        house.setIsOver2years(cursor.getInt(cursor.getColumnIndex("is_over2years")));
        house.setDeedTaxRate(cursor.getDouble(cursor.getColumnIndex("deed_tax_rate")));
        house.setValueAddedTaxRate(cursor.getDouble(cursor.getColumnIndex("value_added_tax_rate")));
        house.setIncomeTaxRate(cursor.getFloat(cursor.getColumnIndex("income_tax_rate")));
        house.setAgencyFeeRate(cursor.getFloat(cursor.getColumnIndex("agency_fee_rate")));
        house.setOtherHandlingFee(cursor.getFloat(cursor.getColumnIndex("other_handling_fee")));
        house.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
        house.setHouseDesign(cursor.getString(cursor.getColumnIndex("house_design")));
        return house;
    }
    /**
     * delete house item by id from DB
     */
    public void deleteHouse(long id){
        db.delete("House","id=?",new String[]{Long.toString(id)});
    }
    /**
     * get db size by table
     */
    public int getDBTableHouseCount(){
        Cursor cursor = db.query("House",null,null,null,null,null,null);
        return cursor.getCount();
    }
    /**
     * close DB
     */
    public void closeHouseDB(){
        db.close();
    }
}
