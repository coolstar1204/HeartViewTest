package com.jgx.heartviewtest.heartviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class BaseGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
	private static final String TAG = "BaseGameView";
	protected void addLog(String log){
		Log.d(TAG, log);
	}
	//构造函数==============================================================	
	SurfaceHolder Holder;
	public BaseGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public BaseGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public BaseGameView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		if(Holder==null){
			Holder = this.getHolder();// 获取holder
	        Holder.addCallback(this);
		}
		
		setFocusable(true);	
	}
	//SurfaceHolder.Callback接口函数实现==========================================================================================================	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawingFlag = true;
		drawThread = new Thread(this, "surfaceDrawThread");
		drawThread.start();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		addLog("surfaceChanged()=========="+width+","+height+"==>"+format);
		screenSizeChanged(format,width,height);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawingFlag = false;	
		addLog("surfaceDestroyed()==========");
		//线程停止、销毁的方法之一，死循环等待
//		boolean retry = true;
//        thread.setRunning(false);
//        while (retry) {
//            try {
//                thread.join();
//                retry = false;
//
//            } catch (InterruptedException e) {
//            }
//        }
	}
	//界面刷新线程变量及方法==========================================================================================================
	private Thread drawThread;  //启动界面绘制的线程对象
	public static final int TIME_IN_FRAME = 30;
	private boolean drawingFlag;   //界面绘制中变量
	/**run函数为线程函数，此处访问的变量，对象如果在外面还有访问需求，要注意同步*/
	@Override
	public void run() {
		long startTime,endTime;
		while(drawingFlag){ //界面创建后通过这里标记死循环实现定时刷新
			startTime = System.currentTimeMillis();
			/**界面绘制*/
			synchronized (Holder) {
				Canvas canvas=null;
				try {
					canvas = Holder.lockCanvas();
					if(canvas!=null){
						drawStep(canvas);
					}
				} catch (Exception e) {
					addLog(""+e.getMessage());
				}finally{
					if(canvas!=null&&Holder!=null){
						try{
							Holder.unlockCanvasAndPost(canvas);
						}catch (Exception e){}
					}
				}				
			};
			/**逻辑运算*/
			runStep();
			endTime= System.currentTimeMillis();
			 /**计算出游戏一次更新的毫秒数**/  
		    int diffTime  = (int)(endTime - startTime);  
		    /**确保每次更新时间为30帧**/  
		    while(diffTime <=TIME_IN_FRAME) {  
		        diffTime = (int)(System.currentTimeMillis() - startTime);  
		        /**线程等待**/  
		        Thread.yield();  //Thread.yield(): 是暂停当前正在执行的线程对象 ，并去执行其他线程。Thread.sleep(long millis):则是使当前线程暂停参数中所指定的毫秒数然后在继续执行线程
		    }  
//			/**
//			* 1000ms /30 = 33.33ms 这里，我们采用33，使帧率限制在最大30帧
//			* 如果担心发热、耗电问题，同样可以使用稍大一些的值。经测试80基本为最大值。
//			**/
//			if (endTime -startTime < 33) {
//				try{
//					Thread.sleep(33- (endTime - startTime));
//				} catch(InterruptedException e) {
//					e.printStackTrace();
//					drawingFlag = false;
//				}
//			}
		}
	}
	//界面框架方法==========================================================================================================
	/**界面刷新函数，主要是使用逻辑计算后的数据进行界面重画*/
	abstract protected void drawStep(final Canvas canvas);
	/**游戏逻辑计算处理函数，主要用于数值变更*/
	abstract protected void runStep();
	/**当界面大小发生变化时，触发此函数，用于子类进行大小更新*/
	abstract protected void screenSizeChanged(int format,int width,int height);
	//静态工具方法========================================================================
	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}
	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint)  {  
	    FontMetrics fm = paint.getFontMetrics(); 
	    return fm.descent - fm.ascent;  
	} 
	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint)  {  
	    FontMetrics fm = paint.getFontMetrics(); 
	    return fm.leading- fm.ascent;  
	} 
}
