package com.jgx.heartviewtest.heartviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;


/**
 * 心灵感应加载动画控件
 * Created by jiguangxing on 2016/2/27.
 */
public class HeartGameView extends BaseGameView {
    public static final int FLY_RIGHT = 1;
    public static final int FLY_LEFT  = -1;


    public static final int STATE_NORMAL       = 1;
    private static final int STATE_FLYING       = 2;
    private static final int STATE_LOADING      = 3;
    private static final int STATE_RESET        = 4;
    private static final int STATE_SCALEING     = 5;
    private static final int STATE_GOBACK       = 6;

    private Bitmap mBgBitmap;
    private int rotateX;
    private int rotateY;
    private int circleRadius;
    private Matrix martix;
    private int rotateState;
    private int tmpdegrees;
    private int curDegrees;
    private float scaleValue;
    private float speed =1.0f;
    private int degreeSpeed =1;
    private boolean isDragMode;
    private boolean notDrawElement;
    private Rect viewRect,bmpRect;
    private Paint bmpPaint;

    public HeartGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public HeartGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeartGameView(Context context) {
        super(context);
        initView(context);
    }
    //=======================================================================================
    private HeartElement heartElement;
    private StarElement starElement1,starElement2;  //二个不动的星球
    private ArrayList<BaseElement> elementList = new ArrayList<BaseElement>();

    public void addFixedElement(HeartElement heartElement,StarElement star1,StarElement star2){
        this.heartElement = heartElement;
        this.starElement1 = star1;
        this.starElement2 = star2;
    }

    public void addElement(BaseElement element){
        elementList.add(element);
    }
    public void addBgDrawable(Bitmap drawable){
        mBgBitmap = drawable;
        if(mBgBitmap !=null){
            bmpRect.set(0,0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
        }
    }
    private void initView(Context context) {
        scaleValue = 1.0f;
        curDegrees = 60;
        rotateState = STATE_LOADING;
        bmpRect = new Rect();
        viewRect = new Rect();
        bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpPaint.setStyle(Paint.Style.FILL);
        bmpPaint.setAntiAlias(true);
    }

    //=======================================================================================
    @Override
    protected void drawStep(Canvas canvas) {
        //绘制背景图片或透明
        if(mBgBitmap !=null){
            canvas.drawBitmap(mBgBitmap,bmpRect,viewRect,bmpPaint);
        }else{
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);  //这样绘制才能在透明背景去掉残影
        }
        //绘制背景上流星元素1
        if(starElement1!=null){
            starElement1.draw(canvas);
        }
        //绘制背景上流星元素2
        if(starElement2!=null){
            starElement2.draw(canvas);
        }
        if(notDrawElement){
            return;
        }
        //根据用户操作或场景动画需要，旋转画布。实现动态整体移动、旋转、淡入淡出等效果
        martix = canvas.getMatrix();
        canvas.save();
        updateCanvasMatrix();
        canvas.setMatrix(martix);
        //主要心形元素动画绘制
        if(heartElement!=null){
            heartElement.draw(canvas);  //顺序很重要，绘制时谁在后面谁先画，在上面的后绘制
        }
        //心形动画元素上面子动画元素绘制
        if(elementList!=null&&elementList.size()>0){
            for(int i=0;i<elementList.size();i++){
                elementList.get(i).draw(canvas);
            }
        }
        canvas.restore();
    }

    private void updateCanvasMatrix() {
        martix.postRotate(curDegrees,rotateX,rotateY);
        martix.preScale(scaleValue,scaleValue,getWidth()/2,getHeight()/2);
    }

    @Override
    protected void runStep() {
        if(starElement1!=null){
            starElement1.step();
        }
        if(starElement2!=null){
            starElement2.step();
        }
        if(notDrawElement){
            return;
        }
        updateRotateValue();
        if(heartElement!=null){
            heartElement.step();
        }
        if(elementList!=null&&elementList.size()>0){
            for(int i=0;i<elementList.size();i++){
                elementList.get(i).step();
            }
        }
    }

