package lets.heartworld.loadanimation;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static lets.heartworld.loadanimation.TransformationLoading.ShapeKind.CIRCLE;
import static lets.heartworld.loadanimation.TransformationLoading.ShapeKind.RECT;
import static lets.heartworld.loadanimation.TransformationLoading.ShapeKind.TRIANGLE;

/**
 * 不断变换图形的加载动画，仿58，参考链接：https://github.com/zzz40500/android-shapeLoadingView
 * 总的来说，颜色变换的速度收两个值控制，一个是：colorChangeRate，一个是time，一个颜色变换率，一个是切换颜色的速度
 */

public class TransformationLoading extends View {

    public static ShapeKind shapeKind = RECT;
    private float currentColorChange = 0f;   //当前颜色切换总值
    private ArgbEvaluator mAnimPercent = new ArgbEvaluator();   //用来转化两种颜色的系统工具，详情自查
    private Paint paint;
    private float controlWidth;   //控件宽度
    private Handler handler;



    /*
    * 下面的参数可以根据实际需要进行更改
    * */


    private int mRectColor;       //方形的颜色
    private int mTriangleColor;    //三角形的颜色
    private int mCircle;            //圆形的颜色
    private float colorChangeRate = 0.02f;   //集中图形切换时颜色的变化速度，推荐0.01-0.5,数值越小颜色变化越不突兀
    private long time = 10;    //动画的执行速度，推荐10 - 100,数值越大，动画越慢
    private boolean changeKind = false;    //是否开启主动切换形状的控制器
    private boolean FlashingOrNot = false;     //是否开启颜色闪动
    private boolean animateOrNot = true;    //是否执行不断刷新

    public TransformationLoading(Context context) {
        super(context);
        initData();
    }

    public TransformationLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    //初始化几个图形的颜色
    private void initData() {
        mRectColor = Color.argb(255, 114, 213, 114);
        mTriangleColor = Color.argb(255, 115, 153, 254);
        mCircle = Color.argb(255, 232, 78, 64);
        paint = new Paint();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 3:
                        invalidate();     //重绘界面
                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        controlWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (shapeKind) {
            case RECT:        //绘制一个正方形
                currentColorChange += colorChangeRate;
                if (FlashingOrNot){
                    int rectColor = (int) mAnimPercent.evaluate(currentColorChange, mRectColor, mTriangleColor);
                    paint.setColor(rectColor);
                }else {
                    paint.setColor(mRectColor);
                }
                if (currentColorChange > 1) {
                    if (changeKind) {
                        shapeKind = TRIANGLE;
                    }
                    currentColorChange = 0f;
                }
                Path rectPath = new Path();
                rectPath.moveTo(0, 0);
                rectPath.lineTo(controlWidth, 0);
                rectPath.lineTo(controlWidth, controlWidth);
                rectPath.lineTo(0, controlWidth);
                rectPath.close();
                canvas.drawPath(rectPath, paint);
                if (animateOrNot) {
                    toRefresh();
                }
                break;
            case TRIANGLE:      //绘制一个三角形
                currentColorChange += colorChangeRate;
                if (FlashingOrNot){
                    int triangleColor = (int) mAnimPercent.evaluate(currentColorChange, mTriangleColor, mCircle);
                    paint.setColor(triangleColor);
                }else {
                    paint.setColor(mTriangleColor);
                }
                if (currentColorChange > 1) {
                    if (changeKind) {
                        shapeKind = CIRCLE;
                    }
                    currentColorChange = 0f;
                }
                Path trianglePath = new Path();
                trianglePath.moveTo(0.5f * controlWidth, 0);
                trianglePath.lineTo(controlWidth, controlWidth);
                trianglePath.lineTo(0, controlWidth);
                trianglePath.close();
                canvas.drawPath(trianglePath, paint);
                if (animateOrNot) {
                    toRefresh();
                }
                break;
            case CIRCLE:         //绘制一个圆形
                currentColorChange += colorChangeRate;
                if (FlashingOrNot){
                    int circleColor = (int) mAnimPercent.evaluate(currentColorChange, mCircle, mRectColor);
                    paint.setColor(circleColor);
                }else {
                    paint.setColor(mCircle);
                }
                if (currentColorChange > 1) {
                    if (changeKind) {
                        shapeKind = RECT;
                    }
                    currentColorChange = 0f;
                }
                canvas.drawCircle(controlWidth / 2, controlWidth / 2, controlWidth / 2, paint);
                if (animateOrNot) {
                    toRefresh();
                }
                break;
        }
    }

    //用来供主动切换形状的方法
    public void changeShape() {
        currentColorChange = 0f;     //重置颜色渐变数值
        changeKind = false;           //关闭自动切换图形的功能
        switch (shapeKind) {
            case RECT:
                shapeKind = TRIANGLE;
                break;
            case TRIANGLE:
                shapeKind = CIRCLE;
                break;
            case CIRCLE:
                shapeKind = RECT;
                break;
        }
    }

    //重新刷新界面
    private void toRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        }, time);
    }

    //变形的集中图形列表，分别是方形，三角形，圆形
    public enum ShapeKind {
        RECT, TRIANGLE, CIRCLE
    }

    //停止动画
    public void stopAnimate() {
        animateOrNot = false;
    }


    //开始动画
    public void startAnimate() {
        if (!animateOrNot) {
            animateOrNot = true;
            toRefresh();
        }
    }

}
