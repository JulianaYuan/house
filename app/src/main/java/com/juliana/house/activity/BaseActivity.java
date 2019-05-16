package com.juliana.house.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.juliana.house.db.HousesDB;

/**
 * Created by JYUAN7 on 12/11/2018.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Log.d("BaseActivity","add Activity "+getClass().getSimpleName());
        ActivityCollector.addActivity(this,getClass());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("mainActivity","remove Activity "+getClass().getSimpleName());
        ActivityCollector.removeActivity(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onStop(){
        Log.i("mainActivity",this.toString()+" onStop ");
        super.onStop();
    }
}
