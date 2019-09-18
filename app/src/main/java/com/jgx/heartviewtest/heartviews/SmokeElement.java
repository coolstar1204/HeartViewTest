package com.jgx.heartviewtest.heartviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by jiguangxing on 2016/2/27.
 */
public class SmokeElement extends BaseElement {

    private int duration;  //动画间隔时长，ms为单位
    private int mMoveLeft;
    private int mTargetWidth;
    private int mTargetHeight;
    private Rect bmpRect;
    private int hideCount;  //控制透明状态时长
    private Paint bmpPaint;
    private Rect startRect;
    private Bitmap bmpCloud;
    private int bmpAlpha;

    public SmokeElement(Rect rect) {
        super(rect);
        duration =32;
        bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpPaint.setAntiAlias(true);
        bmpPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    void draw(Canvas canvas) {
        if(bmpCloud !=null){
            bmpPaint.setAlpha(bmpAlpha);
            canvas.drawBitmap(bmpCloud,bmpRect,getRect(), bmpPaint);
        }

    }

    @Override
    void step() {
        duration -=16;
        if(duration>0){
            return;
        }
        duration = 32;
        if(bmpCloud!=null){
            bmpAlpha = bmpPaint.getAlpha();// .getOpacity();
            bmpAlpha-=5;
            if(bmpAlpha<0){
                bmpAlpha = 0;
            }
            Rect boundRect = getRect();
            if(boundRect.width()<mTargetWidth){
                boundRect.right++;
            }
            if(boundRect.height()<mTargetHeight){
                boundRect.bottom++;
            }
            if(boundRect.left>mMoveLeft){
                boundRect.left--;
                boundRect.right--;
            }else{
                hideCount++;
                if(hideCount>29){ //控制透明状态时长为 30*64=1.92秒
                    hideCount=0;
                    bmpAlpha = 255;
                    boundRect.set(startRect);
                }
            }
            setRect(boundRect);
            bmpPaint.setAlpha(bmpAlpha);
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
        if (bmpCloud != null && !bmpCloud.isRecycled()){
            bmpCloud.recycle();
        }
        bmpPaint.reset();
    }


    @Override
    public void setRect(Rect rect) {
        super.setRect(rect);
        if(rect!=null&&startRect==null){
            startRect =  new Rect();
            startRect.set(rect);
        }
    }
    public Rect getDrawableBound(){
        if(bmpCloud!=null){
            if(bmpRect==null){
                bmpRect = new Rect(0,0,bmpCloud.getWidth(),bmpCloud.getHeight());
            }
            return bmpRect;
        }
        return null;
    }

    public void setMoveLeft(int mMoveLeft) {
        this.mMoveLeft = mMoveLeft;
    }

    public void setBmpCloud(Bitmap bmpCloud) {
        this.bmpCloud = bmpCloud;
        if(bmpCloud!=null){
            bmpRect = new Rect(0,0,bmpCloud.getWidth(),bmpCloud.getHeight());
            mTargetWidth = (int) (bmpCloud.getWidth()*1.2f);
            mTargetHeight = (int) (bmpCloud.getHeight()*1.2f);
        }
    }
}
