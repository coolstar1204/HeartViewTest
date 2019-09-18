package com.jgx.heartviewtest.heartviews;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 心形状效果
 * Created by jiguangxing on 2016/2/27.
 */
public class HeartElement extends BaseElement {
    private Drawable drawableElement;

    public HeartElement(Rect rect) {
        super(rect);
    }

    @Override
    void draw(Canvas canvas) {
        if(drawableElement!=null){
            drawableElement.draw(canvas);
        }
    }

    @Override
    void step() {

    }

    @Override
    void setTouchDownXY(int x, int y) {

    }

    @Override
    void setTouchMoveXY(int x, int y) {

    }

    @Override
    void setTouchUpXY(int x, int y) {

    }

    @Override
    void release() {
        drawableElement.setCallback(null);
    }

    @Override
    public void setRect(Rect rect) {
        super.setRect(rect);
        if(rect!=null&&drawableElement!=null&&rect.width()>0&&rect.height()>0){
            drawableElement.setBounds(rect);
        }
    }


    public void setDrawableElement(Drawable drawableElement) {
        this.drawableElement = drawableElement;
    }

    public Rect getDrawableBound(){
        if(drawableElement!=null){
            Rect bmpRect = new Rect(0,0,drawableElement.getIntrinsicWidth(),drawableElement.getIntrinsicHeight());
            return bmpRect;
        }
        return null;
    }
}
