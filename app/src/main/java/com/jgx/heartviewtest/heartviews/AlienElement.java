package com.jgx.heartviewtest.heartviews;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 外星人
 * Created by jiguangxing on 2016/2/27.
 */
public class AlienElement extends BaseElement {

    private static final int ANGLE_MAX = 10;

    public AlienElement(Rect rect) {
        super(rect);
        duration = 48;
        rotateSpeed = 1;
    }

    private Drawable bmpDrawable;
    private int duration;  //动画间隔时长，ms为单位
    private Rect bmpRect;
    private float rotateVal;
    private float rotateX;
    private float rotateY;
    private float rotateSpeed;
    private boolean isRotateAdd;
    @Override
    void draw(Canvas canvas) {
        if(bmpDrawable!=null){
            canvas.save();
            Matrix matrix = canvas.getMatrix();
            matrix.preRotate(rotateVal,rotateX,rotateY);
            canvas.setMatrix(matrix);
            bmpDrawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    void step() {
        duration -=16;
        if(duration>0){
            return;
        }
        duration = 48;
        if(isRotateAdd){
            rotateVal +=rotateSpeed;
        }else{
            rotateVal -=rotateSpeed;
        }
        if(rotateVal>ANGLE_MAX){
            rotateVal = ANGLE_MAX;
            isRotateAdd = false;
        }else if(rotateVal<-ANGLE_MAX){
            rotateVal = -ANGLE_MAX;
            isRotateAdd = true;
        }
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
        bmpDrawable.setCallback(null);
    }

    @Override
    public void setRect(Rect rect) {
        super.setRect(rect);
        if(rect!=null&&rect.width()>0&&rect.height()>0&&bmpDrawable!=null){
            bmpDrawable.setBounds(rect);
            rotateX = getRect().centerX();
            rotateY = getRect().centerY();
        }
    }
    public Rect getDrawableBound(){
        if(bmpDrawable!=null){
            bmpRect = new Rect(0,0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
            return bmpRect;
        }
        return null;
    }

    public void setBmpDrawable(Drawable bmpDrawable) {
        this.bmpDrawable = bmpDrawable;
        if(bmpDrawable!=null){
            bmpRect = new Rect(0,0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
        }
    }

    public void setRotateAdd(boolean rotateAdd) {
        isRotateAdd = rotateAdd;
    }
}
