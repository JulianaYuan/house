package com.juliana.house.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.juliana.house.db.DBBackupTask;
import com.juliana.house.db.HousesDB;
import com.juliana.house.util.PermissionsUtils;

import java.io.File;

public class HouseBaseActivity extends BaseActivity{
    private static HousesDB m_housesDB = null;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        if(m_housesDB == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!PermissionsUtils.startRequestPermission((Activity) this, PermissionsUtils.WRITE_EXTERNAL_STORAGE_PERMISSIONS, 1)) {
                    Log.d("backup", "permission deny");
                    //return;
                }
            }
            File dbFile = this.getDatabasePath("/data/data/com.juliana.house/databases/"+HousesDB.DB_NAME);
            File backupFile = new File(Environment.getExternalStorageDirectory(),
                    "com.juliana.houseDBBackup"+HousesDB.DB_NAME);
            if ((!dbFile.exists())&&(backupFile.exists())){
                dataRecover();
            }
            m_housesDB = HousesDB.getInstance(this);
        }
    }

    protected static HousesDB getHousesDB()
    {
        return m_housesDB;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onStop(){
        Log.i("mainActivity",this.toString()+" onStop ");
        super.onStop();
        if (0 == ActivityCollector.getActivitiesCount())
        {
            m_housesDB.closeHouseDB();
            Log.i("mainActivity"," APP exit!!! ");
            dataBackup();
        }
    }
    // 数据恢复
    private void dataRecover() {
        // TODO Auto-generated method stub
        new DBBackupTask(this).execute("restroeDatabase");
    }

    // 数据备份
    private void dataBackup() {
        // TODO Auto-generated method stub
        Log.i("mainActivity","dataBackup "+Environment.getExternalStorageDirectory().getAbsolutePath());
        new DBBackupTask(this).execute("backupDatabase");
    }
}
