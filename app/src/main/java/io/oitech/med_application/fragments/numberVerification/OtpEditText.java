package io.oitech.med_application.fragments.numberVerification;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import androidx.appcompat.widget.AppCompatEditText;

import io.oitech.med_application.R;

public class OtpEditText extends AppCompatEditText {
    private float boxSize = 56; // in dp (for width and height)
    private Paint mBorderPaint  ;
    private float mSpace = 10; //24 dp by default, space between the lines
    private float mNumChars = 6;
    private float mLineSpacing = 8; //8dp by default, height of the text from our lines
    private int mMaxLength = 6;
    private float mLineStroke = 2;
    private Paint mLinesPaint;
    private OnClickListener mClickListener;


    public OtpEditText(Context context) {
        super(context);
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OtpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        mLineStroke = multi * mLineStroke;

        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStrokeWidth(mLineStroke);
        mLinesPaint.setColor(getResources().getColor(R.color.black)); // (maybe will be removed)

        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(1 * multi); // 1dp border
        mBorderPaint.setColor(getResources().getColor(R.color.blue)); // your blue color
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);

        setBackgroundResource(0);
        mSpace = multi * mSpace;
        mLineSpacing = multi * mLineSpacing;
        mNumChars = mMaxLength;

        boxSize = boxSize * context.getResources().getDisplayMetrics().density;
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();
        int top = getPaddingTop();

        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        float cornerRadius = 16 * getResources().getDisplayMetrics().density; // 20dp to px

        for (int i = 0; i < mNumChars; i++) {
            float left = startX;
            float right = startX + boxSize;
            float rectBottom = bottom;
            float rectTop = bottom - boxSize; // height same as width = boxSize

            // draw rounded box
            canvas.drawRoundRect(left, rectTop, right, rectBottom, cornerRadius, cornerRadius, mBorderPaint);

            // draw character inside box
            if (text.length() > i) {
                float middleX = left + boxSize / 2;
                float middleY = (rectTop + rectBottom) / 2 + getPaint().getTextSize() / 3;
                canvas.drawText(text, i, i + 1, middleX - textWidths[i] / 2, middleY, getPaint());
            }

            // Move to next box
            if (mSpace < 0) {
                startX += boxSize * 2;
            } else {
                startX += boxSize + mSpace;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth;

        if (mSpace < 0) {
            // If no space, double box size
            desiredWidth = (int)((boxSize * 2) * mNumChars);
        } else {
            desiredWidth = (int)(boxSize * mNumChars + mSpace * (mNumChars - 1));
        }

        int width = resolveSize(desiredWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);
        int height = resolveSize((int) (boxSize + getPaddingTop() + getPaddingBottom()), heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

}