package com.jgx.heartviewtest;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

	/**
	 * Decode and sample down a bitmap from resources to the requested width and
	 * height.
	 * 
	 * @param res
	 *            The resources object containing the image data
	 * @param resId
	 *            The resource id of the image data
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
			int reqHeight) {

		// BEGIN_INCLUDE (read_bitmap_dimensions)
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		
		options.inJustDecodeBounds = true;
		if (!Utils.hasHoneycomb()) {
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		}
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = CalculateStrategyCommonBitmapSize(options, reqWidth, reqHeight);
		// END_INCLUDE (read_bitmap_dimensions)

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and
	 * height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		if (!Utils.hasHoneycomb()) {
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		}
		return decodeSampledBitmapFromFile(filename, reqWidth, reqHeight, options);
	}
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight,final BitmapFactory.Options opts) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = opts;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filename, options);
//		if (reqHeight <= 50 && opts.outHeight <= 100) 
//			reqHeight = -1;
//		if (reqWidth <= 50 && opts.outWidth <= 100) 
//			reqWidth = -1;
		
		// Calculate inSampleSize
		options.inSampleSize = CalculateStrategyCommonBitmapSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * Decode and sample down a bitmap from a file input stream to the requested
	 * width and height.
	 * 
	 * @param fileDescriptor
	 *            The file descriptor to read from
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		if (!Utils.hasHoneycomb()) {
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		}
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = CalculateStrategyCommonBitmapSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	}
	/**
	 * 计算不同策略下，通用图标的缩放策略值
	 */
	public static int CalculateStrategyCommonBitmapSize(BitmapFactory.Options options, int reqWidth,int reqHeight){
		int sampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
		return sampleSize;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
											 int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		float rateVal = 1.0f;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				rateVal = (float) height / (float) reqHeight;
			} else {
				rateVal = (float) width / (float) reqWidth;
			}
			inSampleSize = (int)Math.floor(rateVal+0.7f); //此处加0.7，然后用floor向下取整，可让540的在加载720图时，也缩放加载
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromStream(Uri uri,ContentResolver resolver, int reqWidth, int reqHeight) {
		InputStream stream = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		if (!Utils.hasHoneycomb()) {
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		}
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, options);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Calculate inSampleSize
		options.inSampleSize = CalculateStrategyCommonBitmapSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		InputStream stream1 = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
		Bitmap bm = BitmapFactory.decodeStream(stream1, null, options);
		try {
			stream1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}

	@TargetApi(VERSION_CODES.KITKAT)
	public static int getBitmapSize(BitmapDrawable value) {
		Bitmap bitmap = value.getBitmap();
		if (bitmap == null) {
			return 0; //ui 1.0 ab huawei npe
		}
		if (Utils.hasKitKat()) {
			return bitmap.getAllocationByteCount();
		}
		if (Utils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public static boolean isBitmapNeedResize(String srcBmpFile, int upWidth, int upHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;  //先获取一下源文件大小
		BitmapFactory.decodeFile(srcBmpFile,opts);
		int upMaxSize = upWidth>upHeight?upWidth:upHeight;
		int srcMaxSize = opts.outWidth>opts.outHeight?opts.outWidth:opts.outHeight;
		if(srcMaxSize>upMaxSize*2){
			return true;  //如果源图大于目标二倍，则必须缩放加载
		}
		return false;
	}
}
