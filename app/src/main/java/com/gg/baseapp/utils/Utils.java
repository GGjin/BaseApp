package com.gg.baseapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.gg.baseapp.R;

import java.io.ByteArrayOutputStream;


public class Utils {

    /**
     * 数据库存储    文件存储   Sp      内容提供者
     * <p>
     * <p>
     * false 表示不是第一次运行
     * true 表示第一运行
     */
    public static boolean isFirst(Context context) {
        SharedPreferences sp = context.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        boolean run = sp.getBoolean("run", true);
        if (run) {
            sp.edit().putBoolean("run", false).commit();
        }
        return run;
    }



    public static void showSimpleItemArrDialog(Activity activity, int titleRes, int arrRes,
                                               DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (titleRes > 0)
            builder.setTitle(titleRes);
        builder.setItems(arrRes, listener);
        builder.create().show();
    }

    public static void showConfirmCancelHintDialog(Activity activity, String message,
                                                   DialogInterface.OnClickListener
                                                           confirmListener) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.hint)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bm != null && !bm.isRecycled()) {
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } else
            return null;
    }

    public static void showPictureSelectorDialog(final Activity context, final Uri bitmapPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.picture_option);
        builder.setItems(R.array.get_picture_choice_array, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//				if(which==0){
//					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					context.startActivityForResult(openCameraIntent, DuckrConstant.REQUEST_TAKE_PICTURE);
//				}else if(which==1){
//					Intent intent = new Intent(context,AlbumActivity.class);
//					intent.putExtra(DuckrConstant.pic_number_param, 1);
//					context.startActivity(intent);
//					context.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//				}

                if (which == 0) {
                    if (!FileUtils.checkSDCard()) {
                        ToastUtil.showShort(context, R.string.sdcard_not_connected);
                        return;
                    }
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, bitmapPath);
                    context.startActivityForResult(intent, PrefConstants.REQUEST_TAKE_PICTURE);
                } else if (which == 1) {
                    Intent intent = new Intent(
                            Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    context.startActivityForResult(intent, PrefConstants.REQUEST_GET_ALBUM);
                }

            }
        });
        builder.create().show();
    }

    public static void showPictureSelectorDialog(final Fragment fragment, final Uri bitmapPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle(R.string.picture_option);
        builder.setItems(R.array.get_picture_choice_array, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//				if(which==0){
//					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					context.startActivityForResult(openCameraIntent, DuckrConstant.REQUEST_TAKE_PICTURE);
//				}else if(which==1){
//					Intent intent = new Intent(context,AlbumActivity.class);
//					intent.putExtra(DuckrConstant.pic_number_param, 1);
//					context.startActivity(intent);
//					context.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//				}

                if (which == 0) {
                    if (!FileUtils.checkSDCard()) {
                        ToastUtil.showShort(fragment.getActivity(), R.string.sdcard_not_connected);
                        return;
                    }
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, bitmapPath);
                    fragment.startActivityForResult(intent, PrefConstants.REQUEST_TAKE_PICTURE);
                } else if (which == 1) {
                    Intent intent = new Intent(
                            Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    fragment.startActivityForResult(intent, PrefConstants.REQUEST_GET_ALBUM);
                }

            }
        });
        builder.create().show();
    }


    public static void showConfirmCancelHintDialog(Activity activity, String message,
                                                   DialogInterface.OnClickListener
                                                           confirmListener, DialogInterface
                                                           .OnClickListener cancelListener) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.hint)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, cancelListener)
                .show();
    }

//    public static String toDateStringFromIso(String sdate) {
//        if ("null".equals(sdate) || "NULL".equals(sdate) || "".equals(sdate) || sdate == null) {
//            return "";
//        }
//        Date d1 = null;
//        try {
//            d1 = dateFormaterIsodate.get().parse(sdate);
//            sharecalendar.setTime(d1);
//            sharecalendar.set(Calendar.HOUR_OF_DAY,sharecalendar.get(Calendar.HOUR_OF_DAY)+8);
//            d1=sharecalendar.getTime();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//        return dateFormater4.get().format(d1);
//    }
}