    private void updateRotateValue() {
        switch (rotateState){
            case STATE_NORMAL:
                scaleValue = 1.0f;
                speed = 0.1f;
                degreeSpeed = 1;
                break;
            case STATE_FLYING:
                if(speed<5.0f){
                    speed+=0.2;
                }
                curDegrees +=speed;
                if(curDegrees >=50){
                    rotateState = STATE_LOADING;
                    if(eventLoadingListener!=null){
                        eventLoadingListener.onStartLoading(FLY_RIGHT);
                    }
                }
                break;
            case STATE_GOBACK:
                if(speed<5.0f){
                    speed+=0.2;
                }
                curDegrees -=speed;
                if(curDegrees <=-50){
                    rotateState = STATE_LOADING;
                    if(eventLoadingListener!=null){
                        eventLoadingListener.onStartLoading(FLY_LEFT);
                    }
                }
                break;
            case STATE_LOADING:

                break;
            case STATE_SCALEING:
                scaleValue +=0.05;
                if(scaleValue>1.0f){
                    scaleValue = 1.0f;
                    rotateState = STATE_NORMAL;
                }
                break;
            case STATE_RESET:
                if(degreeSpeed<5){
                    degreeSpeed+=1;
                }
                if(curDegrees>0){
                    curDegrees -= degreeSpeed;
                    if(curDegrees<0){
                        curDegrees = 0;
                    }
                }else if(curDegrees<0){
                    curDegrees += degreeSpeed;
                    if(curDegrees>0){
                        curDegrees = 0;
                    }
                }
                if(curDegrees==0){
                    rotateState = STATE_NORMAL;
                }
                break;
        }
    }

    @Override
    protected void screenSizeChanged(int format, int width, int height) {
        viewRect.set(0,0,width,height);
        updateHeartElement(width,height);  //先更新心形所在位置
        updateStarElementRect(starElement1,width,height);
        updateStarElementRect(starElement2, width, height);

        for(int i=0;i<elementList.size();i++){
            BaseElement element = elementList.get(i);
            if(element instanceof NoteElement){
                updateNoteElemnet((NoteElement) element, width, height);
            }else if(element instanceof SmokeElement){
                updateSmokeElement((SmokeElement) element, width, height);
            }else if(element instanceof AlienElement){
                updateAlienElement((AlienElement) element, width, height);
            }
        }
        rotateX = width/2;
        rotateY = height+height/4;
        circleRadius = height/2+height/4;
    }

    private void updateNoteElemnet(NoteElement element, int width, int height) {
        if(heartElement!=null){
            Rect heartRect = heartElement.getRect();
            Rect localRect = new Rect();
            localRect.left = heartRect.left+heartRect.width()/2+ SizeUtils.dp2px(22);
            localRect.top = heartRect.top+SizeUtils.dp2px(10);
            localRect.right= localRect.left+SizeUtils.dp2px(39);
            localRect.bottom = localRect.top + SizeUtils.dp2px(100);

            element.setRect(localRect);
        }
    }

    private void updateAlienElement(AlienElement element, int width, int height) {
        if(heartElement!=null){
            Rect heartRect = heartElement.getRect();
            Rect tarRect = element.getDrawableBound();

            Rect localRect = new Rect();
            localRect.left = heartRect.left + (heartRect.width()-tarRect.width())/2+SizeUtils.dp2px(17);
            localRect.top = heartRect.top + (heartRect.height()-tarRect.height())/2;
            localRect.right = localRect.left+tarRect.width();
            localRect.bottom = localRect.top+tarRect.height();
            element.setRect(localRect);
        }
    }

    private void updateHeartElement(int width, int height) {
        if(heartElement!=null){
            Rect elementRect = heartElement.getDrawableBound();
            Rect localRect = new Rect();
            localRect.left = (width-elementRect.width())/2;
            localRect.top = (height-elementRect.height())/2-SizeUtils.dp2px(60);
            localRect.right = localRect.left+elementRect.width();
            localRect.bottom = localRect.top + elementRect.height();
            heartElement.setRect(localRect);
        }
    }

    private void updateStarElementRect(StarElement element, int width, int height) {
        if(heartElement!=null){
            Rect heartRect = heartElement.getRect();
            Rect starRect = element.getDrawableBound();
            Rect localRect = new Rect();

            if(element.getmStarId()==1){  //黄色星球位置
                localRect.left = heartRect.left-SizeUtils.dp2px(10);
                localRect.top = heartRect.top+SizeUtils.dp2px(13);
                localRect.right = localRect.left+starRect.width();
                localRect.bottom = localRect.top + starRect.height();
            }else if(element.getmStarId()==2){ //紫色星球位置
                localRect.left = heartRect.right-SizeUtils.dp2px(10);
                localRect.top = heartRect.top+heartRect.height()/2+SizeUtils.dp2px(30);
                localRect.right = localRect.left+starRect.width();
                localRect.bottom = localRect.top + starRect.height();
            }
            element.setRect(localRect);
        }
    }

