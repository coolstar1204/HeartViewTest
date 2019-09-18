/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jgx.heartviewtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class containing some static utility methods.
 */
public class Utils {
	public static final int SINGER = 1;
	public static final int OTHER = 2;//除了歌手，剩下的都按照中间裁

	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasIceCreamSandwich() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}



	public static long getTotalRam(Context context) {
		final String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long totalRam = -1;
		BufferedReader bufferedReader = null;
		try {
			FileReader fileReader = new FileReader(str1);
			bufferedReader = new BufferedReader(fileReader, 8192);
			// 读取MEMINFO第一行，系统总内存大小
			str2 = bufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			// 获得系统总内存，单位是KB，乘以1024转换为Byte
			totalRam = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return totalRam;
	}

	public static Bitmap getBitmap(Bitmap bitmapOrg, int width, int height ,int type) {
		int startX = 0;
		int startY = 0;
		int bitmapWidth = width;
		int bitmapHeight = height;

		// 宽度需要裁剪,裁剪后的宽度为屏幕的宽度,从中间截图
		if (bitmapOrg.getWidth() > width) {
			startX = (bitmapOrg.getWidth() - width) / 2;
		} else {// 不要对图片进行裁减,宽度为图片的宽度
			bitmapWidth = bitmapOrg.getWidth();
		}
		//无论如何都要对高度进行裁剪的
		bitmapHeight = (int) (bitmapWidth * height / (float)width);
		if (type == SINGER) {
			//起始裁剪的位置为剩余空间的1/3处
			startY = (bitmapOrg.getHeight() - bitmapHeight) / 3;
		}else {
			//起始裁剪的位置为剩余空间的1/2处
			startY = (bitmapOrg.getHeight() - bitmapHeight) / 2;
		}
		// 可能的异常处理,防止OOM的try catch
		Bitmap bitmap = null;
		try {
			if (startY < 0)
				startY = 0;
			if (bitmapOrg.getHeight() < bitmapHeight)
				bitmapHeight = bitmapOrg.getHeight();
			if (startX < 0)
				startX = 0;
			if (bitmapOrg.getWidth() < bitmapWidth)
				bitmapWidth = bitmapOrg.getWidth();
			bitmap = Bitmap.createBitmap(bitmapOrg, startX, startY, bitmapWidth, bitmapHeight);
		} catch (Throwable e) {
		}
		return bitmap;
	}
}
