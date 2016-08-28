package com.gianlu.aria2app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class LetterIconBig extends View {
    private Context context;
    private String letters;
    private String addr;
    private String port;
    private Rect lettersBounds = new Rect();
    private Rect textBounds = new Rect();
    private Rect portBounds = new Rect();
    private Paint shapePaint;
    private Paint letterPaint;
    private Paint textPaint;

    public LetterIconBig(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LetterIconBig setProfileAddress(String address) {
        addr = address;

        return this;
    }

    public LetterIconBig setProfilePort(int port) {
        this.port = String.valueOf(port);
        return this;
    }

    public LetterIconBig setProfileName(String name) {
        if (name.length() <= 2) letters = name;
        letters = name.substring(0, 2);

        return this;
    }

    public LetterIconBig setBigTextColor(@ColorRes int colorRes) {
        letterPaint = new Paint();
        letterPaint.setColor(ContextCompat.getColor(context, colorRes));
        letterPaint.setAntiAlias(true);
        letterPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
        letterPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 26, context.getResources().getDisplayMetrics()));

        return this;
    }

    public LetterIconBig setSmallTextColor(@ColorRes int colorRes) {
        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, colorRes));
        textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics()));

        return this;
    }

    public LetterIconBig setShapeColor(@ColorRes int colorRes, @ColorRes int shadowColor) {
        shapePaint = new Paint();
        shapePaint.setAntiAlias(true);
        shapePaint.setColor(ContextCompat.getColor(context, colorRes));
        shapePaint.setShadowLayer(4, 0, 4, ContextCompat.getColor(context, shadowColor));
        setLayerType(LAYER_TYPE_SOFTWARE, shapePaint);

        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;

        int radius;
        if (viewWidthHalf > viewHeightHalf)
            radius = viewHeightHalf - 4;
        else
            radius = viewWidthHalf - 4;

        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, shapePaint);

        letterPaint.getTextBounds(letters, 0, letters.length(), lettersBounds);
        boolean isTextBoundOK = false;
        int textSize = 16;
        while (!isTextBoundOK) {
            if (textSize <= 8) {
                addr = addr.substring(0, 13);
                addr += "...";
            }
            textPaint.getTextBounds(addr, 0, addr.length(), textBounds);

            if (textBounds.width() <= getMeasuredWidth() - 24) {
                isTextBoundOK = true;
            } else {
                textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize--, context.getResources().getDisplayMetrics()));
            }
        }
        textPaint.getTextBounds(port, 0, port.length(), portBounds);

        canvas.drawText(letters, viewWidthHalf - lettersBounds.exactCenterX(), viewHeightHalf - lettersBounds.exactCenterY() - textBounds.height() - 2, letterPaint);
        canvas.drawText(addr, viewWidthHalf - textBounds.exactCenterX(), viewHeightHalf - lettersBounds.exactCenterY() + 8, textPaint);
        canvas.drawText(port, viewWidthHalf - portBounds.exactCenterX(), viewHeightHalf + lettersBounds.height() + 12, textPaint);
    }

    public void build() {
        invalidate();
    }
}