    private void updateSmokeElement(SmokeElement element, int width, int height) {
        if(heartElement!=null){
            Rect heartRect = heartElement.getRect();
            Rect tarRect = element.getDrawableBound();

            Rect localRect = new Rect();
            localRect.left = heartRect.left+SizeUtils.dp2px(15);
            localRect.top = heartRect.top+SizeUtils.dp2px(85);
            localRect.right = localRect.left+tarRect.width();
            localRect.bottom = localRect.top + tarRect.height();

            element.setRect(localRect);
            element.setMoveLeft(heartRect.left);
        }
    }
    //=======================================================================================
    private float dX, dY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(rotateState!=STATE_NORMAL){
            return true;  //非默认状态，不处理触摸事件
        }
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                if(heartElement!=null&&heartElement.getRect()!=null&&heartElement.getRect().contains((int)downX,(int)downY)){
                    isDragMode = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    tmpdegrees = computeMigrationAngle(event.getX(),event.getY());
                    if(eventLoadingListener!=null){
                        eventLoadingListener.onTouchDown(event);
                    }
                }
                dX = event.getX();
                dY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDragMode){
                    int moveDegree = computeMigrationAngle(event.getX(),event.getY());
                    curDegrees +=moveDegree;
                }
                break;
            case MotionEvent.ACTION_UP:
                float uX = event.getX();
                float uY = event.getY();
                if (Math.abs(uX - dX) < 16 &&
                        Math.abs(uY - dY) < 16) {
                    if (eventLoadingListener != null) {
                        eventLoadingListener.onClick(event);
                    }
                }
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                onDegreesChanged();
                tmpdegrees = 0;
                if(eventLoadingListener!=null){
                    eventLoadingListener.onTouchUp(event);
                }
                isDragMode = false;
                break;
        }
        return true;
    }

    public boolean isClickHeart(int x,int y){
        if(heartElement!=null){
            if(heartElement.getRect()!=null&&heartElement.getRect().contains(x,y)){
                return true;
            }
        }
        return false;
    }
    /**
     *
     * 方法名：computeMigrationAngle 功能：计算偏移角度 参数：
     *
     * @param x
     * @param y
     *
     */
    private int computeMigrationAngle(float x, float y) {
        int a = 0;
        float distance = (float) Math.sqrt(((x - rotateX) * (x - rotateX) + (y - rotateY)* (y - rotateY)));
        int degree = (int) (Math.acos((x - rotateX) / distance) * 180 / Math.PI);
        Log.d("Degrees",x+","+y+"--"+rotateX+","+rotateY+"=="+degree);
        if (tmpdegrees != 0) {
            a = tmpdegrees - degree;
        }
        tmpdegrees = degree;
        return a;
    }

    private void onDegreesChanged() {
        if(Math.abs(curDegrees)<40){
            onRotateReset();  //复位不触发加载
        }else{
            onRoateStart();
        }
    }

    private void onRotateReset() {
        if(rotateState==STATE_NORMAL){
            rotateState = STATE_RESET;
        }
    }

    private void onRoateStart() {
        if(rotateState==STATE_NORMAL){
            curDegrees = curDegrees>0?50:-50;
            rotateState = STATE_LOADING;
            if(eventLoadingListener!=null){
                eventLoadingListener.onStartLoading(curDegrees>0?1:-1);
            }
        }
    }

    public void setStartLoading(int direction){
        if(rotateState==STATE_NORMAL){
            speed = 1.0f;
            curDegrees = 0;
            if(direction==FLY_LEFT){
                rotateState = STATE_GOBACK;
            }else{
                rotateState = STATE_FLYING;
            }
        }
    }

    public void setLoadingEnd(){
        if(rotateState==STATE_LOADING){
            scaleValue = 0.1f;
            curDegrees = 0;
            rotateState = STATE_SCALEING;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBgBitmap != null && !mBgBitmap.isRecycled()) {
            mBgBitmap.recycle();
        }
        releaseElement(heartElement);
        releaseElement(starElement1);
        releaseElement(starElement2);
        if (elementList != null) {
            for (BaseElement element : elementList) {
                releaseElement(element);
            }
            elementList.clear();
        }
    }

    private void releaseElement(BaseElement element){
        if (element != null){
            element.release();
        }
    }


    public int getStatus() {
        return rotateState;
    }

    private OnLoadingListener eventLoadingListener;

    public void setEventLoadingListener(OnLoadingListener eventLoadingListener) {
        this.eventLoadingListener = eventLoadingListener;
    }

    public void setNotDrawElement(boolean notDrawElement) {
        this.notDrawElement = notDrawElement;
    }


    public static interface OnLoadingListener{
        /**
         * 当心形飞出屏幕，触发此回调，进行加载事件
         * 注意：此回调有时是在线程中触发，不要处理界面事件
         * 不要长时间执行
         * @param direction
         */
        void onStartLoading(int direction);
        void onTouchDown(MotionEvent event);
        void onTouchUp(MotionEvent event);
        void onClick(MotionEvent event);
    }
}
