package lets.heartworld.loadanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 常用旋转线条加载动画
 */

public class RotaryLine extends View {

    private float center;
    private float radius;
    private Paint linePaint;
    private Paint circlePaint;
    private int startAngle = -90;    //旋转起始角度
    private int sweepAngle = 10;     //起始旋转的展示弧度
    private RectF rect;
    private boolean isAnimate = true;     //设置动画是否旋转



    /*
    * 下面的参数可以根据我们的需要进行设置
    * */


    private int margin = 20;    //设置中心旋转线条距离外边距离
    private int lineCoarseness = 6;    //线条的粗度
    private int startSpeed = 6;    //设置动画刚开始的旋转速度，推荐4-10
    private int endSpeed = 8;     //设置动画结束的旋转速度，推荐6-10
    private int angleOfRotation = 1260;   //设置一个周期的动画旋转的角度，推荐720-900-1260三个参数


    public RotaryLine(Context context) {
        super(context);
        linePaint = new Paint();
        circlePaint = new Paint();
    }

    public RotaryLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        circlePaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        center = getMeasuredWidth() / 2;
        radius = getMeasuredWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initData();
        canvas.drawCircle(radius, radius, radius, circlePaint);
        canvas.drawArc(rect, startAngle, sweepAngle, false, linePaint);
        if (startAngle + sweepAngle < angleOfRotation) {
            startAngle += startSpeed;
            sweepAngle += 2;
        } else {
            if (startAngle < angleOfRotation) {
                startAngle += endSpeed;
                sweepAngle -= endSpeed - 2;
            } else {
                startAngle = -90;
                sweepAngle = 10;
            }
        }
        if (isAnimate) {
            invalidate();
        }
    }

    //重置动画
    public void startAnimate() {
        if (!isAnimate) {
            invalidate();
            isAnimate = true;
        }
    }

    public void stopAnimate() {
        isAnimate = false;
    }

    private void initData() {
        linePaint.setColor(Color.argb(255, 180, 180, 180));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineCoarseness);
        circlePaint.setColor(Color.argb(255, 245, 245, 245));
        rect = new RectF(center - radius + margin, center - radius + margin, center + radius - margin, center + radius - margin);
    }

}
