package com.example.mrz.horizontalscrollview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhy_horizontalscrollview.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.example.mrz.horizontalscrollview.N.n;
import static com.example.mrz.horizontalscrollview.N.sum;
import static com.example.mrz.horizontalscrollview.N.url;

/**
 *
 * 横向Scrollview的adapter
 */
public class HorizontalScrollViewAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDatas;


    public HorizontalScrollViewAdapter(Context context, ArrayList<String> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_index_gallery_item, parent, false);

            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);
            viewHolder.mMark = (ImageView) convertView
                    .findViewById(R.id.imageimage);

            if (n[position]) {
                viewHolder.mMark.setImageResource(R.drawable.yuanquan2);
            } else {
                viewHolder.mMark.setImageResource(R.drawable.yuanquan);
            }
//           点击查看大图
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageBrower(position, mDatas);
                }
            });

//           点击标记选中
            viewHolder.mMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView dd = (ImageView) view.findViewById(R.id.imageimage);
                    if (sum == 3) {
                        if (n[position] == false) {
                            Toast.makeText(mContext, "最多只可以选择三张", Toast.LENGTH_SHORT).show();
                        } else {
                            panduan(dd, position);
                        }
                    } else {
                        panduan(dd, position);
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 加载图片的工具 Imageloader 注意！！！括号中路径必须以  "file://"  开头
        ImageLoader.getInstance().displayImage("file://" + mDatas.get(position), viewHolder.mImg);
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg, mMark;
    }

    //      跳转到单张大图显示 大图activi
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);

    }

    /**
     * 逻辑判断 图片选中后 数组中下角标为position的那一项 为true
     * <p>
     * 图片如果被选中 把图片地址放入数组url中
     * 否则从数组中移除
     * num 记录共有几张图片被选中
     */
    public void panduan(ImageView v, int position) {

        if (!n[position]) {
            v.setImageResource(R.drawable.yuanquan2);
            url.add(sum, mDatas.get(position).toString());
            sum = sum + 1;
        } else {
            v.setImageResource(R.drawable.yuanquan);
            for (int p = 0;p<sum; p++){
                if(url.get(p).equals(mDatas.get(position).toString())){
                    url.remove(p);
                    break;
                }
            }
            sum = sum - 1;
        }
        n[position] = !n[position];
    }

}
