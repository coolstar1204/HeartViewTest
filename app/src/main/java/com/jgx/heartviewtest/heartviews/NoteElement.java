package com.jgx.heartviewtest.heartviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 音符对象元素
 * Created by jiguangxing on 2016/2/27.
 */
public class NoteElement extends BaseElement {
    private static final int STATE_HIDE     = 0;
    private static final int STATE_ONE      = 1;
    private static final int STATE_TWO      = 2;
    private static final int STATE_THREE    = 3;
    private static final int STATE_FOUR     = 4;
    private static final int STATE_FIVE     = 5;

    private int runState = STATE_HIDE;
    private NoteInfo noteOne;
    private NoteInfo noteTwo;
    private NoteInfo noteThree;
    private int hideCount;

    public NoteElement(Rect rect) {
        super(rect);
        duration = 32;
    }

    @Override
    void draw(Canvas canvas) {
       if(noteOne!=null){
           noteOne.draw(canvas);
       }
       if(noteTwo!=null){
           noteTwo.draw(canvas);
       }
       if(noteThree!=null){
           noteThree.draw(canvas);
       }
    }
    private int duration;  //动画间隔时长，ms为单位
    @Override
    void step() {
        duration -=16;
        if(duration>0){
            return;
        }
        duration = 32;
        switch (runState){
            case STATE_HIDE:
                hideCount++;
                if(hideCount>20){
                    hideCount = 0;
                    noteOne.setLocation(rect.centerX(),rect.bottom,rect.left,rect.top+rect.height()/3*2);
                    runState = STATE_ONE;
                }
                break;
            case STATE_ONE:
                noteOne.addAlpha();
                noteOne.runStep();
                if(noteOne.isArrivedTarget()){
                    noteOne.setLocation(rect.left,rect.top+rect.height()/3*2,rect.left+rect.width(),rect.top+rect.height()/3);
                    noteTwo.setLocation(rect.centerX(),rect.bottom,rect.left,rect.top+rect.height()/3*2);
                    runState = STATE_TWO;
                }
                break;
            case STATE_TWO:
                noteOne.addAlpha();
                noteOne.runStep();
                noteTwo.addAlpha();
                noteTwo.runStep();
                if(noteTwo.isArrivedTarget()){
                    noteOne.setLocation(rect.left+rect.width(),rect.top+rect.height()/3,rect.left,rect.top);
                    noteTwo.setLocation(rect.left,rect.top+rect.height()/3*2,rect.left+rect.width(),rect.top+rect.height()/3);
                    noteThree.setLocation(rect.centerX(),rect.bottom,rect.left,rect.top+rect.height()/3*2);
                    runState = STATE_THREE;
                }
                break;
            case STATE_THREE:
                noteOne.divAlpha();
                noteOne.runStep();
                noteTwo.addAlpha();
                noteTwo.runStep();
                noteThree.addAlpha();
                noteThree.runStep();
                if(noteThree.isArrivedTarget()){
                    noteOne.setAlpha(0);
                    noteTwo.setLocation(rect.left+rect.width(),rect.top+rect.height()/3,rect.left,rect.top);
                    noteThree.setLocation(rect.left,rect.top+rect.height()/3*2,rect.left+rect.width(),rect.top+rect.height()/3);
                    runState = STATE_FOUR;
                }
                break;
            case STATE_FOUR:
                noteTwo.divAlpha();
                noteTwo.runStep();
                noteThree.addAlpha();
                noteThree.runStep();
                if(noteThree.isArrivedTarget()){
                    noteTwo.setAlpha(0);
                    noteThree.setLocation(rect.left+rect.width(),rect.top+rect.height()/3,rect.left,rect.top);
                    runState = STATE_FIVE;
                }
                break;
            case STATE_FIVE:
                noteThree.divAlpha();
                noteThree.runStep();
                if(noteThree.isArrivedTarget()){
                    noteOne.setAlpha(0);
                    runState = STATE_HIDE;
                }
                break;
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
        recycleNoteInfo(noteOne);
        recycleNoteInfo(noteTwo);
        recycleNoteInfo(noteThree);
    }

    private void recycleNoteInfo(NoteInfo info) {
        if (info != null){
            if (!info.noteImg.isRecycled()){
                info.noteImg.recycle();
            }
            if (info.notePaint!= null){
                info.notePaint.reset();
            }
        }
    }

    public void addNote(Bitmap noteBmp, Bitmap noteBmp1, Bitmap noteBmp2) {
        noteOne = new NoteInfo(noteBmp);
        noteTwo = new NoteInfo(noteBmp1);
        noteThree = new NoteInfo(noteBmp2);
    }

    private static class NoteInfo{
        int id;
//        int noteState;
        Bitmap noteImg;
        Paint notePaint;
        float startX;
        float startY;
        float targetX;
        float targetY;
        float speedX;
        float speedY;
        float offetX; //保存成图片大小的一半，用于绘制时偏移左上角坐标，计算时是以图片中心为坐标的。
        float offetY;


        public void setLocation(float startX,float startY,float targetX,float targetY){
            this.startX = startX;
            this.startY = startY;
            this.targetX=targetX;
            this.targetY = targetY;
            speedX = (targetX-startX)/(600/16);  //默认一次设置一秒移到，
            speedY = (targetY-startY)/(600/16);
        }

        public void runStep(){
            if(Math.abs(targetX-startX)<1f){
                targetX = startX;
            }else{
                startX+=speedX;
            }
            if(Math.abs(targetY-startY)<1f){
                targetY = startY;
            }else{
                startY +=speedY;
            }
        }

        public boolean isArrivedTarget(){
            return Math.abs(targetX-startX)<1f&&Math.abs(targetY-startY)<1f;
        }

        public NoteInfo(Bitmap noteImg) {
            this.noteImg = noteImg;
            notePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            notePaint.setAlpha(0);  //默认先透明
            offetX = noteImg.getWidth()/2;
            offetY = noteImg.getHeight()/2;
        }
        public int getAlpha(){
            return notePaint.getAlpha();
        }
        public void setAlpha(int alpha){
            notePaint.setAlpha(alpha);
        }
        public void addAlpha(){
            int curAlpha = getAlpha();
            curAlpha+=10;
            if(curAlpha>255){
                curAlpha = 255;
            }
            setAlpha(curAlpha);
        }
        public void divAlpha(){
            int curAlpha = getAlpha();
            curAlpha-=10;
            if(curAlpha<0){
                curAlpha = 0;
            }
            setAlpha(curAlpha);
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(noteImg,startX-offetX,startY-offetY,notePaint);
        }
    }
}
