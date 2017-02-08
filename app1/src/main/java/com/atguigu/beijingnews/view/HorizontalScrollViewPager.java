package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 麻少亭 on 2017/2/8.
 */

public class HorizontalScrollViewPager extends ViewPager {
    private float startX;
    private float startY;
    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE :
                float endX = ev.getX();
                float endY = ev.getY();
                float xX = Math.abs(endX - startX);
                float yY = Math.abs(endY - startY);
                //                    如果x > y 表示水平移动
                if (xX > yY){
//如果当前item == 0 并且  startX - endX < 0)
                    if (getCurrentItem() == 0 &&  endX - startX > 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if(getCurrentItem()== getAdapter().getCount()-1 && startX - endX > 0 ){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }else{
//                    否则表示 竖直移动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }



                break;
        }


        return super.dispatchTouchEvent(ev);
    }
}
