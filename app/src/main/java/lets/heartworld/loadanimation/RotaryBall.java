package lets.heartworld.loadanimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 仿视频加载动画，旋转的蓝色小球
 */

public class RotaryBall extends View {

    private Path rotationPath;
    private float radius;
    private Paint circlePaintOne;
    private PathMeasure pathMeasure;
    private int finishAnimateOne = 0;   // 用来判断当前动画有没有开始
    private int finishAnimateTwo = 0;   // 用来判断当前动画有没有开始
    private int finishAnimateThree = 0;   // 用来判断当前动画有没有开始
    private int finishAnimateFour = 0;   // 用来判断当前动画有没有开始
    private int finishAnimateFive = 0;   // 用来判断当前动画有没有开始
    private Handler handler;
    private float[] mCurrentPositionOne = new float[2];
    private float[] mCurrentPositionTwo = new float[2];
    private float[] mCurrentPositionThree = new float[2];
    private float[] mCurrentPositionFour = new float[2];
    private float[] mCurrentPositionFive = new float[2];
    private ValueAnimator valueAnimatorOne = null;
    private ValueAnimator valueAnimatorTwo = null;
    private ValueAnimator valueAnimatorThree = null;
    private ValueAnimator valueAnimatorFour = null;
    private ValueAnimator valueAnimatorFive = null;
    private int currentStatus = -1;    //-1表示第一次运行，0表示动画结束或者没开始，1表示正在运动中
    private boolean animateOrNot = true;    //用来决定是否开启动画

    public RotaryBall(Context context) {
        super(context);
        initData();
    }


