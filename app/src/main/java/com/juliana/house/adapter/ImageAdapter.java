package com.juliana.house.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.juliana.house.R;
import com.juliana.house.util.TakePhotoUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public List<Bitmap> imgList = new ArrayList<Bitmap>();

    //  图片数组源
    public Integer[] imgs = { R.drawable.house, R.drawable.house,
            R.drawable.house};

    public ImageAdapter(Context c) {
        mContext = c;
        loadImage();
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    // 获取图片位置
    @Override
    public Object getItem(int position) {
        return imgList.toArray()[position];
    }

    // 获取图片ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview = new ImageView(mContext);
        //imageview.setImageResource(imgList.toArray()[position % imgList.size()]);
        Log.i("mainActivity","position "+position);
        imageview.setImageBitmap(imgList.get(position));
        imageview.setLayoutParams(new Gallery.LayoutParams(240, 180));		// 设置布局 图片120×120显示
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);				// 设置显示比例类型
        imageview.setBackgroundColor(Color.alpha(1));
        return imageview;
    }

    private void loadImage(){
        Log.i("mainActivity","loadImage ");
        if (imgList.size()>0){
            imgList.clear();
        }
        Bitmap bp;
        for (int i = 0;i<imgs.length;i++) {
            bp = BitmapFactory.decodeResource(mContext.getResources(), imgs[i], null);
            imgList.add(bp);
        }
    }
}
