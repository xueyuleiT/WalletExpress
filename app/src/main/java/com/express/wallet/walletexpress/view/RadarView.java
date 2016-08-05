package com.express.wallet.walletexpress.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.net.LinkAddress;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.express.wallet.walletexpress.R;
import com.express.wallet.walletexpress.listener.DialogListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zenghui on 2016/7/29.
 */
public class RadarView extends View{

    private DialogListener dialogListener;
    private Paint mGradientPaint;
    private Paint linePaint,textPaint,mPaint;
    private String text = "241";
    private List<Rect> rectList = new ArrayList<>();


    private ValueAnimator valueAnimator;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mGradientPaint = new Paint();
        mGradientPaint.setStyle(Paint.Style.FILL);
        mGradientPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(Color.argb(255,74,144,226));
        linePaint.setAntiAlias(true);

        textPaint= new Paint();
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.public_textsize_value_24));
        textPaint.setColor(Color.WHITE);

        mPaint = new Paint();
        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.public_textsize_value_16));

    }

    int index = -1;
    long downTime = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rectList.size() >0 && dialogListener != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isOnRect(rectList.get(0), event)) {
                    index = 0;
                    downTime = System.currentTimeMillis();
                } else if (isOnRect(rectList.get(1), event)) {
                    index = 1;
                    downTime = System.currentTimeMillis();
                } else if (isOnRect(rectList.get(2), event)) {
                    index = 2;
                    downTime = System.currentTimeMillis();
                } else if (isOnRect(rectList.get(3), event)) {
                    index = 3;
                    downTime = System.currentTimeMillis();
                } else if (isOnRect(rectList.get(4), event)) {
                    index = 4;
                    downTime = System.currentTimeMillis();
                }
                return true;
            }else if (event.getAction() == MotionEvent.ACTION_UP){
                if (index >= 0 &&  System.currentTimeMillis() - downTime <= 300){
                    if (isOnRect(rectList.get(index), event)) {
                        dialogListener.handle(""+index);
                    }
                }
                index = -1;
            }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
                index = -1;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    boolean isOnRect(Rect rect,MotionEvent motionEvent){

        if (motionEvent.getX()<= rect.right && motionEvent.getX() >= rect.left
                && motionEvent.getY()>=rect.top - rect.height()/2&& motionEvent.getY()<= rect.bottom+rect.height()/2){
            return true;
        }

        return false;
    }

    int margin = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Point> points = getPoint(getWidth()*3/10);
        for (int i = 0;i < points.size();i++) {
            Point p,pNext;
            if (i < points.size()-1) {
                 p = points.get(i);
                 pNext = points.get(i+1);
            }else {
                 p = points.get(i);
                 pNext = points.get(0);
            }
            canvas.drawLine(p.x, p.y,pNext.x,pNext.y,linePaint );
            Rect rect = new Rect();
            int x = 0,y = 0;
            if (i == 0){
                String mStr = "网络消费";
                mPaint.getTextBounds(mStr,0,mStr.length(),rect);
                x = p.x-rect.width()-margin;
                y = p.y+rect.height()/2-margin;
                canvas.drawText(mStr,p.x-rect.width()-margin,p.y+rect.height()/2-margin,mPaint);
            }else if (i == 1){
                String mStr = "网络消费";
                mPaint.getTextBounds(mStr,0,mStr.length(),rect);
                x = p.x-rect.width()/2;
                y = p.y-margin;
                canvas.drawText(mStr,x,y,mPaint);
            }else if (i == 2){
                String mStr = "网络消费";
                mPaint.getTextBounds(mStr,0,mStr.length(),rect);
                x = p.x+margin;
                y = p.y+rect.height()/2 -margin;
                canvas.drawText(mStr,p.x+margin,p.y+rect.height()/2 -margin,mPaint);
            }else if (i == 3){
                String mStr = "网络消费";
                mPaint.getTextBounds(mStr,0,mStr.length(),rect);
                x = p.x+margin;
                y = p.y+rect.height()/2+margin;
                canvas.drawText(mStr,p.x+margin,p.y+rect.height()/2+margin,mPaint);
            }else if (i == 4){
                String mStr = "网络消费";
                mPaint.getTextBounds(mStr,0,mStr.length(),rect);
                x = p.x-rect.width() - margin;
                y = p.y+rect.height()/2+margin;
                canvas.drawText(mStr,p.x-rect.width() - margin,p.y+rect.height()/2+margin,mPaint);
            }
            if (rectList.size() < 5) {
                rectList.add(new Rect(x, y, x + rect.width(), y + rect.height()));
            }
        }
        drawRadar(canvas,0.86f,53);
        drawRadar(canvas,0.72f,58);
        drawRadar(canvas,0.58f,65);
        drawRadar(canvas,0.42f,73);
        drawRadar(canvas,0.28f,81);
        drawRadar(canvas,0.14f,90);

        if (radarR == -1){
            return;
        }
        List<Point> pList = getPentagon(radarR);
        int size = pList.size();
        mGradientPaint.reset();
        mGradientPaint.setStyle(Paint.Style.FILL);
        mGradientPaint.setAntiAlias(true);
        mGradientPaint.setColor(Color.argb(150,74,144,226));
        for (int i = 0;i <size;i++) {
            Point p,pNext;
            if (i < size-1) {
                p = pList.get(i);
                pNext = pList.get(i+1);
            }else {
                p = pList.get(i);
                pNext = pList.get(0);

            }

            Path path = new Path();
            path.moveTo(getWidth() / 2, getWidth() / 2);// 此点为多边形的起点
            path.lineTo(p.x, p.y);
            path.lineTo(pNext.x, pNext.y);

            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, mGradientPaint);

        }

        if (radarR < getWidth()){
            return;
        }

        Rect rect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,getWidth()/2 - rect.width()/2,getHeight()/2+rect.height()/2,textPaint);
    }


   void drawRadar(Canvas canvas,float pecent,int alpha){
        List<Point> points = getPoint((int) (getWidth()*pecent*3/10));
        int size = points.size();
        for (int i = 0;i <size;i++) {
            Point p,pNext;//,bp = null,bpNext = null;
            if (i < size-1) {
                p = points.get(i);
                pNext = points.get(i+1);

            }else {
                p = points.get(i);
                pNext = points.get(0);

            }

            Shader mShader = new LinearGradient( getWidth()/2,  getWidth()/2, (p.x+pNext.x)/2,(p.y+pNext.y)/2,
                    new int[] {Color.argb(alpha,74,144,226), Color.argb(alpha/2,74,144,226) }, new float[]{0.0f,1f}, Shader.TileMode.CLAMP); // 一个材质,打造出一个线性梯度沿著一条线。
            mGradientPaint.setShader(mShader);

            Path path = new Path();
            path.moveTo(getWidth() / 2, getWidth() / 2);// 此点为多边形的起点
            path.lineTo(p.x, p.y);
            path.lineTo(pNext.x, pNext.y);

            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, mGradientPaint);
        }
       mGradientPaint.reset();
    }

    int radarR = -1;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bottom - top > 0 & margin == 0){
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = getWidth();
            setLayoutParams(layoutParams);

            margin = getWidth()/25;
            valueAnimator = ValueAnimator.ofInt(0,getWidth());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    radarR = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }
    }

    List<Point> getPoint(int r){
        List<Point> points = new ArrayList<>();
        Point point1 = new Point();
        point1.set((int) (getWidth()/2-r*Math.cos(Math.toRadians(18))),(int)(getWidth()/2-r*Math.sin(Math.toRadians(18))));
        points.add(point1);

        Point point2 = new Point();
        point2.set(getWidth()/2,getWidth()/2-r);
        points.add(point2);

        Point point3 = new Point();
        point3.set((int) (getWidth()/2+r*Math.cos(Math.toRadians(18))),(int)(getWidth()/2-r*Math.sin(Math.toRadians(18))));
        points.add(point3);

        Point point4 = new Point();
        point4.set((int) (getWidth()/2+r*Math.sin(Math.toRadians(36))),(int)(getWidth()/2+r*Math.cos(Math.toRadians(36))));
        points.add(point4);

        Point point5 = new Point();
        point5.set((int) (getWidth()/2-r*Math.sin(Math.toRadians(36))),(int)(getWidth()/2+r*Math.cos(Math.toRadians(36))));
        points.add(point5);
        return points;
    }

    List<Point> getPentagon(int radarR){
        int r = radarR*3/20;
        List<Point> points = new ArrayList<>();
        Point point1 = new Point();
        point1.set((int) (getWidth()/2-r*Math.cos(Math.toRadians(18))),(int)(getWidth()/2-r*Math.sin(Math.toRadians(18))));
        points.add(point1);

        Point point2 = new Point();
        point2.set(getWidth()/2, getWidth()/2 - (int) (radarR*15.9/80));
        points.add(point2);

        r = radarR*9/80;
        Point point3 = new Point();
        point3.set((int) (getWidth()/2+r*Math.cos(Math.toRadians(18))),(int)(getWidth()/2-r*Math.sin(Math.toRadians(18))));
        points.add(point3);

        r = radarR*3/20;
        Point point4 = new Point();
        point4.set((int) (getWidth()/2+r*Math.sin(Math.toRadians(36))),(int)(getWidth()/2+r*Math.cos(Math.toRadians(36))));
        points.add(point4);

        r = (int) (radarR*16.5/80);
        Point point5 = new Point();
        point5.set((int) (getWidth()/2-r*Math.sin(Math.toRadians(36))),(int)(getWidth()/2+r*Math.cos(Math.toRadians(36))));
        points.add(point5);
        return points;
    }
}
