package com.leather.skindemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.leather.skindemo.R;
import com.leather.skindemo.skin.ISkinViewSupport;
import com.leather.skindemo.skin.utils.SkinResources;

import java.util.List;

public class BarGraphView extends View implements ISkinViewSupport {
    private float itemWidth = 310;
    private float itemHeight = 310;
    private int totalTime = 100;
    private float barWidth; //默认柱子的宽度
    private float barHeight; //柱子的高度
    private float cellHeight = 1;  //按照list的最大值算出的1点的height
    private float widthSpace; //默认柱子的间距
    private float viewMargin = 0; //整个view的外边距

    private Paint colorBarPaint; //柱状图画笔
    private Paint textPaint; //文字画笔

    private RectF barRectF;
    private RectF textRectF;
    private RectF numRectF;

    private List<BarGraphInfo> mList;

    //共有多少个柱状图
    private int barCount;

    private float startX;
    private float startY;

    //不同分辨率的比例 这个好像没用上
    private float scaleWidth;
    private float scaleHeight;

    private float maxBarHeight;//最大的柱状图高度

    private int currentTime = 0; //当前已绘制的次数
    private Paint paint;
    private Paint ringPaint;

    private int ringColor, textPaintColor, barPaintColor, paintColor;

    public BarGraphView(Context context) {
        this(context, null);
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarGraphView, defStyleAttr, 0);
        ringColor = typedArray.getResourceId(R.styleable.BarGraphView_ringColor, 0);
        textPaintColor = typedArray.getResourceId(R.styleable.BarGraphView_textPaintColor, 0);
        barPaintColor = typedArray.getResourceId(R.styleable.BarGraphView_barPaintColor, 0);
        paintColor = typedArray.getResourceId(R.styleable.BarGraphView_paintColor, 0);
        typedArray.recycle();

        //获取不同屏幕的比例大小  这后来想想取比例没什么意义
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        scaleWidth = width / 1920;
        scaleHeight = height / 1080;
        init();
    }

    private void init() {

        //初始化画笔
        colorBarPaint = new Paint();
        colorBarPaint.setColor(barPaintColor);
        colorBarPaint.setAntiAlias(true);
        //设置矩形
        barRectF = new RectF();
        textRectF = new RectF();
        numRectF = new RectF();

        textPaint = new Paint();
        textPaint.setColor(textPaintColor);
        paint = new Paint();
        paint.setColor(paintColor);
        ringPaint = new Paint();
        ringPaint.setColor(ringColor);
        //根据各个手机分辨率不同算出margin的间距大小
        viewMargin = scaleWidth > scaleHeight ? scaleWidth * viewMargin : scaleHeight * viewMargin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            itemWidth = widthSize - (2 * viewMargin); //减去外边距
            startX = viewMargin / 2; //起始位置定位到默认的
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            itemHeight = heightSize - (2 * viewMargin);
            startY = viewMargin;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setList(mList);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = startX + widthSpace;
        //画矩形
        for (int i = 0; i < barCount; i++) {
            //计算出高度
            barHeight = cellHeight * mList.get(i).num;

            barHeight = barHeight / totalTime * currentTime;
            if (barHeight < barWidth) {
                barHeight = 0;
            }

            float barLeft = left + i * barWidth;
            float top = (startY + maxBarHeight) - barHeight;
            float right = left + (i + 1) * barWidth;
            float bottom = startY + maxBarHeight;

            barRectF.set(barLeft, top, right, bottom);
            canvas.drawRoundRect(barRectF, barWidth / 2, barWidth / 2, colorBarPaint);

            drawRing(canvas, barLeft, i);

            //画subTitle
            float subTitleLeft = left + i * barWidth;
            float subTitleTop = startY + maxBarHeight;
            float subTitleRight = left + (i + 1) * barWidth;
            float subTitleBottom = startY + maxBarHeight - barWidth;
            textRectF.set(subTitleLeft, subTitleTop, subTitleRight, subTitleBottom);
            drawText(canvas, textRectF, mList.get(i).subTitle, textPaintColor);

            //画num
            float numLeft = left + i * barWidth;
            float numTop = (startY + maxBarHeight) - barHeight;
            float numRight = left + (i + 1) * barWidth;
            float numBottom = (startY + maxBarHeight) - barHeight + 2 * barWidth / 3;
            numRectF.set(numLeft, numTop, numRight, numBottom);
            drawText(canvas, numRectF, String.valueOf(mList.get(i).num), textPaintColor);

            left += 2 * widthSpace;
        }

        if (currentTime < totalTime) {
            currentTime++;
            postInvalidate();
        } else {
            currentTime = totalTime;
        }
    }

    //绘制底部圆环
    private void drawRing(Canvas canvas, float barLeft, int i) {
        float roundWidth = 5;
        int center = (int) (barWidth / 2 + barLeft);
        int radius = (int) (center - roundWidth - barLeft);
        //第一步：绘制一个最外层的圆
        ringPaint.setStrokeWidth(roundWidth);
        ringPaint.setStyle(Paint.Style.FILL);
        ringPaint.setAntiAlias(true);
        canvas.drawCircle(center, startY + maxBarHeight - barWidth / 2, center - barLeft, ringPaint);
        //第二步：绘制一个内层的圆
        paint.setStrokeWidth(roundWidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(center, startY + maxBarHeight - barWidth / 2, radius, paint);
    }


    //画文字居中
    private void drawText(Canvas canvas, RectF rectF, String text, int color) {
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(text, rectF.centerX(), baseLineY, textPaint);
    }

    @Override
    public void applySkin() {
        if (ringColor != 0) {
            ringPaint.setColor(SkinResources.getInstance().getColor(ringColor));
        }
        if (textPaintColor != 0) {
            textPaint.setColor(SkinResources.getInstance().getColor(textPaintColor));
        }
        if (barPaintColor != 0) {
            colorBarPaint.setColor(SkinResources.getInstance().getColor(barPaintColor));
        }
        if (paintColor != 0) {
            paint.setColor(SkinResources.getInstance().getColor(paintColor));
        }

        invalidate();
    }


    public static class BarGraphInfo {
        private String subTitle;
        private float num;

        public BarGraphInfo(String subTitle, float num) {
            this.subTitle = subTitle;
            this.num = num;
        }
    }

    /**
     * 设置数据源
     *
     * @param list
     */
    public void setList(List<BarGraphInfo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        mList = list;
        barCount = list.size();//多少个柱状图

        float avgWidth = itemWidth / barCount;
        widthSpace = avgWidth / 12;  //获取间隙
        barWidth = avgWidth - 2 * widthSpace;  //获取状图的宽度

        float maxNum = 0;
        float minNum = list.get(0).num;
        for (BarGraphInfo info : mList) {
            //获取最大值
            if (info.num > maxNum) {
                maxNum = info.num;
            }
            if (info.num < minNum) {
                minNum = info.num;
            }
        }
        //获取柱状图最大显示区域
        maxBarHeight = itemHeight;
        cellHeight = maxBarHeight / maxNum;  //平均每num = 1 时的高度

        postInvalidate();
    }
}
