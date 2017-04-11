package com.twisstosin.popularmovies1.Views;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView  extends android.support.v7.widget.AppCompatImageView {

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

      super.onMeasure(widthMeasureSpec, widthMeasureSpec);

  }

  public SquareImageView(Context context) {
    super(context);
  }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}