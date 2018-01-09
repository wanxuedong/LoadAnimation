package lets.heartworld.loadanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 万学冬 on 2018/1/3.
 */

public class GradientRotation extends View {

    private int center;
    private Paint linePaint;
    private Handler handler;
    private int rotation = 1;            //用来计算旋转是否有一周的标记
    private boolean isAnimate = true;    //设置动画是否可以旋转



    /*
     *   下面的数值我们可以根据自己的需要进行调节
     */


    private Paint circlePaint;      //用来设置中间小球的颜色等属性，不同项目中都要更改
    private int deepenedDegree = 8;     //颜色渐变的程度，从1到255，推荐6-15
    private int numberOfLines = 12;    //旋转线条的个数，推荐12-15
    private int lineCoarseness = 5;    //线条的粗度，推荐4-6
    private int time = 20;    //动画旋转的速度，数值越大，速度越慢

    public GradientRotation(Context context) {
        super(context);
        initData();
    }

    public GradientRotation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        linePaint = new Paint();
        circlePaint = new Paint();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 2:
                        invalidate();     //重绘界面
                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        center = getMeasuredWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint.setStrokeWidth(lineCoarseness);          //设置线条的粗度
        circlePaint.setColor(Color.argb(255, 255, 255, 255));         //设置中心圆的颜色,我们用的时候需要根据我们的背景进行设置
        canvas.translate(center, center);
        canvas.rotate((360 / numberOfLines) * rotation);     //每次重绘前都要比上一次多(360 / numberOfLines)度，这样才能有旋转的感觉
        for (int i = 0; i < numberOfLines; i++) {
            linePaint.setColor(Color.argb(255, 255 - deepenedDegree * i, 255 - deepenedDegree * i, 255 - deepenedDegree * i));    //一共会绘制12个线，每个线的颜色逐渐加深
            canvas.drawLine(0, 0, center - 10, 0, linePaint);
            canvas.rotate(360 / numberOfLines);
        }
        canvas.drawCircle(0, 0, 20, circlePaint);
        if (rotation == numberOfLines) {
            rotation = 1;
        }
        rotation++;

        //这里用了定时是为了避免界面刷新过快，不好看
        if (isAnimate) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }, time);
        }
    }

    //停止动画
    public void stopAnimate() {
        isAnimate = false;
    }

    //停止动画
    public void startAnimate() {
        if (!isAnimate) {      //添加这个判断是为了避免重复点击开始动画导致产生多个动画的情况
            isAnimate = true;
            invalidate();
        }
    }

}
