package com.example.mrz.horizontalscrollview;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrz.scrollerproxy.HackyViewPager;
import com.example.mrz.scrollerproxy.ImageDetailFragment;
import com.example.zhy_horizontalscrollview.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.mrz.horizontalscrollview.N.n;
import static com.example.mrz.horizontalscrollview.N.sum;
import static com.example.mrz.horizontalscrollview.N.url;

/**
 * 单张显示照片的activity
 */
public class ImagePagerActivity extends FragmentActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String CHOISE1 = "chio_position111";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private LinearLayout ttll, xzll;
    private TextView mTextView;
    private ImageView mImageView;
    private List<String> urls = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);//上一个activity点击的坐标
        urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);

        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        ttll = (LinearLayout) findViewById(R.id.till);//头部LinearLayout
        xzll = (LinearLayout) findViewById(R.id.quedingxuanze);//底部确定按钮
        mTextView = (TextView) findViewById(R.id.fanhui);
        mImageView = (ImageView) findViewById(R.id.xuanze);
        indicator = (TextView) findViewById(R.id.indicator);//页码标注
         //第一次点击  无滑动
        CharSequence text = getString(R.string.viewpager_indicator, pagerPosition + 1, mPager.getAdapter().getCount());
        indicator.setText(text);

        // 更新下标
        updatemark(savedInstanceState);

        //返回、确定
        tofinish();

        //滑动到和点击的position相对应
        mPager.setCurrentItem(pagerPosition);


        //如果这张被选择显示一下标记
        showpic(pagerPosition);



        //右上角选择按钮点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sum == 3) {
                    if (n[pagerPosition] == false) {
                        Toast.makeText(getApplicationContext(), "最多只可以选择三张", Toast.LENGTH_SHORT).show();
                    } else {
                        duang();
                    }
                } else {
                    duang();
                }
            }
        });


    }

    /**
     * 在大图activity中右上角的选择逻辑处理
     */
    public void duang() {
        if (!n[pagerPosition]) {
            mImageView.setImageResource(R.drawable.yuanquan2);
            url.add(sum, urls.get(pagerPosition).toString());
            sum = sum + 1;
        } else {
            mImageView.setImageResource(R.drawable.yuanquan);
            for (int p = 0; p < sum; p++) {
                if (url.get(p).equals(urls.get(pagerPosition).toString())) {
                    url.remove(p);
                    break;
                }
            }
            sum = sum - 1;
        }
        n[pagerPosition] = !n[pagerPosition];
    }

    /**
     * 点击按钮返回
     */
    public void tofinish() {
        xzll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * viewpager的滑动监听
     *
     * @param savedInstanceState
     */
    public void updatemark(Bundle savedInstanceState) {
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                pagerPosition = arg0;
                //滑动viewpager
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                showpic(arg0);
            }
        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
    }

    /**
     * 滑动到每一个新的页时判断是否被选择
     * <p>
     * 若已经被选择则显示右上角图片
     *
     * @param aaa
     */
    public void showpic(int aaa) {
        if (n[aaa] == true) {
            mImageView.setImageResource(R.drawable.yuanquan2);
        } else {
            mImageView.setImageResource(R.drawable.yuanquan);
        }
    }

    /**
     * 鸿洋大神写的  说实话 只知道他是切换viewpager的
     * <p>
     * 具体哪行代码我已经在下面标注了
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);//滑动切换viewpager中的fragment
        }
    }

}
