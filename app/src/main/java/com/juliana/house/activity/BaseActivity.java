package com.juliana.house.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
        Log.d("BaseActivity","remove Activity "+getClass().getSimpleName());
        ActivityCollector.removeActivity(this);
    }
}
