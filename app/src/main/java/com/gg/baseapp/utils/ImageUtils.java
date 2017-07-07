package com.gg.baseapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ImageUtils extends AbstractUtils {

	private Context mContext;

	private static final int IMAGE_SIZE = 1440;

	private static final long IMAGE_FILE_SIZE_MAX = 150 * 1024;

	public ImageUtils(Context context) {
		this.mContext = context;
	}

	public Bitmap resize(Bitmap originalBitmap, int size) {
		int w = originalBitmap.getWidth();
		int h = originalBitmap.getHeight();
		if (Math.max(w, h) > size) {
			int scalledW;
			int scalledH;
			if (w <= h) {
				scalledW = (int) (w / ((double) h / size));
				scalledH = size;
			} else {
				scalledW = size;
				scalledH = (int) (h / ((double) w / size));
			}
			Bitmap scalledBitmap = Bitmap.createScaledBitmap(originalBitmap,
					scalledW, scalledH, true);
			return scalledBitmap;
		} else {
			return originalBitmap;
		}
	}

	public Bitmap rotate(Bitmap bitmap, int degree) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(degree);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	public String getOriginalPath(Uri uri) {
		String path = null;
		if (uri.getScheme().equals("file")) {
			return uri.getPath();
		} else if (uri.toString().startsWith("content://media/")) {
			String[] projection = { MediaStore.MediaColumns.DATA };
			Cursor metaCursor = mContext.getContentResolver().query(uri,
					projection, null, null, null);
			if (metaCursor != null) {
				try {
					if (metaCursor.moveToFirst()) {
						path = metaCursor.getString(0);
					}
				} finally {
					metaCursor.close();
				}
			}
		}
		return path;
	}

	private int getRotation(Uri image) {
		try {
			InputStream is = mContext.getContentResolver().openInputStream(
					image);
			return ExifHelper.getOrientation(is);
		} catch (FileNotFoundException e) {
			return 0;
		}
	}

	private int getRotation(String path) {
		try {
			InputStream is = new FileInputStream(path);
			return ExifHelper.getOrientation(is);
		} catch (FileNotFoundException e) {
			return 0;
		}
	}

	public static int getFileSizeBitmap(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public static Bitmap compressImage(Bitmap image) {

		if(image==null || image.isRecycled()) return null;
		int newWidth = (image.getWidth() < image.getHeight() ? 700 : 1400);
		if (newWidth > image.getWidth())
			newWidth = image.getWidth();
		int newHeight = (int) (((double) (newWidth) / (double) image.getWidth()) * image
				.getHeight());
		// image = getSmallBitmap(image, newWidth, newHeight);
		image = Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 1000) { // 循环判断如果压缩后图片是否大于150kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if (options <= 50)
				break;
		}
		image.recycle();
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
		int sampleSize = 1;
		Bitmap bitmap = null;
		try{
			bmpOptions.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(isBm, null, bmpOptions);// 把ByteArrayInputStream数据生成图片
		}catch (OutOfMemoryError e) {
			sampleSize ++;
			bmpOptions.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(isBm, null, bmpOptions);// 把ByteArrayInputStream数据生成图片
		}
		Log.w("photo compressed size " + String.valueOf(baos.toByteArray().length));
		return bitmap;
	}

	public static Bitmap getSmallBitmap(Bitmap bitmap, int maxWidth,
			int maxHeight) {
		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();

		// no need to resize
		if (originWidth <= maxWidth && originHeight <= maxHeight) {
			return bitmap;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] photoBytes = baos.toByteArray();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory
				.decodeByteArray(photoBytes, 0, photoBytes.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxWidth,
				maxHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length,
				options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	@SuppressLint("NewApi")
	public File compressImage(Uri uri, int sampleSize) {
		try {
			InputStream is = mContext.getContentResolver().openInputStream(uri);
			File file = new File(mContext.getCacheDir()
					+ "/duckrtmppic/duckr_tmp.jpg");
			file.getParentFile().mkdirs();
			file.createNewFile();
			Bitmap originalBitmap;
			BitmapFactory.Options options = new BitmapFactory.Options();
			int inSampleSize = (int) Math.pow(2, sampleSize);
			options.inSampleSize = inSampleSize;
			originalBitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
			if (originalBitmap == null) {
				return null;
			}
			// if(getFileSizeBitmap(originalBitmap)>IMAGE_FILE_SIZE_MAX){
			//
			// }
			Bitmap scalledBitmap = resize(originalBitmap, IMAGE_SIZE);
			originalBitmap = null;
			int rotation = getRotation(uri);
			if (rotation > 0) {
				scalledBitmap = rotate(scalledBitmap, rotation);
			}
			OutputStream os = new FileOutputStream(file);
			boolean success = scalledBitmap.compress(
					CompressFormat.WEBP, 75, os);
			if (!success) {
				return null;
			}
			os.flush();
			os.close();
			long size = file.length();
			if (size > IMAGE_FILE_SIZE_MAX) {
				++sampleSize;
				return compressImage(uri, sampleSize);
			}
			int width = scalledBitmap.getWidth();
			int height = scalledBitmap.getHeight();
			return file;
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		} catch (SecurityException ignored) {
		} catch (OutOfMemoryError e) {
			++sampleSize;
			if (sampleSize <= 3) {
				return compressImage(uri, sampleSize);
			} else {
			}
		}
		return null;
	}

	@SuppressLint("NewApi")
	public File compressImage(String path, int sampleSize) {
		try {
			InputStream is = new FileInputStream(path);
			File file = new File(mContext.getCacheDir()
					+ "/duckrtmppic/duckr_tmp.jpg");
			file.getParentFile().mkdirs();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			Bitmap originalBitmap;
			BitmapFactory.Options options = new BitmapFactory.Options();
			int inSampleSize = (int) Math.pow(2, sampleSize);
			options.inSampleSize = inSampleSize;
			originalBitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
			if (originalBitmap == null) {
				return null;
			}
			// if(getFileSizeBitmap(originalBitmap)>IMAGE_FILE_SIZE_MAX){
			//
			// }
			Bitmap scalledBitmap = resize(originalBitmap, IMAGE_SIZE);
			originalBitmap = null;
			int rotation = getRotation(path);
			if (rotation > 0) {
				scalledBitmap = rotate(scalledBitmap, rotation);
			}
			OutputStream os = new FileOutputStream(file);
			boolean success = scalledBitmap.compress(
					CompressFormat.WEBP, 75, os);
			if (!success) {
				return null;
			}
			os.flush();
			os.close();
			long size = getFileSizeBitmap(scalledBitmap);
			// LogHelper.d(APP_TAG,"sizebitmap"+sampleSize+":"+String.valueOf(getFileSizeBitmap(scalledBitmap)));
			// LogHelper.d(APP_TAG,"sizefile"+sampleSize+":"+String.valueOf(file.length()));
			if (size > IMAGE_FILE_SIZE_MAX) {
				++sampleSize;
				return compressImage(path, sampleSize);
			}
			return file;
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		} catch (SecurityException ignored) {
		} catch (OutOfMemoryError e) {
			++sampleSize;
			if (sampleSize <= 3) {
				return compressImage(path, sampleSize);
			} else {
			}
		}
		return null;
	}

	public Bitmap getBitmapFromUri(Uri uri) {
		if(uri==null) return null;
		try {
			// 读取uri所在的图片
//			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//					context.getContentResolver(), uri);
//			return bitmap;
			return decodeBitmap(uri,0);
		} catch (Exception e) {
			Log.e("ImageUtils getbitmapfromuri " + e.toString());
			return null;
		}
	}

	public Bitmap getBitmapFromPath(String path) {
		try {
			return decodeBitmap(path,0);
		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static File saveMyBitmap(String fileName, Bitmap bitmap) {
		File f = new File(fileName);
		if (f.exists())
			f.delete();
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(fOut != null) {
			bitmap.compress(CompressFormat.PNG, 100, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}

	public static void doCrop(Activity activity,Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();
		if (size == 0) {
			Toast.makeText(activity, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();
		} else {
			intent.setData(uri);
			intent.putExtra("outputX", 500);
			intent.putExtra("outputY", 500);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
//			intent.putExtra("scale", true);
			intent.putExtra("crop", true);
			intent.putExtra("return-data", true);
			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));
				activity.startActivityForResult(i, PrefConstants.REQUEST_CROP_PICTURE);
			}
		}
	}

	public static Bitmap getScaledBitmap(int newWidth, int newHeight,
			Bitmap bitmap) {
		if (bitmap == null)
			return null;
		if (newWidth <= 0)
			return bitmap;
		if (newHeight <= 0)
			newHeight = (int) (((double) newWidth / (double) bitmap.getWidth()) * bitmap
					.getHeight());
		bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
		return bitmap;
	}

	public Bitmap decodeBitmap(Uri uri, int sampleSize) {
		try {
			InputStream is = mContext.getContentResolver().openInputStream(uri);
			Bitmap originalBitmap;
			BitmapFactory.Options options = new BitmapFactory.Options();
			int inSampleSize = (int) Math.pow(2, sampleSize);
			options.inSampleSize = inSampleSize;
			originalBitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
			if (originalBitmap == null) {
				Log.e("null bitmap");
				return null;
			}
			int rotation = getRotation(uri);
			if (rotation > 0) {
				originalBitmap = rotate(originalBitmap, rotation);
			}
			return originalBitmap;
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		} catch (SecurityException ignored) {
		} catch (OutOfMemoryError e) {
			++sampleSize;
			return decodeBitmap(uri, sampleSize);
		}
		return null;
	}

	public Bitmap decodeBitmap(String path, int sampleSize) {
		try {
			Bitmap originalBitmap;
			BitmapFactory.Options options = new BitmapFactory.Options();
			int inSampleSize = (int) Math.pow(2, sampleSize);
			options.inSampleSize = inSampleSize;
			originalBitmap = BitmapFactory.decodeFile(path, options);
			if (originalBitmap == null) {
				Log.e("null bitmap");
				return null;
			}
			return originalBitmap;
		} catch (SecurityException ignored) {
		} catch (OutOfMemoryError e) {
			++sampleSize;
			return decodeBitmap(path, sampleSize);
		}
		return null;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
	
	static public String fileNameForResourceAtURL(String url) {
		String fileName = url;
		if (url.startsWith("http://"))
			fileName = url.substring("http://".length());
		else if (url.startsWith("https://"))
			fileName = url.substring("https://".length());

		fileName = fileName.replaceAll("/", "__");
		fileName = fileName.replaceAll(":", "__");
		return fileName;
	}
	
	public static void saveImageToGallery(Context context, Bitmap bmp) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "Duckr");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
	}

	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com

		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.

		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.

		// If you are using this algorithm in your code please add
		// the following line:
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		sentBitmap.recycle();

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		// Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		// Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}
	    

}
