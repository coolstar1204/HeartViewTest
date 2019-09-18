package com.jgx.heartviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jgx.heartviewtest.heartviews.ElementBuilder;
import com.jgx.heartviewtest.heartviews.HeartGameView;

public class MainActivity extends AppCompatActivity {

    private HeartGameView mHeartGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHeartGameView = (HeartGameView) findViewById(R.id.heartview_root);
        int width = ScreenUtils.getScreenWidth() == 0 ? 480 : ScreenUtils.getScreenWidth();
        int height = ScreenUtils.getScreenHeight() == 0 ? 800 : ScreenUtils.getScreenHeight();
        Bitmap bm = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.ksing_telepathy_bg, width, height);
        mHeartGameView.addBgDrawable(bm);
        Resources res = getResources();
        loadAnimElement(res);
        mHeartGameView.setNotDrawElement(false);
        mHeartGameView.setEventLoadingListener(new HeartGameView.OnLoadingListener() {
            @Override
            public void onStartLoading(int direction) {
                ToastUtils.showShort("触摸触发加载动画");
                mHeartGameView.setLoadingEnd();
            }

            @Override
            public void onTouchDown(MotionEvent event) {

            }

            @Override
            public void onTouchUp(MotionEvent event) {

            }

            @Override
            public void onClick(MotionEvent event) {
                ToastUtils.showShort("点击事件响应");
            }
        });
        mHeartGameView.setLoadingEnd();
    }

    private void loadAnimElement(Resources res) {
        Drawable star1 = res.getDrawable(R.drawable.heart_yellow_star);     //闪烁星球1
        Drawable star2 = res.getDrawable(R.drawable.heart_purple_star);     //闪烁星球2
        Drawable heart = res.getDrawable(R.drawable.heart_heart);           //大红心
        mHeartGameView.addFixedElement(ElementBuilder.buildHeartElement(heart),
                ElementBuilder.buildStarElement(star1, 1),
                ElementBuilder.buildStarElement(star2, 2)
        );

        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.heart_clouds);//云彩
        mHeartGameView.addElement(ElementBuilder.buildSmokeElement(bmp));

        Drawable alien = res.getDrawable(R.drawable.heart_alien); //外星人
        mHeartGameView.addElement(ElementBuilder.buildAlienElement(alien));

        Bitmap noteBmp = BitmapFactory.decodeResource(res, R.drawable.heart_note1);
        Bitmap noteBmp2 = BitmapFactory.decodeResource(res, R.drawable.heart_note2);
        Bitmap noteBmp3 = BitmapFactory.decodeResource(res, R.drawable.heart_note3);
        mHeartGameView.addElement(ElementBuilder.buildNoteElement(noteBmp, noteBmp2, noteBmp3)); //音符效果
    }
}
