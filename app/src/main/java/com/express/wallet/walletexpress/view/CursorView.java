package com.express.wallet.walletexpress.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.express.wallet.walletexpress.model.Point;
import com.express.wallet.walletexpress.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zenghui on 15/11/30.
 */
public class CursorView extends View {

    private Path path;
    private Paint paint;
    private List<Point> bubbleList = new ArrayList<Point>();
    int pathCenterX, radius;
    int cIndex = 0;
    public boolean isAnimation = false;

    public int getcIndex() {
        return cIndex;
    }

    public void setcIndex(int cIndex) {
        this.cIndex = cIndex;
    }

    public CursorView(Context context) {
        super(context);
        init();
    }

    public CursorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CursorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xF0D29E);
    }

    int lineLength;

    public void setBubbleList(int length) {
        radius = getHeight() / 2;
        for (int i = 0; i < length; i++) {
            Point point = new Point(getWidth() / 10 + getWidth() / (length * 2) + i * (getWidth() * 2 / 3) / (length), getHeight() / 2, radius);
            bubbleList.add(point);
        }
        lineLength = (int) (getWidth() / (1.78 * length));
        path.reset();
        Point point1 = bubbleList.get(0);
        path.moveTo(point1.getX() - lineLength, point1.getY() + point1.getR());
        RectF rectF = new RectF(point1.getX() - lineLength - point1.getR(), point1.getY() - point1.getR(), point1.getX() - lineLength + point1.getR(), point1.getY() + point1.getR());
        path.arcTo(rectF, 90, 180);
        path.lineTo(point1.getX(), point1.getY() - point1.getR());
        RectF rectF1 = new RectF(point1.getX() - point1.getR(), point1.getY() - point1.getR(), point1.getX() + point1.getR(), point1.getY() + point1.getR());
        path.arcTo(rectF1, 270, 180);
        path.close();
        pathCenterX = point1.getX() - lineLength / 2;

    }

    private ValueAnimator valueAnimator;

    public void jumpToRight(final int index, final boolean isToOriginal) {

        if (index > bubbleList.size() - 1 || isAnimation) {
            return;
        }

        int endX;
        if (index == 0) {
            endX = (int) (getWidth() * 1.5 / (bubbleList.size()));
        } else if (index == bubbleList.size() - 1) {
            endX = (bubbleList.get(index).getX() + (getWidth() * 2 / 5) / (bubbleList.size()) - pathCenterX - radius);
        } else {
            endX = bubbleList.get(index).getX() - pathCenterX;
        }
        valueAnimator = ValueAnimator.ofInt(0, endX);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int moveX = (int) animation.getAnimatedValue();
                int nextX = pathCenterX + moveX;

                path.reset();
                Point point1 = bubbleList.get(index);
                Point point = bubbleList.get(index - 1);
                RectF rectF;
                if (nextX - lineLength / 2 - radius <= point.getX() + radius && nextX - lineLength / 2 - radius >= point.getX() - radius) {
                    path.moveTo(point.getX(), getHeight() / 2 + radius);
                    rectF = new RectF(point.getX() - radius, getHeight() / 2 - radius, point.getX() + radius, getHeight() / 2 + radius);
                } else {
                    path.moveTo(nextX - lineLength / 2, getHeight() / 2 + radius);
                    rectF = new RectF(nextX - lineLength / 2 - radius, getHeight() / 2 - radius, nextX - lineLength / 2 + radius, getHeight() / 2 + radius);
                }
                path.arcTo(rectF, 90, 180);

                if (nextX + lineLength / 2 + radius >= point1.getX() - radius && nextX + lineLength / 2 + radius <= point1.getX() + radius) {
                    path.lineTo(point1.getX(), getHeight() / 2 - radius);
                    rectF = new RectF(point1.getX() - radius, getHeight() / 2 - radius, point1.getX() + radius, getHeight() / 2 + radius);
                } else {
                    path.lineTo(nextX + lineLength / 2, getHeight() / 2 - radius);
                    rectF = new RectF(nextX + lineLength / 2 - radius, getHeight() / 2 - radius, nextX + lineLength / 2 + radius, getHeight() / 2 + radius);
                }
                path.arcTo(rectF, 270, 180);
                path.close();
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (index == bubbleList.size() - 1) {
                    pathCenterX = bubbleList.get(index).getX() + lineLength / 2;
                } else
                    pathCenterX = bubbleList.get(index).getX();
                if (!isToOriginal)
                    cIndex++;
                isAnimation = false;
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
        isAnimation = true;

    }


    public void jumpToOrginal() {
        if (cIndex == 0) {
            if (pathCenterX > bubbleList.get(cIndex).getX() - lineLength / 2) {
                jumpToLeft(cIndex, true);
            } else if (pathCenterX < bubbleList.get(cIndex).getX() - lineLength / 2) {
                jumpToRight(cIndex, true);
            }
        } else if (cIndex == bubbleList.size() - 1) {
            if (pathCenterX > bubbleList.get(cIndex).getX() + lineLength / 2) {
                jumpToLeft(cIndex, true);
            } else if (pathCenterX < bubbleList.get(cIndex).getX() + lineLength / 2) {
                jumpToRight(cIndex, true);
            }
        } else if (pathCenterX > bubbleList.get(cIndex).getX()) {
            jumpToLeft(cIndex, true);
        } else if (pathCenterX < bubbleList.get(cIndex).getX()) {
            jumpToRight(cIndex, true);
        }
    }

    public void toRight(int index, float dx) {
        if (isAnimation) {
            return;
        }
        float moveX = (float) Math.ceil((dx * getWidth()) / CommonUtil.screem_width);
        if(moveX  == 0f){
            return;
        }
        if (moveX > lineLength / 2) {
            moveX = lineLength / 2;
        }

        int endX;
        if (index > bubbleList.size() - 1) {
            endX = (bubbleList.get(index - 1).getX() + (getWidth() * 2 / 5) / (bubbleList.size()) - pathCenterX - radius);
            if (endX <= 0) {
                jumpToOrginal();
                return;
            }
            index--;
        } else {
            if (pathCenterX < bubbleList.get(index - 1).getX() & index > 1) {
                index--;
            }
        }


        Point point = bubbleList.get(index);
        Point point1 = bubbleList.get(index - 1);
        if (pathCenterX > point.getX()) {
            return;
        }
        path.reset();
        RectF rectF;
        if (pathCenterX - lineLength / 2 - radius + moveX <= point1.getX() + radius && pathCenterX - lineLength / 2 - radius + moveX >= point1.getX() - radius) {
            path.moveTo(point1.getX(), getHeight() / 2 + radius);
            rectF = new RectF(point1.getX() - radius, getHeight() / 2 - radius, point1.getX() + radius, getHeight() / 2 + radius);
            path.arcTo(rectF, 90, 180);
        } else {
            path.moveTo(pathCenterX - lineLength / 2 + moveX, getHeight() / 2 + radius);
            rectF = new RectF(pathCenterX - lineLength / 2 - radius + moveX, getHeight() / 2 - radius, pathCenterX - lineLength / 2 + radius + moveX, getHeight() / 2 + radius);
            path.arcTo(rectF, 90, 180);
        }

        if (pathCenterX + lineLength / 2 + radius + moveX >= point.getX() - radius && (pathCenterX + lineLength / 2 + moveX <= point.getX())) {
            path.lineTo(point.getX(), point.getY() - point.getR());
            rectF = new RectF(point.getX() - point.getR(), point.getY() - point.getR(), point.getX() + point.getR(), point.getY() + point.getR());
            pathCenterX += moveX;

            if (pathCenterX > point.getX()) {
                pathCenterX = point.getX();
            }

        } else {
            path.lineTo(pathCenterX + lineLength / 2 + moveX, getHeight() / 2 - radius);
            rectF = new RectF(pathCenterX + lineLength / 2 - radius + moveX, getHeight() / 2 - radius, pathCenterX + lineLength / 2 + radius + moveX, getHeight() / 2 + radius);
            pathCenterX += moveX;
        }
        path.arcTo(rectF, 270, 180);
        path.close();

        invalidate();
    }


    private int getIndex(int x) {
        int length = bubbleList.size();
        for (int i = 0; i < length; i++) {
            if (x < bubbleList.get(i).getX() + radius & x > bubbleList.get(i).getX() - radius) {
                return i;
            }
        }

        return -1;
    }

    public void toLeft(int index, float dx) {
        if (isAnimation) {
            return;
        }

        float moveX =  (dx * getWidth()) / CommonUtil.screem_width;
        if(moveX == 0f){
            return;
        }

        if (moveX > lineLength / 2) {
            moveX = lineLength / 2;
        }

        int endX;
        if (index < 0) {
            endX = pathCenterX - radius + (getWidth() * 2 / 5) / (bubbleList.size()) - (bubbleList.get(index + 1).getX());
            if (endX <= 0) {
                jumpToOrginal();
                return;
            }
            index++;
        } else {
            if (pathCenterX > bubbleList.get(index + 1).getX() & index < bubbleList.size() - 2) {
                index++;
            }
        }

        Point point1 = bubbleList.get(index + 1);
        Point point = bubbleList.get(index);
        if (pathCenterX < point.getX()) {
            return;
        }
        path.reset();

        RectF rectF;
        if (pathCenterX + lineLength / 2 + radius - moveX >= point1.getX() - radius && pathCenterX + lineLength / 2 - moveX <= point1.getX()) {
            path.moveTo(point1.getX(), getHeight() / 2 + radius);
            rectF = new RectF(point1.getX() - radius, getHeight() / 2 - radius, point1.getX() + radius, getHeight() / 2 + radius);
            path.arcTo(rectF, 90, -180);
        } else {
            path.moveTo(pathCenterX + lineLength / 2 - moveX, getHeight() / 2 + radius);
            rectF = new RectF(pathCenterX + lineLength / 2 - radius - moveX, getHeight() / 2 - radius, pathCenterX + lineLength / 2 + radius - moveX, getHeight() / 2 + radius);
            path.arcTo(rectF, 90, -180);
        }

        if (pathCenterX - lineLength / 2 - radius - moveX <= point.getX() + radius && (pathCenterX - lineLength / 2 - moveX >= point.getX())) {
            path.lineTo(point.getX(), point.getY() - point.getR());
            rectF = new RectF(point.getX() - point.getR(), point.getY() - point.getR(), point.getX() + point.getR(), point.getY() + point.getR());
            pathCenterX -= moveX;


            if (pathCenterX < point.getX()) {
                pathCenterX = point.getX();
            }


        } else {
            path.lineTo(pathCenterX - lineLength / 2 - moveX, getHeight() / 2 - radius);
            rectF = new RectF(pathCenterX - lineLength / 2 - radius - moveX, getHeight() / 2 - radius, pathCenterX - lineLength / 2 + radius - moveX, getHeight() / 2 + radius);
            pathCenterX -= moveX;
        }
        path.arcTo(rectF, 270, -180);
        path.close();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bubbleList.size() == 0) {
            return;
        }
        paint.reset();
        paint.setAntiAlias(true);


        if (cIndex == 0) {
            paint.setColor(0xFFEDEDEB);
        } else if (cIndex == 1) {
            paint.setColor(0xFF749EB4);
        } else {
            paint.setColor(0xFFE3B976);
        }
        paint.setAlpha(127);
        int length = bubbleList.size();
        for (int i = 0; i < length; i++) {
            Point point = bubbleList.get(i);
            canvas.drawCircle(point.getX(), point.getY(), point.getR(), paint);
        }
        paint.setAlpha(255);
        canvas.drawPath(path, paint);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bubbleList.size() == 0) {
            setBubbleList(3);
        }
    }

    public void jumpToLeft(final int index, final boolean isToOriginal) {
        if (index < 0 || index > bubbleList.size() - 1 || isAnimation) {
            return;
        }

        int endX;
        if (index == 0) {
            endX = pathCenterX - radius + (getWidth() * 2 / 5) / (bubbleList.size()) - (bubbleList.get(index).getX());
        } else if (index == bubbleList.size() - 1) {
            endX = (bubbleList.get(index).getX() + (getWidth() * 2 / 5) / (bubbleList.size()) - pathCenterX - radius);
        } else {
            endX = pathCenterX - bubbleList.get(index).getX();
        }
        valueAnimator = ValueAnimator.ofInt(0, endX);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int moveX = (int) animation.getAnimatedValue();
                int nextX = pathCenterX - moveX;

                path.reset();
                Point point1 = bubbleList.get(index);
                Point point = bubbleList.get(index + 1);
                RectF rectF;
                if (nextX - lineLength / 2 - radius <= point1.getX() + radius && nextX - lineLength / 2 - radius >= point1.getX() - radius) {
                    path.moveTo(point1.getX(), getHeight() / 2 + radius);
                    rectF = new RectF(point1.getX() - radius, getHeight() / 2 - radius, point1.getX() + radius, getHeight() / 2 + radius);
                } else {
                    path.moveTo(nextX - lineLength / 2, getHeight() / 2 + radius);
                    rectF = new RectF(nextX - lineLength / 2 - radius, getHeight() / 2 - radius, nextX - lineLength / 2 + radius, getHeight() / 2 + radius);

                }
                path.arcTo(rectF, 90, 180);

                if (nextX + lineLength / 2 + radius <= point.getX() + radius && nextX + lineLength / 2 + radius >= point.getX() - radius) {
                    path.lineTo(point.getX(), getHeight() / 2 - radius);
                    rectF = new RectF(point.getX() - radius, getHeight() / 2 - radius, point.getX() + radius, getHeight() / 2 + radius);
                } else {
                    path.lineTo(nextX + lineLength / 2, getHeight() / 2 - radius);
                    rectF = new RectF(nextX + lineLength / 2 - radius, getHeight() / 2 - radius, nextX + lineLength / 2 + radius, getHeight() / 2 + radius);
                }
                path.arcTo(rectF, 270, 180);
                path.close();
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (index == 0) {
                    pathCenterX = bubbleList.get(index).getX() - lineLength / 2;
                } else
                    pathCenterX = bubbleList.get(index).getX();
                if (!isToOriginal)
                    cIndex--;
                isAnimation = false;
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
        isAnimation = true;
    }
}
