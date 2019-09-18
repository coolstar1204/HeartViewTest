package com.jgx.heartviewtest.heartviews;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by jiguangxing on 2016/2/29.
 */
public class StarElement extends BaseElement {
    private BitmapDrawable bmpDrawable;
    private int duration;  //动画间隔时长，ms为单位
    private boolean alphaAdd=false;
    private int bmpAlpha;
    private int mStarId;

    public StarElement(Rect rect) {
        super(rect);
        duration=64;
        bmpAlpha = 255;
    }

    @Override
    void draw(Canvas canvas) {
        if(bmpDrawable!=null){
            bmpDrawable.draw(canvas);
        }
    }

    @Override
    void step() {
        duration -=16;
        if(duration>0){
            return;
        }
        duration = 64;
        if(bmpDrawable!=null){
            if(alphaAdd){
                bmpAlpha+=5;
            }else{
                bmpAlpha-=5;
            }
            if(bmpAlpha>=255){
                alphaAdd = false;
            }else if(bmpAlpha<30){
                alphaAdd = true;
            }
//            Log.d("Alpha",""+bmpAlpha);
            bmpDrawable.getPaint().setAlpha(bmpAlpha);
        }
    }

    @Override
    public void setRect(Rect rect) {
        super.setRect(rect);
        if(rect!=null&&rect.width()>0&&rect.height()>0&&bmpDrawable!=null){
            bmpDrawable.setBounds(rect);
        }
    }
    public Rect getDrawableBound(){
        if(bmpDrawable!=null){
            Rect bmpRect = new Rect(0,0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
            return bmpRect;
        }
        return null;
    }

    public void setBmpDrawable(BitmapDrawable bmpDrawable) {
        this.bmpDrawable = bmpDrawable;
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
        if (bmpDrawable != null){
            bmpDrawable.setCallback(null);
        }
    }

    public void setStartId(int starId) {
        mStarId = starId;
    }

    public int getmStarId() {
        return mStarId;
    }

    public void setmStarId(int mStarId) {
        this.mStarId = mStarId;
    }
}
