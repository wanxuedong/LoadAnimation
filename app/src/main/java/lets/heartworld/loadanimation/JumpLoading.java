package lets.heartworld.loadanimation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 仿58加载动画跳动的小球，和TransformationLoading控件结合起来使用,并且要关闭TransformationLoading的主动切换形状的控制器，也就是：changeKind
 */

public class JumpLoading extends LinearLayout {

    private AnimatorSet upAnimator;      //上升的动画集合
    private AnimatorSet downAnimator;    //下降的动画集合
    private boolean move = false;    //当前有没有执行动画
    private Runnable runnable;
    private TransformationLoading transformationLoading;    //跳动的小球
    private ImageView jumpShadow;       //下面的阴影




    /*
    * 下面的参数可以根据实际需要进行更改
    * */




    private int jumpDistance = 200;     //跳动的距离
    private int time = 500;    //跳动一个周期的时间，0.5秒
    private int angle = 180;    //跳动过程中的旋转角度

    public JumpLoading(Context context) {
        super(context);
        initData(context);
    }


    public JumpLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private void initData(Context context) {
        LayoutInflater.from(context).inflate(R.layout.jump_layout, this, true);
        transformationLoading = (TransformationLoading) findViewById(R.id.transformationLoading);
        jumpShadow = (ImageView) findViewById(R.id.jumpShadow);
        runnable = new Runnable() {
            @Override
            public void run() {
                ViewHelper.setRotation(transformationLoading, 180f);
                ViewHelper.setTranslationY(transformationLoading, 0f);
                ViewHelper.setScaleX(jumpShadow, 0.2f);
                move = true;
                freeFall();    //先执行下降动画
            }
        };
    }

    //下降动画
    private void freeFall() {

        if (downAnimator == null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(transformationLoading, "translationY", 0, jumpDistance);
            ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(jumpShadow, "scaleX", 0.2f, 1f);
            downAnimator = new AnimatorSet();
            downAnimator.playTogether(objectAnimator, scaleIndication);
            downAnimator.setDuration(time);
            downAnimator.setInterpolator(new AccelerateInterpolator(1.2f));
            downAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (move) {
                        transformationLoading.changeShape();
                        rise();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        downAnimator.start();
    }

    //上升动画
    private void rise() {
        if (upAnimator == null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(transformationLoading, "translationY", jumpDistance, 0);
            ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(jumpShadow, "scaleX", 1f, 0.2f);
            ObjectAnimator objectAnimator1 = null;
            objectAnimator1 = ObjectAnimator.ofFloat(transformationLoading, "rotation", 0, angle);
            upAnimator = new AnimatorSet();
            upAnimator.playTogether(objectAnimator, objectAnimator1, scaleIndication);
            upAnimator.setDuration(time);
            upAnimator.setInterpolator(new DecelerateInterpolator(1.2f));
            upAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (move) {
                        freeFall();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        upAnimator.start();
    }

    //开始执行动画，从runnable开始，然后rise和freeFall互调
    public void startAnimate(int delay) {
        if (!move) {
            if (downAnimator != null && downAnimator.isRunning()) {
                return;
            }
            this.removeCallbacks(runnable);
            if (delay > 0) {
                this.postDelayed(runnable, delay);
            } else {
                this.post(runnable);
            }
        }
    }

    //停止动画,停止所有动画和监听
    public void stopAnimate() {
        move = false;
        if (upAnimator != null) {
            if (upAnimator.isRunning()) {
                upAnimator.cancel();
            }
            upAnimator.removeAllListeners();
            for (Animator animator : upAnimator.getChildAnimations()) {
                animator.removeAllListeners();
            }
            upAnimator = null;
        }
        if (downAnimator != null) {
            if (downAnimator.isRunning()) {
                downAnimator.cancel();
            }
            downAnimator.removeAllListeners();
            for (Animator animator : downAnimator.getChildAnimations()) {
                animator.removeAllListeners();
            }
            downAnimator = null;
        }
        this.removeCallbacks(runnable);
    }

    //界面被加载结束
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        startAnimate(0);
    }

    //界面被销毁
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimate();
    }

}
