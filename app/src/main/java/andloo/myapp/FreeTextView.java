package andloo.myapp;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lwl on 2016/10/17.
 * 根据text内容长度自动改变字体大小的TextView
 */

public class FreeTextView extends TextView {
    private static float DEFAULT_MIN_TEXT_SIZE = 10;
    private static float DEFAULT_MAX_TEXT_SIZE = 50;

    private TextPaint testPaint;
    private float minTextSize;
    private float maxTextSize;
    
    public FreeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    //初始化
    private void initialize() {
        testPaint = new TextPaint();
        testPaint.set(this.getPaint());
        // 定义的最大字形小于默认最小字形的话，设置为默认最大字形
        maxTextSize = this.getTextSize();
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0&&textHeight>0) {
            //允许显示
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int availableHeight = textHeight - this.getPaddingBottom() - this.getPaddingTop();
            //根据行计算
            int autoWidth = availableWidth;
            float mult=getLineSpacingMultiplier();
            float add=getLineSpacingExtra();

            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            int oldline=1,newline=1;
            while ((trySize > minTextSize)) {
                //计算字体单行宽
                int displayW = (int) testPaint.measureText(text);
                //计算字体单行高
                int displaH=round(testPaint.getFontMetricsInt(null)*mult+add);
                if (displayW < autoWidth) {
                    break;
                }
                //计算最大行
                newline = availableHeight / displaH;
                //if line change ,calculate new autoWidth
                if (newline > oldline) {
                    oldline=newline;
                    autoWidth = availableWidth * newline;
                    continue;
                }
                //try more small TextSize
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }

                testPaint.setTextSize(trySize);
            }
            //setMultiLine
            if (newline>=2)
            {
                this.setSingleLine(false);
                this.setMaxLines(newline);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (w!=oldw || h!=oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }

    public static int round(float value) {
        long lx = (long) (value * (65536 * 256f));
        return (int) ((lx + 0x800000) >> 24);
    }
}
