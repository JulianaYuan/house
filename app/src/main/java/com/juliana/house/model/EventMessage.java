package com.juliana.house.model;

import android.graphics.Bitmap;

import java.util.List;

public class EventMessage {
    public int type;
    public List<Bitmap> obj;
    public EventMessage(int t,List<Bitmap> ob){
        this.type = t;
        this.obj = ob;
    }
}
