package com.example.mrz.scrollerproxy;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mrz.horizontalscrollview.ImagePagerActivity;
import com.example.zhy_horizontalscrollview.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment
 */

public class ImageDetailFragment extends Fragment {

    private String mImageUrl;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private View view;
    private LinearLayout t, u;
    private String mt;
    private static ImagePagerActivity mImagePagerActivity;
    private ImageView mImageView,toprightimage;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
//        mImagePagerActivity = yt;
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image_detail_fragment, null);
        mImageView = (ImageView) view.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);
//        t = (LinearLayout) getActivity().findViewById(R.id.till);
//        u = (LinearLayout) getActivity().findViewById(R.id.quedingxuanze);
//        toprightimage = (ImageView) getActivity().findViewById(R.id.xuanze);

        progressBar = (ProgressBar) view.findViewById(R.id.loading);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ImageLoader.getInstance().displayImage("file://" + mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });




    }
}

