package com.juliana.house.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Spinner;
import android.widget.Toast;

import com.juliana.house.R;
import com.juliana.house.adapter.ImageAdapter;
import com.juliana.house.db.HousesDB;
import com.juliana.house.model.House;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HouseDetailActivity extends BaseActivity implements View.OnClickListener{
    private Button switchHouseBtn;
    private Button saveEditBtn;
    private Button calTaxBtn;

    private Gallery houseGallery;

    private EditText districtEdit;
    private EditText describeEdit;
    private EditText areaEdit;
    private EditText floorEdit;

    private Spinner locationSpin;

    private EditText quotedEdit;
    private EditText realPriceEdit;
    private EditText buyingPriceEdit;
    private EditText taxPriceEdit;

    private Spinner over5yearSpin;
    private Spinner onlyHouseSpin;
    private Spinner over2yearSpin;
    private Spinner firstSellSpin;

    private Spinner normalHouseSpin;

    private CheckBox evadeTaxCheck;
    private CheckBox firstBuyCheck;

    private EditText deedTaxRateEdit;
    private EditText valueAddedTaxRateEdit;
    private EditText incomeTaxRateEdit;
    private EditText agencyFeeRateEdit;
    private EditText otherFeeEdit;
    private EditText deedTaxEdit;
    private EditText valueAddedTaxEdit;
    private EditText incomeTaxEdit;
    private EditText agencyFeeEdit;
    private EditText downPaymentEdit;
    private EditText firstPayEdit;

    private boolean m_isEditing = true;
    private boolean m_isNewHouse = true;
    private House m_house = null;
    private HousesDB m_housesDB;
    private ImageAdapter m_ImgAdapter = null;			// 声明图片资源对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.house_detail);

        switchHouseBtn = (Button)findViewById(R.id.switch_house);
        saveEditBtn = (Button)findViewById(R.id.save_edit);
        calTaxBtn = (Button)findViewById(R.id.cal_tax);
        houseGallery = (Gallery)findViewById(R.id.gallery);
        districtEdit = (EditText)findViewById(R.id.district_edit);
        describeEdit = (EditText)findViewById(R.id.describe_edit);
        areaEdit = (EditText)findViewById(R.id.area_edit);
        floorEdit = (EditText)findViewById(R.id.floor_edit);
        locationSpin = (Spinner)findViewById(R.id.location_spin);
        quotedEdit = (EditText)findViewById(R.id.quoted_edit);
        realPriceEdit = (EditText)findViewById(R.id.real_price_edit);
        buyingPriceEdit = (EditText)findViewById(R.id.buying_price_edit);
        taxPriceEdit = (EditText)findViewById(R.id.tax_price_edit);
        taxPriceEdit.setEnabled(false);
        over5yearSpin = (Spinner)findViewById(R.id.over5year_spin);
        onlyHouseSpin = (Spinner)findViewById(R.id.only_house_spin);
        over2yearSpin = (Spinner)findViewById(R.id.over2year_spin);
        firstSellSpin = (Spinner)findViewById(R.id.first_sell_spin);
        normalHouseSpin = (Spinner) findViewById(R.id.normal_house_spin);
        normalHouseSpin.setEnabled(false);
        evadeTaxCheck = (CheckBox) findViewById(R.id.evade_tax_check);
        evadeTaxCheck.setChecked(false);
        firstBuyCheck = (CheckBox) findViewById(R.id.first_buy_check);
        firstBuyCheck.setChecked(true);

        deedTaxRateEdit = (EditText)findViewById(R.id.deed_tax_rate_edit);
        deedTaxRateEdit.setEnabled(false);
        valueAddedTaxRateEdit = (EditText)findViewById(R.id.value_added_tax_rate_edit);
        valueAddedTaxRateEdit.setEnabled(false);
        incomeTaxRateEdit = (EditText)findViewById(R.id.income_tax_rate_edit);
        incomeTaxRateEdit.setEnabled(false);
        agencyFeeRateEdit = (EditText)findViewById(R.id.agency_fee_rate_edit);
        otherFeeEdit = (EditText)findViewById(R.id.other_fee_edit);
        deedTaxEdit = (EditText)findViewById(R.id.deed_tax_edit);
        deedTaxEdit.setEnabled(false);
        valueAddedTaxEdit = (EditText)findViewById(R.id.value_added_tax_edit);
        valueAddedTaxEdit.setEnabled(false);
        incomeTaxEdit = (EditText)findViewById(R.id.income_tax_edit);
        incomeTaxEdit.setEnabled(false);
        agencyFeeEdit = (EditText)findViewById(R.id.agency_fee_edit);
        agencyFeeEdit.setEnabled(false);
        downPaymentEdit = (EditText)findViewById(R.id.down_payment_edit);
        downPaymentEdit.setEnabled(false);
        firstPayEdit = (EditText)findViewById(R.id.first_pay_edit);
        firstPayEdit.setEnabled(false);

        m_housesDB = HousesDB.getInstance(this);
        switchHouseBtn.setOnClickListener(this);
        saveEditBtn.setOnClickListener(this);
        calTaxBtn.setOnClickListener(this);
        m_ImgAdapter = new ImageAdapter(this);
        houseGallery.setAdapter(m_ImgAdapter);
        houseGallery.setGravity(Gravity.CENTER_HORIZONTAL);		// 设置水平居中显示
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) houseGallery.getLayoutParams();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        layoutParams.setMargins(-width*2/3, 0, 0, 0);
        //houseGallery.setSelection(m_ImgAdapter.imgs.length * 100);		// 设置起始图片显示位置（可以用来制作gallery循环显示效果）

        houseGallery.setOnItemClickListener(clickListener); 			// 设置点击图片的监听事件（需要用手点击才触发，滑动时不触发）
        houseGallery.setOnItemSelectedListener(selectedListener);		// 设置选中图片的监听事件（当图片滑到屏幕正中，则视为自动选中）
        houseGallery.setUnselectedAlpha(0.3f);					// 设置未选中图片的透明度
        houseGallery.setSpacing(5);							// 设置图片之间的间距
        //houseGallery.setOnClickListener(this);
        over5yearSpin.setOnItemSelectedListener(selectedListener);
        firstSellSpin.setOnItemSelectedListener(selectedListener);
        evadeTaxCheck.setOnClickListener(this);
        firstBuyCheck.setOnClickListener(this);
        int houseId = getIntent().getIntExtra("house_id",0);
        if (houseId!=0)
        {
            Log.i("mainActivity","HouseDetailActivity onCreate houseCode is not empty ");
            m_house = m_housesDB.readHouse(houseId);
            m_isNewHouse = false;
            m_isEditing = false;
            m_house.calcHouse();
            loadHouseState(m_house);
        }
        else{
            Log.i("mainActivity","HouseDetailActivity onCreate houseCode is empty ");
            m_house = new House();
            m_isNewHouse = true;
            m_isEditing = true;
        }
        updateWidgetState(m_isEditing);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()) {
            case R.id.switch_house:
                Log.i("mainActivity","m_housesDB.getDBTableHouseCount() "+m_housesDB.getDBTableHouseCount());
                Log.i("mainActivity","ActivityCollector.isActivityExist(HouseListActivity.class) "+ActivityCollector.isActivityExist(HouseListActivity.class));
                if((m_housesDB.getDBTableHouseCount()!=0)&&(!ActivityCollector.isActivityExist(HouseListActivity.class)))  {
                    intent = new Intent(HouseDetailActivity.this,HouseListActivity.class);
                    Log.i("mainActivity","HouseDetailActivity switch_house");
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.save_edit:
                if(m_isEditing) {
                    m_isEditing = false;
                    saveHouseState();
                    m_house.calcHouse();
                    updateCalWidgetState();
                    saveHouseToDB();
                }else{
                    m_isEditing = true;
                }
                updateWidgetState(m_isEditing);
                break;
            case R.id.cal_tax:
                int houseId = m_house.getId();
                intent = new Intent(HouseDetailActivity.this, TaxLoanActivity.class);
                Log.i("mainActivity", "HouseDetailActivity cal_tax");
                intent.putExtra("house_id",houseId);
                startActivity(intent);
                break;
            case R.id.evade_tax_check:
                Log.i("mainActivity","HouseDetailActivity evade_tax_check");
                saveHouseState();
                m_house.calcHouse();
                updateCalWidgetState();
                saveHouseToDB();
                break;
            case R.id.first_buy_check:
                Log.i("mainActivity","HouseDetailActivity first_buy_check");
                saveHouseState();
                m_house.calcHouse();
                updateCalWidgetState();
                saveHouseToDB();
                break;
        }
    }
    // 点击图片的监听事件
    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(HouseDetailActivity.this, "点击图片 " + (position + 1), 100).show();
        }
    };

    // 选中监听事件
    AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (view.getId()){
                case R.id.gallery:
                    Toast.makeText(HouseDetailActivity.this, "选中图片 " + (position + 1), 20).show();
                    break;
                case R.id.over5year_spin:
                    if (position == 1)
                    {
                        over2yearSpin.setSelection(1);
                        over2yearSpin.setEnabled(false);
                    }else{
                        over2yearSpin.setEnabled(true);
                    }
                    break;
                case R.id.first_sell_spin:
                    if (position == 1)
                    {
                        buyingPriceEdit.setEnabled(false);
                    }else{
                        if (m_isEditing) {
                            Log.i("mainActivity","HouseDetailActivity m_isEditing ");
                            buyingPriceEdit.setEnabled(true);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBackPressed(){
        Intent intent;
        if((m_housesDB.getDBTableHouseCount()!=0)&&(!ActivityCollector.isActivityExist(HouseListActivity.class))) {
            intent = new Intent(HouseDetailActivity.this,HouseListActivity.class);
            Log.i("mainActivity","HouseDetailActivity onBackPressed");
            startActivity(intent);
        }
        super.onBackPressed();
    }
    private void updateWidgetState(boolean isEditing)
    {
        districtEdit.setEnabled(isEditing);
        describeEdit.setEnabled(isEditing);
        areaEdit.setEnabled(isEditing);
        floorEdit.setEnabled(isEditing);
        locationSpin.setEnabled(isEditing);

        quotedEdit.setEnabled(isEditing);
        realPriceEdit.setEnabled(isEditing);
        buyingPriceEdit.setEnabled(isEditing);
        if (m_house.getIsFirstSell()==1)buyingPriceEdit.setEnabled(false);

        over5yearSpin.setEnabled(isEditing);
        onlyHouseSpin.setEnabled(isEditing);
        if (m_house.getIsOver5years()==1){
            over2yearSpin.setEnabled(false);
        }else {
            over2yearSpin.setEnabled(isEditing);
        }
        firstSellSpin.setEnabled(isEditing);
        if(isEditing) {
            saveEditBtn.setText("保存");
            calTaxBtn.setEnabled(false);
        }else{
            saveEditBtn.setText("编辑");
            if(locationSpin.getSelectedItemPosition()==0
            ||over5yearSpin.getSelectedItemPosition()==0
            ||onlyHouseSpin.getSelectedItemPosition()==0
            ||over2yearSpin.getSelectedItemPosition()==0
            ||firstSellSpin.getSelectedItemPosition()==0
            ||normalHouseSpin.getSelectedItemPosition()==0){
                calTaxBtn.setEnabled(false);
            }else {
                calTaxBtn.setEnabled(true);
            }
        }
        agencyFeeRateEdit.setEnabled(isEditing);
        otherFeeEdit.setEnabled(isEditing);
    }

    private void loadHouseState(House house)
    {
        districtEdit.setText(house.getDistrictName());
        describeEdit.setText(house.getDescribe());
        areaEdit.setText(house.getArea().toString());
        floorEdit.setText(house.getRealFloor()+"/"+house.getTotalFloors());
        locationSpin.setSelection(house.getLocationType());
        evadeTaxCheck.setChecked(house.getEvadeTax());
        firstBuyCheck.setChecked(house.getIsFirstBuy());

        quotedEdit.setText(house.getQuotedPrice().toString());
        realPriceEdit.setText(house.getRealPrice().toString());
        buyingPriceEdit.setText(house.getBuyingPrice().toString());
        if (house.getIsFirstSell()==1)buyingPriceEdit.setEnabled(false);
        taxPriceEdit.setText(house.calTaxPrice().toString());

        over5yearSpin.setSelection(house.getIsOver5years());
        onlyHouseSpin.setSelection(house.getIsOnlyHouse());
        if (m_house.getIsOver5years()==1){
            over2yearSpin.setSelection(1);
        }else{
            over2yearSpin.setSelection(house.getIsOver2years());
        }
        firstSellSpin.setSelection(house.getIsFirstSell());
        normalHouseSpin.setSelection(house.getIsNormalHouse());
        deedTaxRateEdit.setText(house.calDeedTaxRate().toString()+"%");
        valueAddedTaxRateEdit.setText(house.calValueAddedRate().toString()+"%");
        incomeTaxRateEdit.setText(house.calIncomeTaxRate().toString()+"%");
        agencyFeeRateEdit.setText(house.getAgencyFeeRate().toString()+"%");
        otherFeeEdit.setText(house.getOtherHandlingFee().toString());
        deedTaxEdit.setText(house.calDeedTax().toString());
        valueAddedTaxEdit.setText(house.calValueAddedTax().toString());
        incomeTaxEdit.setText(house.calIncomeTax().toString());
        agencyFeeEdit.setText(house.calAgencyFee().toString());
        downPaymentEdit.setText(house.calDownPayment().toString());
        firstPayEdit.setText(house.calFirstPay().toString());
    }
    private void saveHouseState()
    {
        if (!TextUtils.isEmpty(districtEdit.getText().toString()))
            m_house.setDistrictName(districtEdit.getText().toString());
        if (!TextUtils.isEmpty(describeEdit.getText().toString()))
            m_house.setDescribe(describeEdit.getText().toString());
        if (!TextUtils.isEmpty(areaEdit.getText().toString()))
            m_house.setArea(Float.valueOf(areaEdit.getText().toString()));
        if (!TextUtils.isEmpty(floorEdit.getText().toString())) {
            String floor = floorEdit.getText().toString();
            String floors[] = floor.split("/");
            Log.i("mainActivity", "saveHouseState " + floors[0] + "  " + floors[1]);
            m_house.setRealFloor(Integer.parseInt(floors[0]));
            m_house.setTotalFloors(Integer.parseInt(floors[1]));
        }
        m_house.setLocationType(locationSpin.getSelectedItemPosition());
        m_house.setEvadeTax(evadeTaxCheck.isChecked());
        m_house.setIsFirstBuy(firstBuyCheck.isChecked());

        if (!TextUtils.isEmpty(quotedEdit.getText().toString()))
            m_house.setQuotedPrice(Float.valueOf(quotedEdit.getText().toString()));
        if (!TextUtils.isEmpty(realPriceEdit.getText().toString()))
            m_house.setRealPrice(Float.valueOf(realPriceEdit.getText().toString()));
        if (!TextUtils.isEmpty(buyingPriceEdit.getText().toString()))
            m_house.setBuyingPrice(Float.valueOf(buyingPriceEdit.getText().toString()));

        m_house.setIsOver5years(over5yearSpin.getSelectedItemPosition());
        m_house.setIsOnlyHouse(onlyHouseSpin.getSelectedItemPosition());
        if (m_house.getIsOver5years()==1)m_house.setIsOver2years(1);
        else m_house.setIsOver2years(over2yearSpin.getSelectedItemPosition());
        m_house.setIsFirstSell(firstSellSpin.getSelectedItemPosition());
        m_house.setIsNormalHouse(normalHouseSpin.getSelectedItemPosition());
        if (!TextUtils.isEmpty(deedTaxRateEdit.getText().toString())) {
            Log.i("mainActivity", "saveHouseState " + deedTaxRateEdit.getText().toString().split("%")[0]);
            m_house.setDeedTaxRate(Double.valueOf(deedTaxRateEdit.getText().toString().split("%")[0]));
        }
        if (!TextUtils.isEmpty(valueAddedTaxRateEdit.getText().toString())) {
            Log.i("mainActivity", "saveHouseState " + valueAddedTaxRateEdit.getText().toString().split("%")[0]);
            m_house.setValueAddedTaxRate(Double.valueOf(valueAddedTaxRateEdit.getText().toString().split("%")[0]));
        }
        if (!TextUtils.isEmpty(incomeTaxRateEdit.getText().toString())) {
            Log.i("mainActivity", "saveHouseState " + incomeTaxRateEdit.getText().toString().split("%")[0]);
            m_house.setIncomeTaxRate(Float.valueOf(incomeTaxRateEdit.getText().toString().split("%")[0]));
        }
        if (!TextUtils.isEmpty(agencyFeeRateEdit.getText().toString())) {
            Log.i("mainActivity", "saveHouseState " + agencyFeeRateEdit.getText().toString().split("%")[0]);
            m_house.setAgencyFeeRate(Float.valueOf(agencyFeeRateEdit.getText().toString().split("%")[0]));
        }
        if (!TextUtils.isEmpty(otherFeeEdit.getText().toString()))
            m_house.setOtherHandlingFee(Float.valueOf(otherFeeEdit.getText().toString()));
    }
    private void saveHouseToDB(){
        if(m_isNewHouse)
        {
            m_isNewHouse = false;
            m_housesDB.addHouse(m_house);
        }
        else
        {
           m_housesDB.saveHouse(m_house);
        }
    }
    private void updateCalWidgetState(){
        buyingPriceEdit.setText(m_house.getBuyingPrice().toString());
        taxPriceEdit.setText(m_house.calTaxPrice().toString());
        over5yearSpin.setSelection(m_house.getIsOver5years());
        normalHouseSpin.setSelection(m_house.getIsNormalHouse());
        deedTaxRateEdit.setText(m_house.calDeedTaxRate().toString()+"%");
        valueAddedTaxRateEdit.setText(m_house.calValueAddedRate().toString()+"%");
        incomeTaxRateEdit.setText(m_house.calIncomeTaxRate().toString()+"%");
        deedTaxEdit.setText(m_house.calDeedTax().toString());
        valueAddedTaxEdit.setText(m_house.calValueAddedTax().toString());
        incomeTaxEdit.setText(m_house.calIncomeTax().toString());
        agencyFeeEdit.setText(m_house.calAgencyFee().toString());
        downPaymentEdit.setText(m_house.calDownPayment().toString());
        firstPayEdit.setText(m_house.calFirstPay().toString());
    }
}
