package com.example.countdown;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Clepsydra extends View {
    private double ratio;

    public Clepsydra(Context context) {
        super(context);
        setFillRatio(1);
    }

    public Clepsydra(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFillRatio(1);
    }

    public Clepsydra(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillRatio(1);
    }

    // Entre 0.0 et 1.0
    public void setFillRatio(double ratio) {
        this.ratio = ratio;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        Rect rect1 = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(rect1, paint);
        paint.setColor(Color.BLUE);
        Rect rect2 = new Rect(0, getHeight() - (int)(getHeight() * ratio), getWidth(), getHeight());
        Log.d("rectangle", rect2.toString());
        canvas.drawRect(rect2, paint);

    }
}
