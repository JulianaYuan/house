package com.juliana.house.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.juliana.house.R;
import com.juliana.house.db.HousesDB;
import com.juliana.house.model.House;

import java.util.ArrayList;
import java.util.List;

public class HouseListActivity extends BaseActivity implements View.OnClickListener{

    private Button createHouseBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private HousesDB m_housesDB;
    private List<String> dataList = new ArrayList<String>();

    /**
     *house list
     */
    private List<House> houseList;
    private static final int DELETE_ID = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.house_list);
        createHouseBtn = (Button) findViewById(R.id.create_house);
        listView = (ListView)findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        m_housesDB = HousesDB.getInstance(this);
        queryHouses();
        createHouseBtn.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int houseId = houseList.get(i).getId();
                Intent intent = new Intent(HouseListActivity.this,HouseDetailActivity.class);
                Log.i("mainActivity","HouseListActivity onCreate");
                intent.putExtra("house_id",houseId);
                startActivity(intent);
                //finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.showContextMenu(view.getX(),view.getY());
                return true;
            }
        });
        //listView.setOnCreateContextMenuListener(this);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "删除");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case DELETE_ID:
                Log.i("mainActivity","onContextItemSelected "+info.id);
                Log.i("mainActivity","delete Id: "+houseList.get((int) info.id).getId());
                showDialog(info.id);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.create_house:
                Intent intent = new Intent(HouseListActivity.this,HouseDetailActivity.class);
                Log.i("mainActivity","HouseListActivity onCreate");
                startActivity(intent);
                //finish();
                break;
        }
    }
    private void showDialog(final long par){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(HouseListActivity.this);
        //    设置Title的图标
        builder.setIcon(R.mipmap.ic_launcher);
        //    设置Title的内容
        builder.setTitle("删除"+houseList.get((int) par).getDistrictName());
        //    设置Content来显示一个信息
        builder.setMessage("确定删除吗？");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            //long id = par;
            @Override
            public void onClick(DialogInterface dialog, int which){
                Log.i("mainActivity","delete Id: "+houseList.get((int) par).getId());
                deleteHouseFromDB(houseList.get((int) par).getId());
                queryHouses();
                //Toast.makeText(HouseListActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(HouseListActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(HouseListActivity.this, "neutral: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    显示出该对话框
        builder.show();
    }
    private void queryHouses(){
        houseList = m_housesDB.loadHouse();
        Log.i("mainActivity","queryHouses ");
        if (houseList.size()>0){
            dataList.clear();
            for (House house:houseList){
                dataList.add(house.getTimeStamp()+":"+house.getDistrictName()+ " " + house.getDescribe());
                Log.i("mainActivity","queryHouses getId: "+house.getId());
                Log.i("mainActivity","queryHouses getTimeStamp: "+house.getTimeStamp());
                Log.i("mainActivity","queryHouses getDistrictName: "+house.getDistrictName());
                Log.i("mainActivity","queryHouses getArea: "+house.getArea());
                Log.i("mainActivity","queryHouses getQuotedPrice: "+house.getQuotedPrice());
                Log.i("mainActivity","queryHouses getRealPrice: "+house.getRealPrice());
                Log.i("mainActivity","queryHouses getBuyingPrice: "+house.getBuyingPrice());
                Log.i("mainActivity","queryHouses getTotalFloors: "+house.getTotalFloors());
                Log.i("mainActivity","queryHouses getRealFloor: "+house.getRealFloor());
                Log.i("mainActivity","queryHouses getLocationType: "+house.getLocationType());
                Log.i("mainActivity","queryHouses getIsFirstSell: "+house.getIsFirstSell());
                Log.i("mainActivity","queryHouses getIsNormalHouse: "+house.getIsNormalHouse());
                Log.i("mainActivity","queryHouses getIsOver5years: "+house.getIsOver5years());
                Log.i("mainActivity","queryHouses getIsOnlyHouse: "+house.getIsOnlyHouse());
                Log.i("mainActivity","queryHouses getIsOver2years: "+house.getIsOver2years());
                Log.i("mainActivity","queryHouses getDeedTaxRate: "+house.getDeedTaxRate());
                Log.i("mainActivity","queryHouses getValueAddedTaxRate: "+house.getValueAddedTaxRate());
                Log.i("mainActivity","queryHouses getIncomeTaxRate: "+house.getIncomeTaxRate());
                Log.i("mainActivity","queryHouses getAgencyFeeRate: "+house.getAgencyFeeRate());
                Log.i("mainActivity","queryHouses getOtherHandlingFee: "+house.getOtherHandlingFee());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }else{
            Intent intent = new Intent(HouseListActivity.this,HouseDetailActivity.class);
            Log.i("mainActivity","HouseListActivity onCreate");
            startActivity(intent);
            finish();
        }
    }
    private void deleteHouseFromDB(long id){
        m_housesDB.deleteHouse(id);
    }
}
