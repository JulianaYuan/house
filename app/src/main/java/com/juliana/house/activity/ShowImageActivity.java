package com.juliana.house.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.juliana.house.R;
import com.juliana.house.adapter.MyPagerAdapter;
import com.juliana.house.model.EventMessage;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ShowImageActivity extends HouseBaseActivity {
    private ViewPager viewPager;  //声明viewpage
    private List<View> listViews = null;  //用于获取图片资源
    private int index = 0;   //获取当前点击的图片位置
    private MyPagerAdapter adapter;   //ViewPager的适配器
    private ArrayList<Bitmap> bmp = null;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
        setContentView(R.layout.show_image_layout);    //绑定布局
        inigView();
        initData();

    }

    private void inigView() {
        viewPager = (ViewPager) findViewById(R.id.show_view_pager);  //绑定viewpager的id
    }

    private void initData() {
        listViews = new ArrayList<View>();   //初始化list
        position = getIntent().getIntExtra("id",0);
    }

    private void inint() {

        if (bmp != null && bmp.size() > 0){
            for (int i = 0; i < bmp.size(); i++) {  //for循环将试图添加到list中
                View view = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
                //                ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
                //                iv.setImageBitmap(bmp.get(i));   //设置当前点击的图片

                SubsamplingScaleImageView iv = (SubsamplingScaleImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
                iv.setImage(ImageSource.bitmap(bmp.get(i)));
                listViews.add(view);
                /**
                 * 图片的长按监听
                 * */
                iv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //弹出提示，提示内容为当前的图片位置
                        Toast.makeText(ShowImageActivity.this, "这是第" + (index + 1) + "图片", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
            adapter = new MyPagerAdapter(listViews);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    index = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            }); //设置viewpager的改变监听
            //截取intent获取传递的值
            viewPager.setCurrentItem(position);    //viewpager显示指定的位置
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(EventMessage em) {
        bmp = (ArrayList<Bitmap>)em.obj;
        int byteCount = bmp.get(0).getByteCount();
        inint();   //初始化
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
