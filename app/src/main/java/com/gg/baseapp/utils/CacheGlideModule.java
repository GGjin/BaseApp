package com.gg.baseapp.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.gg.baseapp.R;


/**
 *
 *
 */

public class CacheGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.image_tag);

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int memoryCacheSize = calculator.getMemoryCacheSize();

        DiskLruCacheFactory factory = new ExternalCacheDiskCacheFactory(context, "image_cache", DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
        builder.setDiskCache(factory);


        LruResourceCache cache = new LruResourceCache((int) (1.2 * memoryCacheSize));
        builder.setMemoryCache(cache);

//
//        ViewTarget.setTagId(R.id.image_tag);
//
//        // 在 Android 中有两个主要的方法对图片进行解码：ARGB8888 和 RGB565。前者为每个像素使用了 4 个字节，
//        // 后者仅为每个像素使用了 2 个字节。ARGB8888 的优势是图像质量更高以及能存储一个 alpha 通道。
//        // Picasso 使用 ARGB8888，Glide 默认使用低质量的 RGB565。
//        // 对于 Glide 使用者来说：你使用 Glide module 方法去改变解码规则。
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        //设置缓存目录
//        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
//        int diskCacheSize = 1024 * 1024 * 200;//最多可以缓存多少字节的数据
//        //设置磁盘缓存大小
//        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "Duckr", diskCacheSize));
//
//
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "Duckr", diskCacheSize));
////        File cacheDir = PathUtils.getDiskCacheDir(context, CacheConfig.IMG_DIR);
////        cache = DiskLruCacheWrapper.get(cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);// 250 MB
////        builder.setDiskCache(new DiskCache.Factory() {
////            @Override
////            public DiskCache build() {
////                return cache;
////            }
////        });
//
//
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
//        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
//        //设置内存缓存大小
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
//        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
//        //设置memory和Bitmap池的大小
////        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
////        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
////        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
////
////        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
////        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
////
////        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