    public RotaryBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        rotationPath = new Path();
        circlePaintOne = new Paint();
        circlePaintOne.setColor(Color.BLUE);
        circlePaintOne.setAntiAlias(true);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 4:
                        if (finishAnimateOne == 0) {
                            startAnimatorOne();
                        }
                        if (finishAnimateTwo == 0) {
                            startAnimatorTwo();
                        }
                        if (finishAnimateThree == 0) {
                            startAnimatorThree();
                        }
                        if (finishAnimateFour == 0) {
                            startAnimatorFour();
                        }
                        if (finishAnimateFive == 0) {
                            startAnimatorFive();
                        }
                        currentStatus = 0;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = getMeasuredWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        rotationPath.addCircle(radius, radius, radius - 10, CW);
        rotationPath.moveTo(radius, 0 + 10);
        rotationPath.cubicTo(radius, 0 + 10, radius * 2 - 10, 0 + 10, radius * 2 - 10, radius);
        rotationPath.cubicTo(radius * 2 - 10, radius, radius * 2 - 10, radius * 2 - 10, radius, radius * 2 - 10);
        rotationPath.cubicTo(radius, radius * 2 - 10, 0 + 10, radius * 2 - 10, 0 + 10, radius);
        rotationPath.cubicTo(0 + 10, radius, 0 + 10, 0 + 10, radius, 0 + 10);
        rotationPath.close();
        pathMeasure = new PathMeasure(rotationPath, false);
        //下面绘制不同半径的小圆
        canvas.drawCircle(mCurrentPositionOne[0], mCurrentPositionOne[1], 10, circlePaintOne);
        canvas.drawCircle(mCurrentPositionTwo[0], mCurrentPositionTwo[1], 9, circlePaintOne);
        canvas.drawCircle(mCurrentPositionThree[0], mCurrentPositionThree[1], 7, circlePaintOne);
        canvas.drawCircle(mCurrentPositionFour[0], mCurrentPositionFour[1], 5, circlePaintOne);
        canvas.drawCircle(mCurrentPositionFive[0], mCurrentPositionFive[1], 3, circlePaintOne);
        if (currentStatus == -1) {
            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);
        }
        if (animateOrNot) {
            if (currentStatus == 0) {
                currentStatus = 1;
                new Thread() {            //用线程来统一五个圆的周期
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.d("thread", "thread");
                            Thread.sleep(1600);
                            Message message = new Message();
                            message.what = 4;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

    //供外部调用，开始动画
    public void startAnimate() {
        if (!animateOrNot) {
            animateOrNot = true;
            currentStatus = -1;
            invalidate();
        }
    }

    //供外部调用，停止动画
    public void stopAnimate() {
        if (animateOrNot) {
            animateOrNot = false;
        }
    }

    //界面被销毁
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimate();
        clearAllAnimation();
    }

    //清除所有动画效果
    private void clearAllAnimation() {
        if (valueAnimatorOne != null){
            if (valueAnimatorOne.isRunning()){
                valueAnimatorOne.cancel();
            }
            valueAnimatorOne.removeAllUpdateListeners();
            valueAnimatorOne = null;
        }
        if (valueAnimatorTwo != null){
            if (valueAnimatorTwo.isRunning()){
                valueAnimatorTwo.cancel();
            }
            valueAnimatorTwo.removeAllUpdateListeners();
            valueAnimatorTwo = null;
        }
        if (valueAnimatorThree != null){
            if (valueAnimatorThree.isRunning()){
                valueAnimatorThree.cancel();
            }
            valueAnimatorThree.removeAllUpdateListeners();
            valueAnimatorThree = null;
        }
        if (valueAnimatorFour != null){
            if (valueAnimatorFour.isRunning()){
                valueAnimatorFour.cancel();
            }
            valueAnimatorFour.removeAllUpdateListeners();
            valueAnimatorFour = null;
        }
        if (valueAnimatorFive != null){
            if (valueAnimatorFive.isRunning()){
                valueAnimatorFive.cancel();
            }
            valueAnimatorFive.removeAllUpdateListeners();
            valueAnimatorFive = null;
        }
    }


    //开始第一个小球的动画
    private void startAnimatorOne() {
        if (valueAnimatorOne == null) {
            Log.d("valueAnimatorOne", "valueAnimatorOne");
            valueAnimatorOne = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimatorOne.setDuration(800);
            // 减速插值器
            valueAnimatorOne.setInterpolator(new DecelerateInterpolator());
            valueAnimatorOne.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 获取当前点坐标封装到mCurrentPosition
                    float value = (Float) animation.getAnimatedValue();
                    pathMeasure.getPosTan(value, mCurrentPositionOne, null);
                    postInvalidate();
                }
            });
            valueAnimatorOne.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    finishAnimateOne = 1;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishAnimateOne = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        valueAnimatorOne.start();
    }

    //开始第二个小球的动画
    private void startAnimatorTwo() {
        if (valueAnimatorTwo == null) {
            valueAnimatorTwo = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimatorTwo.setDuration(1000);
            // 减速插值器
            valueAnimatorTwo.setInterpolator(new DecelerateInterpolator());
            valueAnimatorTwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    // 获取当前点坐标封装到mCurrentPosition
                    pathMeasure.getPosTan(value, mCurrentPositionTwo, null);
                    postInvalidate();
                }
            });
            valueAnimatorTwo.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    finishAnimateTwo = 1;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishAnimateTwo = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        valueAnimatorTwo.start();
    }

    //开始第三个小球的动画
    private void startAnimatorThree() {
        if (valueAnimatorThree == null) {
            valueAnimatorThree = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimatorThree.setDuration(1200);
            // 减速插值器
            valueAnimatorThree.setInterpolator(new DecelerateInterpolator());
            valueAnimatorThree.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    // 获取当前点坐标封装到mCurrentPosition
                    pathMeasure.getPosTan(value, mCurrentPositionThree, null);
                    postInvalidate();
                }
            });
            valueAnimatorThree.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    finishAnimateThree = 1;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishAnimateThree = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        valueAnimatorThree.start();
    }

    //开始第四个小球的动画
    private void startAnimatorFour() {
        if (valueAnimatorFour == null) {
            valueAnimatorFour = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimatorFour.setDuration(1400);
            // 减速插值器
            valueAnimatorFour.setInterpolator(new DecelerateInterpolator());
            valueAnimatorFour.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    // 获取当前点坐标封装到mCurrentPosition
                    pathMeasure.getPosTan(value, mCurrentPositionFour, null);
                    postInvalidate();
                }
            });
            valueAnimatorFour.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    finishAnimateFour = 1;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishAnimateFour = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        valueAnimatorFour.start();
    }

    //开始第五个小球的动画
    private void startAnimatorFive() {
        if (valueAnimatorFive == null) {
            valueAnimatorFive = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimatorFive.setDuration(1600);
            // 减速插值器
            valueAnimatorFive.setInterpolator(new DecelerateInterpolator());
            valueAnimatorFive.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    // 获取当前点坐标封装到mCurrentPosition
                    pathMeasure.getPosTan(value, mCurrentPositionFive, null);
                    postInvalidate();
                }
            });
            valueAnimatorFive.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    finishAnimateFive = 1;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishAnimateFive = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        valueAnimatorFive.start();
    }

}
