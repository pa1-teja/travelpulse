package com.trimax.vts.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by pankaj.khedekar on 9/29/2016.
 */
public class CustomViewCircularShape  extends ImageView {
    public static float radius = 100.0f;

    public CustomViewCircularShape(Context context) {
        super(context);
    }

    public CustomViewCircularShape(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewCircularShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//float radius = 36.0f;
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
