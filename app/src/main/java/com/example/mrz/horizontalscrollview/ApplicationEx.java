package com.example.mrz.horizontalscrollview;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.zhy_horizontalscrollview.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;

/**
 * ImageLoader 设置图片属性
 */
public class ApplicationEx extends Application {

    private static ApplicationEx sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(300, 400)
                // max width, max height，即保存的每个缓存文件的最大长宽
                //.diskCacheExtraOptions(480, 800, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                //.memoryCacheSize(2 * 1024 * 1024)
                //.diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                .diskCacheFileCount(100)// 缓存的文件数量
                .diskCache(
                        new UnlimitedDiscCache(new File(Environment
                                .getExternalStorageDirectory() +
                                "/myApp/imgCache")))
                // 自定义缓存路径
                .defaultDisplayImageOptions(getDisplayOptions())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                //.writeDebugLogs() // Remove for release app
                .build();// 开始构建
        ImageLoader.getInstance().init(config);
        L.disableLogging();
    }

    private DisplayImageOptions getDisplayOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.background_list) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.imageloader2)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.imageloader3) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置 图片的缩放方式
                //EXACTLY :图像将完全按比例缩小的目标大小
                //EXACTLY_STRETCHED:图片会缩放到目标大小完全
                //IN_SAMPLE_INT:图像将被二次采样的整数倍
                //IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                //NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                //.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(1000))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
        return options;
    }

    public static ApplicationEx getInstance() {
        return sInstance;
    }


}
