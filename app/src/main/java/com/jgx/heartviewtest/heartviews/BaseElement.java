package com.jgx.heartviewtest.heartviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 自定义绘制元素基类
 * @author gx
 *
 */
public abstract class BaseElement {
	protected Rect rect=null;
	
	/**元素绘制自身方法*/
	abstract void draw(final Canvas canvas);
	abstract void step();
	/**触摸事件响应*/
	abstract void setTouchDownXY(int x,int y);
	abstract void setTouchMoveXY(int x,int y);
	abstract void setTouchUpXY(int x,int y);
	abstract void release();
	
	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public BaseElement(Rect rect) {
		super();
		this.rect = rect;
	}

}
