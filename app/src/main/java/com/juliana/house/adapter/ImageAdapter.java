package com.juliana.house.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

import com.juliana.house.R;
import com.juliana.house.util.TakePhotoUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;//视图容器
    public List<Bitmap> imgList = new ArrayList<Bitmap>();

    //  图片数组源
    public Integer[] imgs = { R.drawable.house};

    public ImageAdapter(Context c,List<Bitmap> bmp) {
        inflater = LayoutInflater.from(c);
        mContext = c;
        imgList = bmp;
        //loadImage();
    }

    @Override
    public int getCount() {
        return (imgList.size()+1);
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
        final int coord = position;
        ImageAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new ImageAdapter.ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.iv_item);
            holder.bt_del = (Button) convertView.findViewById(R.id.iiv_del);
            convertView.setTag(holder);
        } else {
            holder = (ImageAdapter.ViewHolder) convertView.getTag();
        }

        if (position == imgList.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.icon_addpic_upload));
            holder.bt_del.setVisibility(View.GONE);
            if (position == Integer.valueOf(R.string.maxImageNum))
            {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(imgList.get(position));
            holder.bt_del.setVisibility(View.VISIBLE);
            holder.bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDelItemPhotoClickListener != null){
                        mOnDelItemPhotoClickListener.onDelItemPhotoClick(position);
                    }
                }
            });
        }
         holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnPhotoClickListener != null){
                        mOnPhotoClickListener.onPhotoClick(position);
                    }
                }
            });
        return convertView;
    }

    public  interface  OnDelItemPhotoClickListener{
        void onDelItemPhotoClick(int position);
    }

    public OnDelItemPhotoClickListener mOnDelItemPhotoClickListener;

    public void setOnDelItemPhotoClickListener(OnDelItemPhotoClickListener mOnDelItemPhotoClickListener){
        this.mOnDelItemPhotoClickListener = mOnDelItemPhotoClickListener;
    }
    public  interface  OnPhotoClickListener{
        void onPhotoClick(int position);
    }

    public OnPhotoClickListener mOnPhotoClickListener;

    public void setOnPhotoClickListener(OnPhotoClickListener mOnPhotoClickListener){
        this.mOnPhotoClickListener = mOnPhotoClickListener;
    }

    public class ViewHolder {
        public ImageView image;
        public Button bt_del;
    }
}
