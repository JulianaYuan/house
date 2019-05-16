package com.juliana.house.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.juliana.house.R;
import com.juliana.house.adapter.ImageAdapter;
import com.juliana.house.db.HousesDB;
import com.juliana.house.model.EventMessage;
import com.juliana.house.model.House;
import com.juliana.house.util.BitmapUtils;
import com.juliana.house.util.ImageFactory;
import com.juliana.house.util.TakePhotoUtils;
import com.juliana.house.util.WindowManagerUtils;
import com.nanchen.compresshelper.CompressHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HouseDetailActivity extends HouseBaseActivity implements View.OnClickListener{
    private Button switchHouseBtn;
    private Button saveEditBtn;
    private Button calTaxBtn;

    //private Gallery houseGallery;

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
    private GridView photoGrid;

    private boolean m_isEditing = true;
    private boolean m_isNewHouse = true;
    private House m_house = null;
    private HousesDB m_housesDB;
    private ImageAdapter m_ImgAdapter = null;			// 声明图片资源对象

    private TakePhotoUtils m_takePhotoUtil = null;
    private List<Bitmap> imgList = new ArrayList<Bitmap>();
    private List<Bitmap> origalImgList = new ArrayList<Bitmap>();
    private Bitmap zoomImageBitmap;
    private Uri origalUri;
    private File file;
    private File newFile;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.house_detail);

        switchHouseBtn = (Button)findViewById(R.id.switch_house);
        saveEditBtn = (Button)findViewById(R.id.save_edit);
        calTaxBtn = (Button)findViewById(R.id.cal_tax);
        //houseGallery = (Gallery)findViewById(R.id.gallery);
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
        photoGrid = (GridView)findViewById(R.id.gv_photo);


        m_housesDB = getHousesDB();
        switchHouseBtn.setOnClickListener(this);
        saveEditBtn.setOnClickListener(this);
        calTaxBtn.setOnClickListener(this);
        m_ImgAdapter = new ImageAdapter(this,imgList);

        m_ImgAdapter.setOnDelItemPhotoClickListener(delItemPhotoClickListener);
        m_ImgAdapter.setOnPhotoClickListener(photoClickListener);

        photoGrid.setAdapter(m_ImgAdapter);
        int screenWidth = WindowManagerUtils.getScreenWidth(this);
        width = screenWidth/3;
        photoGrid.setOnItemClickListener(clickListener);
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

        m_takePhotoUtil = TakePhotoUtils.getInstance();
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
            if (position == imgList.size()){
                TakePhotoUtils.getInstance().pickPhoto(parent.getContext());
            }else{
                //进入图片预览页面
                Log.i("mainActivity","HouseDetailActivity onItemClick 进入图片预览页面");
                EventBus.getDefault().postSticky(new EventMessage(1,origalImgList));
                Intent intent = new Intent(parent.getContext(), ShowImageActivity.class);
                intent.putExtra("id", position);   //将当前点击的位置传递过去
                parent.getContext().startActivity(intent);     //启动Activity
            }
            Log.i("mainActivity","HouseDetailActivity onItemClick");
            Toast.makeText(HouseDetailActivity.this, "点击图片 " + (position + 1), Toast.LENGTH_LONG).show();
        }
    };

    ImageAdapter.OnDelItemPhotoClickListener delItemPhotoClickListener =
            new ImageAdapter.OnDelItemPhotoClickListener() {
                @Override
                public void onDelItemPhotoClick(int position) {
                    Log.i("mainActivity","HouseDetailActivity onDelItemPhotoClick "+position);
                    if (imgList != null && imgList.size() > 0){
                        imgList.remove(position);
                        m_ImgAdapter.notifyDataSetChanged();
                    }

                    if (origalImgList != null && origalImgList.size() > 0){
                        origalImgList.remove(position);
                    }

                }
            };
    ImageAdapter.OnPhotoClickListener photoClickListener =
            new ImageAdapter.OnPhotoClickListener() {
                @Override
                public void onPhotoClick(int position) {
                    Log.i("mainActivity","HouseDetailActivity onPhotoClick "+position);
                    if (position == imgList.size()){
                        TakePhotoUtils.getInstance().pickPhoto(photoGrid.getContext());
                    }else{
                        //进入图片预览页面
                        Log.i("mainActivity","HouseDetailActivity onItemClick 进入图片预览页面");
                        EventBus.getDefault().postSticky(new EventMessage(1,origalImgList));
                        Intent intent = new Intent(photoGrid.getContext(), ShowImageActivity.class);
                        intent.putExtra("id", position);   //将当前点击的位置传递过去
                        photoGrid.getContext().startActivity(intent);     //启动Activity
                    }
                }
            };

    @Override
    public void onActivityResult(int pRequestCode, int pResultCode, Intent pData){
        Uri uri;
        if (pResultCode == Activity.RESULT_OK) {
            switch (pRequestCode) {
                // 从相册取
                case TakePhotoUtils.CHOOSE_PICTURE:
                    origalUri = pData.getData();
                    file = BitmapUtils.getFileFromMediaUri(getApplicationContext(), origalUri);
                    newFile = CompressHelper.getDefault(getApplicationContext()).compressToFile(file);
                    Bitmap photoBmp = null;
                    try {
                        //                        photoBmp = BitmapUtils.getBitmapFormUri(UserFeedbackActivity.this, Uri.fromFile(file));
                        photoBmp = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int degree = BitmapUtils.getBitmapDegree(newFile.getAbsolutePath());

                    /**
                     * 把图片旋转为正的方向
                     */
                    Bitmap newbitmap = BitmapUtils.rotateBitmapByDegree(photoBmp, degree);



                    origalImgList.add(newbitmap);

                    zoomImage(newbitmap,width,width);
                    //                    TakePhotoUtils.getInstance().cropImageUri(context, origalUri, width, width, TakePhotoUtils.CROP_BIG_PICTURE);
                    break;

                case TakePhotoUtils.CROP_BIG_PICTURE:
                    // 剪大图用uri
                    if (TakePhotoUtils.getInstance().mImageFile != null) {
                        Bitmap bitmap = TakePhotoUtils.getInstance().decodeUriAsBitmap(getApplicationContext(), Uri.fromFile
                                (TakePhotoUtils.getInstance().mImageFile));

                        Bitmap image = ImageFactory.ratio(bitmap, width, width);
                        if (image != null) {
                            // spath:生成图片取个名字和路径包含类型
                            String fileName = "image" + System.currentTimeMillis()
                                    + ".png";
                            String outPath = getApplicationContext().getFilesDir().getAbsolutePath() + fileName;
                            try {
                                ImageFactory.compressAndGenImage(image, outPath, 140);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File file = new File(outPath);
                            imgList.add(image);

                            m_ImgAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(pRequestCode, pResultCode, pData);
    }
    /**
     * 缩放图片
     * @param bitmap
     * @param width
     * @param height
     */
    private void zoomImage(Bitmap bitmap,int width,int height){
        zoomImageBitmap = BitmapUtils.zoomBitmap(bitmap, width, height);
        imgList.add(zoomImageBitmap);
        m_ImgAdapter.notifyDataSetChanged();
    }
    // 选中监听事件
    AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()){
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
