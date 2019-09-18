package com.jgx.heartviewtest.heartviews;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 元素创建帮助类
 * Created by jiguangxing on 2016/2/27.
 */
public class ElementBuilder {
    public static StarElement buildStarElement(Drawable drawable,int starId){
        StarElement item = new StarElement(null);
        item.setStartId(starId);
        item.setBmpDrawable((BitmapDrawable)drawable);
        return item;
    }

    public static HeartElement buildHeartElement(Drawable drawable){
        HeartElement item = new HeartElement(null);
        item.setDrawableElement(drawable);
        return item;
    }
    public static BaseElement buildSmokeElement(Bitmap bmp){
        SmokeElement item = new SmokeElement(null);
        item.setBmpCloud(bmp);
        return item;
    }

    public static BaseElement buildAlienElement(Drawable drawable){
        AlienElement item = new AlienElement(null);
        item.setBmpDrawable(drawable);
        item.setRotateAdd(false);
        return item;
    }

    public static BaseElement buildNoteElement(Bitmap noteBmp,Bitmap noteBmp2,Bitmap noteBmp3){
        NoteElement item = new NoteElement(null);
        item.addNote(noteBmp,noteBmp2,noteBmp3);
        return item;
    }
}
