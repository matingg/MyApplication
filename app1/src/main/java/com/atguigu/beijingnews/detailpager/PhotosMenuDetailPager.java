package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.base.MenuDetailBasePager;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/6 11:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：图组详情页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {


    public PhotosMenuDetailPager(Context context) {
        super(context);
    }
    private TextView textView;
    @Override
    public View initView() {
        //图组详情页面的视图
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("图组详情页面内容");
    }

}
