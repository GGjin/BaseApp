package com.gg.baseapp.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.gg.baseapp.utils.wxpay.MD5Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 */

public class FileUtils {

    public static final String ROOT_DIR = "1601";
    public static final String SYS_DIR_CACHE = "cache";
    private static final String JSON_CACHE = "json";

    public static File getRootFile(Context context) {
        String externalCacheDir;
        //判断外置sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //当前手机运行的版本号
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                //sd卡/Android/data/包名/cache
                externalCacheDir = context.getExternalCacheDir().getAbsolutePath();
            } else {
                //sd卡/1601
                externalCacheDir = Environment.getExternalStorageDirectory() + ROOT_DIR;
            }

        } else {
            //表示系统目录
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                //sys/data/data/包名/cache
                externalCacheDir = context.getCacheDir().getAbsolutePath();
            } else {
                //sys/data/data/包名/cache
                externalCacheDir = Environment.getDataDirectory() + File.separator + context.getPackageName() + File.separator + SYS_DIR_CACHE;
            }

        }
        File file = new File(externalCacheDir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;

    }

    public static File getJsonCache(Context context) {

        String jsonPath = getRootFile(context) + JSON_CACHE;
        File jsonFile = new File(jsonPath);

        if (!jsonFile.exists()) {
            jsonFile.mkdirs();
        }
        return jsonFile;

    }


    public static boolean isFileExists(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists())
                return true;
        }
        return false;
    }

    static public String filePathForStorage(Context context) {
        String path = context.getFilesDir() + "/";
        return path;
    }

    static public String filePathForStorageSDCard() {
        String path = null;

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = Environment.getExternalStorageDirectory().getPath()
                    + "/Duckr/";
            try {
                String nomediaPath = path + ".nomedia";
                File file = new File(nomediaPath);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                Log.e(e.getMessage());
            }
        }
        return path;
    }

    public static String getCacheDir() {
        String path = filePathForStorageSDCard();
        path += "cache/";
        createNomediaFile(path);
        return path;
    }

    public static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir(file.getPath());
        }
        dir.delete();
    }

    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists())
            file.delete();
    }

    static public long getFileSize(String fileName) {
        long size = 0;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            size = buffer.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    static public String readFileData(String fileName) {
        StringBuilder res = new StringBuilder();
        try {
            FileInputStream fin = new FileInputStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String tem;
            while (true) {
                tem = reader.readLine();
                if (tem == null) {
                    break;
                }
                res.append(tem);
            }
            reader.close();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public static void writeDataToFile(String filePath, byte[] data)
            throws IOException {
        if (data.length > 0) {
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            file.createNewFile(); // need add permission to manifest

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        }
    }

    public static boolean checkSDCard() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String getImageFileDir() {
        String path = filePathForStorageSDCard();
        path += "image/";
        File file = new File(path);
        file.mkdirs();
        try {
            String oldPath = filePathForStorageSDCard() + "img/";
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                createNomediaFile(oldPath);
            }
        } catch (Exception e) {
        }
        return path;
    }

    public static String getImageFilePath() {
        String path = getImageFileDir();
        if (isFolderExists(path)) {
            path += "/" + System.currentTimeMillis() + ".jpg";
        }
        return path;
    }

    public static Uri getImageFileUriForTakePhoto() {
        String path = getImageFilePath();
        File f = new File(path);
        Uri uri = Uri.fromFile(f);
        return uri;
    }

    public static String getVideoFileDir() {
        String path = filePathForStorageSDCard();
        path += "video/";
        File file = new File(path);
        file.mkdirs();
        createNomediaFile(path);
        return path;
    }

    public static String getVideoCacheDir() {
        String path = getCacheDir();
        path += "video/";
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    public static String getVideoCacheFilePath(String url) {
        String path = getVideoCacheDir();
        path += MD5Util.MD5Encode(url, null);
        return path;
    }

    public static Uri getImageFilePathWithFile() {
        String path = getImageFilePath();
        File f = new File(path);
        Uri uri = Uri.fromFile(f);
        return uri;
    }

    static public boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean copy(File from, File to) {
        try {
            File temFile = new File(to.getPath() + ".tmp");
            temFile.createNewFile();
            FileInputStream fileInputStream = new FileInputStream(from);
            FileOutputStream fileOutputStream = new FileOutputStream(temFile);
            int count;
            byte[] buffer = new byte[1024];
            while ((count = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            fileInputStream.close();
            temFile.renameTo(to);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void createNomediaFile(String dirPath) {
        try {
            String nomediaPath = dirPath + ".nomedia";
            File file = new File(nomediaPath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            Log.e(e.getMessage());
        }
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDcardOK() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡跟路径。SD卡不可用时，返回null
     */
    public static String getSDcardRoot() {
        if (isSDcardOK()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        return null;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    /**获取字符串中某个字符串出现的次数。*/
    public static int countMatches(String res, String findString) {
        if (res == null) {
            return 0;
        }

        if (findString == null || findString.length() == 0) {
            throw new IllegalArgumentException("The param findString cannot be null or 0 length.");
        }

        return (res.length() - res.replace(findString, "").length()) / findString.length();
    }

    /**判断该文件是否是一个图片。*/
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

}
