package com.juliana.house.model;


import android.media.Image;
import android.util.Log;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class House {
    private int id;
    private String timeStamp;
    private String districtName;

    private Float area;
    private Float quotedPrice;
    private Float realPrice;
    private Float buyingPrice;
    private Integer totalFloors;
    private Integer realFloor;
    private Integer locationType;
    private Integer isFirstSell;//for seller
    private Integer isNormalHouse;
    private Integer isOver5years;
    private Integer isOnlyHouse;
    private Integer isOver2years;

    private Double deedTaxRate;
    private Double valueAddedTaxRate;
    private Float incomeTaxRate;
    private Float agencyFeeRate;
    private Float otherHandlingFee;

    private String describe;
    private String houseDesign;//image path

    private boolean evadeTax;
    private boolean isFirstBuy;//for buyer

    public House(){
        id = 0;
        timeStamp = new String("2019-01-01 00:00");
        districtName = new String("null");

        area = new Float(0);
        quotedPrice = new Float(0);
        realPrice = new Float(0);
        buyingPrice = new Float(0);
        totalFloors = new Integer(0);
        realFloor = new Integer(0);
        locationType = new Integer(0);
        isFirstSell = new Integer(0);
        isNormalHouse = new Integer(0);
        isOver5years = new Integer(0);
        isOnlyHouse = new Integer(0);
        isOver2years = new Integer(0);

        deedTaxRate = new Double(0);
        valueAddedTaxRate = new Double(0);
        incomeTaxRate = new Float(0);
        agencyFeeRate = new Float(2);
        otherHandlingFee = new Float(10000);

        describe = new String("null");
        houseDesign = new String("null");
        evadeTax = false;
        isFirstBuy = true;
    }

    public String getTimeStamp(){
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getDistrictName(){
        return districtName;
    }
    public void setDistrictName(String districtName){
        this.districtName = districtName;
    }
    public Float getArea(){
        return area;
    }
    public void setArea(Float area){
        this.area = area;
    }
    public Float getQuotedPrice(){
        return quotedPrice;
    }
    public void setQuotedPrice(Float quotedPrice){
        this.quotedPrice = quotedPrice;
    }
    public Float getRealPrice(){
        return realPrice;
    }
    public void setRealPrice(Float realPrice){
        this.realPrice = realPrice;
    }
    public Float getBuyingPrice(){
        if (isFirstSell==1)
            return calTaxPrice();
        else return buyingPrice;
    }
    public void setBuyingPrice(Float buyingPrice){
        this.buyingPrice = buyingPrice;
    }
    public int getTotalFloors(){
        return totalFloors;
    }
    public void setTotalFloors(int totalFloors){
        this.totalFloors = totalFloors;
    }
    public int getRealFloor(){
        return realFloor;
    }
    public void setRealFloor(int realFloor){
        this.realFloor = realFloor;
    }
    public int getLocationType(){
        return locationType;
    }
    public void setLocationType(int locationType){
        this.locationType = locationType;
    }
    public Integer getIsFirstSell(){
        return isFirstSell;
    }
    public void setIsFirstSell(Integer isFirstSell){
        this.isFirstSell = isFirstSell;
    }
    public Integer getIsNormalHouse(){
        return isNormalHouse;
    }
    public void setIsNormalHouse(Integer isNormalHouse){
        this.isNormalHouse = isNormalHouse;
    }
    public Integer getIsOver5years(){
        return isOver5years;
    }
    public void setIsOver5years(Integer isOver5years){
        this.isOver5years = isOver5years;
    }
    public Integer getIsOnlyHouse(){
        return isOnlyHouse;
    }
    public void setIsOnlyHouse(Integer isOnlyHouse){
        this.isOnlyHouse = isOnlyHouse;
    }
    public Integer getIsOver2years(){
        if (isOver5years==1)isOver2years=1;
        return isOver2years;
    }
    public void setIsOver2years(Integer isOver2years){
        this.isOver2years = isOver2years;
    }

    public Double getDeedTaxRate(){
        return deedTaxRate;
    }
    public void setDeedTaxRate(Double deedTaxRate){
        this.deedTaxRate = deedTaxRate;
    }
    public Double getValueAddedTaxRate(){
        return valueAddedTaxRate;
    }
    public void setValueAddedTaxRate(Double valueAddedTaxRate){
        this.valueAddedTaxRate = valueAddedTaxRate;
    }
    public Float getIncomeTaxRate(){
        return incomeTaxRate;
    }
    public void setIncomeTaxRate(Float incomeTaxRate){
        this.incomeTaxRate = incomeTaxRate;
    }
    public Float getAgencyFeeRate(){
        return agencyFeeRate;
    }
    public void setAgencyFeeRate(Float agencyFeeRate){
        this.agencyFeeRate = agencyFeeRate;
    }
    public Float getOtherHandlingFee(){
        return otherHandlingFee;
    }
    public void setOtherHandlingFee(Float otherHandlingFee){
        this.otherHandlingFee = otherHandlingFee;
    }

    public String getDescribe(){
        return describe;
    }
    public void setDescribe(String describe){
        this.describe = describe;
    }
    public String getHouseDesign(){
        return houseDesign;
    }
    public void setHouseDesign(String houseDesign){
        this.houseDesign = houseDesign;
    }
    public boolean getEvadeTax(){
        return evadeTax;
    }
    public void setEvadeTax(boolean evadeTax){
        this.evadeTax = evadeTax;
    }
    public boolean getIsFirstBuy(){
        return isFirstBuy;
    }
    public void setIsFirstBuy(boolean isFirstBuy){
        this.isFirstBuy = isFirstBuy;
    }

    public void calcHouse()
    {
        setIsNormalHouse(calIsNormalHouse());
        setDeedTaxRate(calDeedTaxRate());
        setValueAddedTaxRate(calValueAddedRate());
        setIncomeTaxRate(calIncomeTaxRate());

    }
    public int calIsNormalHouse()
    {
        AtomicInteger isNormalHouse = new AtomicInteger();
        if((area<=140)&&
                ((locationType==3)&&(realPrice<=230))||
                ((locationType==2)&&(realPrice<=310))||
                ((locationType==1)&&(realPrice<=450))||(evadeTax==true)) {
            isNormalHouse.set(1);
        }
        else if(locationType==0){
            isNormalHouse.set(0);
        }else{
            isNormalHouse.set(2);
        }
        return isNormalHouse.get();
    }

    public Double calDeedTaxRate(){
        AtomicReference<Double> tax = new AtomicReference<>(Double.valueOf(3));
        if((isFirstBuy==true)&&(area<=90))
        {
            tax.set(Double.valueOf(1));
        }else if((isFirstBuy==true)&&(area>90)){
            tax.set(Double.valueOf(1.5));
        }else{
            tax.set(Double.valueOf(3));
        }
        return tax.get();
    }
    public Double calDeedTax(){
        AtomicReference<Double> tax = new AtomicReference<>(0.0);
        tax.set(calTaxPrice() * deedTaxRate/100);
        return tax.get();
    }
    public Double calValueAddedRate(){
        AtomicReference<Double> tax = new AtomicReference<>(5.05);
        if(((isNormalHouse==1)&&(isOver2years==1))||(isFirstSell == 1)) {
            tax.set(0.0);
        }else{
            tax.set(5.05);
        }
        return tax.get();
    }
    public Double calValueAddedTax(){
        AtomicReference<Double> tax = new AtomicReference<>(0.0);
        if(isOver2years==2){
            tax.set(calTaxPrice()*valueAddedTaxRate/100);
        }else{
            tax.set((calTaxPrice()-buyingPrice)*valueAddedTaxRate/100);
        }
        Log.i("mainActivity tax.get() ",tax.get().toString());
            return tax.get();
    }
    public Float calIncomeTaxRate(){
        AtomicReference<Float> tax = new AtomicReference<>(Float.valueOf(2));
        if((isOver5years==1)&&(isOnlyHouse==1)) {
            tax.set(Float.valueOf(0));
        }else if((((isNormalHouse==1)||(isFirstSell==1))&&(isOver5years==2))||
                (((isNormalHouse==1)||(isFirstSell==1))&&(isOnlyHouse==2))) {
            tax.set(Float.valueOf(1));
        }else{
            tax.set(Float.valueOf(2));
        }
        return tax.get();
    }
    public Double calIncomeTax(){
        AtomicReference<Double> tax = new AtomicReference<>(Double.valueOf(0));
        tax.set(Double.valueOf((calTaxPrice()-calValueAddedTax())*incomeTaxRate/100));
        return tax.get();
    }

    public Float calTaxPrice(){
        AtomicReference<Float> taxPrice = new AtomicReference<>(Float.valueOf(0));
        if(locationType==1){
            if ((realPrice>450)&&(evadeTax==true)){
                taxPrice.set(Float.valueOf(449));
            }
            else{
                taxPrice.set(realPrice);
            }
        }else if(locationType==2){
            if ((realPrice>310)&&(evadeTax==true)){
                taxPrice.set(Float.valueOf(309));
            }
            else{
                taxPrice.set(realPrice);
            }
        }else if(locationType==3){
            if ((realPrice>230)&&(evadeTax==true)){
                taxPrice.set(Float.valueOf(229));
            }
            else{
                taxPrice.set(realPrice);
            }
        }
        return taxPrice.get();
    }

    public Double calAgencyFee(){
        AtomicReference<Double> tax = new AtomicReference<>(Double.valueOf(0));
        tax.set(Double.valueOf(realPrice*agencyFeeRate/100));
        return tax.get();
    }

    public Double calDownPayment()
    {
        AtomicReference<Double> tax = new AtomicReference<>(Double.valueOf(0));
        tax.set(Double.valueOf(calTaxPrice()*0.35));
        return tax.get();
    }
    public Double calFirstPay()
    {
        AtomicReference<Double> tax = new AtomicReference<>(Double.valueOf(0));
        tax.set(Double.valueOf(calDownPayment()+(realPrice-calTaxPrice())+
                calDeedTax()+calValueAddedTax()+calIncomeTax()+calAgencyFee()+getOtherHandlingFee()/10000));
        return tax.get();
    }
}
