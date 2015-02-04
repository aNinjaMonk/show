package com.example.abhi.show;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by abhi on 27/01/15.
 */
public class SpacerDecorator extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mInsets;
    private int mInsets2;
    private static final int[] ATTRS = {android.R.attr.listDivider};

    public SpacerDecorator(Context mContext){
        TypedArray a = mContext.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();

        mInsets = mContext.getResources().getDimensionPixelOffset(R.dimen.card_insets);
        mInsets2 = mContext.getResources().getDimensionPixelOffset(R.dimen.card_insets2);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c,parent);
    }
    public void drawVertical(Canvas c,RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + mInsets;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mInsets2,mInsets,mInsets2,mInsets);
    }
}
