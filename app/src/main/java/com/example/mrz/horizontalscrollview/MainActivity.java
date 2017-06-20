package com.example.mrz.horizontalscrollview;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.zhy_horizontalscrollview.R;

import java.util.ArrayList;

import static com.example.mrz.horizontalscrollview.N.n;
import static com.example.mrz.horizontalscrollview.N.url;

/**
 * 主界面
 */
public class MainActivity extends Activity {

    private ArrayList<String> vecFile = new ArrayList<String>();
    private MyHorizontalScrollView mHorizontalScrollView;
    private HorizontalScrollViewAdapter mAdapter;
    private Button mButton;
    private String ppp = "";
    private int po = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.argb(50, 0, 0, 0));
        setContentView(R.layout.activity_main);
/**
 *     MyHorizontalScrollView 继承 HorizontalScrollView
 *     鸿洋大神写的
 *     里面包括 图片的滑动加载，只加载显示的页
 */
        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);


        mButton = (Button) findViewById(R.id.choisebutton);


        vecFile = getImgPathList();//得到所有图片
/**
 * 初始化时 设置数组 n 所有值为false  代表全部未选择
 *
 * 当点击选择后 下角标为position 的元素 变为true 代表被选择
  */
        boolean [] m = new boolean[vecFile.size()];
        for (int i = 0; i < vecFile.size(); i++) {
            m[i] = false;
        }
        n = m;


        // 点击确定选择图片
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (po%2 ==0){
                    mHorizontalScrollView.setVisibility(View.VISIBLE);
                }else{
                    url = mAdapter.getUrl();
                    for (int i = 0;i<url.size();i++){
                        int j = i+1;
                        ppp = "第"+j+"个图片"+">>>>>>>>>>>>>>>>>>>>>"+url.get(i).toString()+ppp+"\n";
                    }
                    Log.i("qwqwqw",ppp);
                    ppp = "";
                    mHorizontalScrollView.setVisibility(View.GONE);
                }
                po = po+1;
            }
        });
    }

    /**
     * 获取内存中所有图片地址
     *
     * @return
     */
    private ArrayList<String> getImgPathList() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "_data"}, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
        return list;
    }

    /**
     * 写在onResume里是为了大图activity结束时
     * 刷新一下adapter
     * adapter自带的刷新方法不好使，，不懂什么情况
     */
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = null;
        mAdapter = new HorizontalScrollViewAdapter(this,vecFile);//设置adapter
        mHorizontalScrollView.initDatas(mAdapter);
    }

}
