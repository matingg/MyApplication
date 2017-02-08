package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.TabDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/2/6 15:26
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.listview)
    ListView listview;
    private String url;
    private TabDetailPagerAdapter adapter;
    ViewPager viewPager;
    private int  prePoint ;
    /**
     * 列表数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsEntity> news;
    private TextView tvTitle;
    private LinearLayout llGroupPoint;
    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topNews;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        View view = View.inflate(mContext, R.layout.tab_detail_pager , null);
        ButterKnife.inject(this,view);
//        Button button = new Button(mContext);
//        button.setText("头部按钮");
        View headerview = View.inflate(mContext,R.layout.header_view,null);
        viewPager = (ViewPager) headerview.findViewById(R.id.viewpager);
        tvTitle = (TextView) headerview.findViewById(R.id.tv_title);
        llGroupPoint = (LinearLayout) headerview.findViewById(R.id.ll_group_point);
        listview.addHeaderView(headerview);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        Log.e("TAG","TabDetailPager--url=="+url);
        //设置数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","请求数据成功==TabDetailPager=="+childrenBean.getTitle());
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","请求数据失败==TabDetailPager=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean pagerBean = new Gson().fromJson(json,TabDetailPagerBean.class);
        news =pagerBean.getData().getNews();


        //设置适配器
        adapter = new TabDetailPagerAdapter(mContext,news);
        listview.setAdapter(adapter);

//        设置顶部ViewPager适配器
        topNews = pagerBean.getData().getTopnews();
        viewPager.setAdapter(new MyPagerAdapter());
//   设置viewPager的监听 当页面滑动的时候改变标题
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
//        进来时默认选中
        tvTitle.setText(topNews.get(0).getTitle());
        //        添加红点
        llGroupPoint.removeAllViews();
        for (int i = 0 ; i < topNews.size();i++){
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2 ,ViewGroup.LayoutParams.WRAP_CONTENT);
            point.setLayoutParams(params);
            if (i != 0){
                params.leftMargin = DensityUtil.dip2px(mContext,8);
//                设置图片是否高亮
                point.setEnabled(false);
            }else{
                //                设置图片是否高亮
                point.setEnabled(true);
            }
            point.setLayoutParams(params);
            //设置图片背景选择器
            point.setBackgroundResource(R.drawable.point_selector);
//把点添加到线性布局中
            llGroupPoint.addView(point);
        }

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            llGroupPoint.getChildAt(prePoint).setEnabled(false);
            llGroupPoint.getChildAt(position).setEnabled(true);
            prePoint =position;
        }

        @Override
        public void onPageSelected(int position) {
            tvTitle.setText(topNews.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
//            设置图片可拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片
            Glide.with(mContext).load(Constants.BASE_URL+topNews.get(position).getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                默认图片
                    .placeholder(R.drawable.news_pic_default)
//                出错图片
                    .error(R.drawable.news_pic_default)
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
