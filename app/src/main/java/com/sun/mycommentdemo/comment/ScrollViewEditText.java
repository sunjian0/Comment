package com.sun.mycommentdemo.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class ScrollViewEditText extends EditText {
    //滑动距离的最大边界
    private int mOffsetHeight;

    //是否到顶或者到底的标志
    private boolean mBottomFlag = false;

    public ScrollViewEditText(Context context) {
        super(context);
        init();
    }

    public ScrollViewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollViewEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int paddingTop;
        int paddingBottom;
        int mHeight;
        int mLayoutHeight;

        //获得内容面板
        Layout mLayout = getLayout();
        //获得内容面板的高度
        mLayoutHeight = mLayout.getHeight();
        //获取上内边距
        paddingTop = getTotalPaddingTop();
        //获取下内边距
        paddingBottom = getTotalPaddingBottom();

        //获得控件的实际高度
        mHeight = getHeight();

        //计算滑动距离的边界
        mOffsetHeight = mLayoutHeight + paddingTop + paddingBottom - mHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            //如果是新的按下事件，则对mBottomFlag重新初始化
            mBottomFlag = false;
        //如果已经不要这次事件，则传出取消的信号，这里的作用不大
        if (mBottomFlag)
            event.setAction(MotionEvent.ACTION_CANCEL);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        //如果是需要拦截，则再拦截，这个方法会在onScrollChanged方法之后再调用一次
        if (!mBottomFlag)
            getParent().requestDisallowInterceptTouchEvent(true);
        return result;
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (vert == mOffsetHeight || vert == 0) {
            //这里触发父布局或祖父布局的滑动事件
            getParent().requestDisallowInterceptTouchEvent(false);
            mBottomFlag = true;
        }
    }


}