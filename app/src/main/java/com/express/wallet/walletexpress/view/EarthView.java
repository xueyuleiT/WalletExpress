package com.express.wallet.walletexpress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.express.wallet.walletexpress.R;
import com.express.wallet.walletexpress.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zenghui on 2016/7/22.
 */
public class EarthView extends View{

    List<Point> points = new ArrayList<>();
    Timer timer = new Timer();
    Paint paint = new Paint();
    Paint linePaint = new Paint();
    void mRun(){
        int size = points.size()-1;
        for (int i = 0; i < size;i++){
            Point point = points.get(i);
            if (point.isBoard()){
                point.setPoint3(null);
            }else {
                point.setPoint1(null);
                point.setPoint2(null);
                point.setPoint3(null);
            }
        }

        for (int i = 0; i < size;i++){
            movePoint(points.get(i));
        }
        for (int i = 0; i < size;i++){
            findRelativePoint(points.get(i));
        }
        handler.sendEmptyMessage(0);
    }

    public EarthView(Context context) {
        super(context);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mRun();
            }
        };
        timer.schedule(timerTask,10,200);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        linePaint.setStrokeWidth(cr/2);
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    public EarthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mRun();
            }
        };
        timer.schedule(timerTask,10,200);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        linePaint.setStrokeWidth(cr/2);
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    public EarthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mRun();
            }
        };
        timer.schedule(timerTask,10,200);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        linePaint.setStrokeWidth(cr/2);
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postInvalidate();
        }
    };
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = points.size()-1;
        for (int i = 0; i < size;i++){
            Point mPoint = points.get(i);
            if (mPoint.getPoint1() != null) {
                canvas.drawLine(mPoint.getX(), mPoint.getY(), mPoint.getPoint1().getX(), mPoint.getPoint1().getY(), linePaint);
            }
            if (mPoint.getPoint2() != null) {
                canvas.drawLine(mPoint.getX(), mPoint.getY(), mPoint.getPoint2().getX(), mPoint.getPoint2().getY(), linePaint);
            }
            if (mPoint.getPoint3() != null) {
                canvas.drawLine(mPoint.getX(), mPoint.getY(), mPoint.getPoint3().getX(), mPoint.getPoint3().getY(), linePaint);
            }
            canvas.drawCircle(mPoint.getX(),mPoint.getY(),cr,paint);
        }
//        Paint mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(cr/2);
//        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
//        canvas.drawCircle(r,r,r,mPaint);

    }

    int w,h,r;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bottom - top > 0 && points.size() == 0){
            w = getWidth();
            h = getHeight();
            r = w/2;
            int x = 0,y = 0;
            int speed = getResources().getDimensionPixelSize(R.dimen.public_space_value_2);
            for (int i = 0; i < 50;i++){
                x = random.nextInt(w);
                y = random.nextInt(h);
                while (Math.pow(x-r,2)+Math.pow(y-r,2) >= r*r){
                    x = random.nextInt(w);
                    y = random.nextInt(h);
                }
                Point point = new Point();
                point.setX(x);
                point.setY(y);
                point.setoX(x);
                point.setoY(y);
                point.setAngle(random.nextInt(360));
                point.setSpeed(cr/2);
                points.add(point);
            }

            for (int i = 0; i <11 ;i++){
                Point point = new Point();
                int angle = i*36+5;
                int mx = 0,my = 0;
//                if (angle>=90 & angle < 180) {// 第二区
//                    mx += r * Math.cos(Math.toRadians(angle));
//                    my -= r * Math.sin(Math.toRadians(angle));
//                } else if (angle>=180 & angle < 270) {// 第三区
//                    mx -= r * Math.cos(Math.toRadians(angle));
//                    my += r * Math.sin(Math.toRadians(angle));
//
//                } else if (angle>=270) {// 第四区
//                    mx += r * Math.cos(Math.toRadians(angle));
//                    my -= r * Math.sin(Math.toRadians(angle));
//
//                } else if (angle>=0 & angle < 90) {// 第一区
//                    mx -= r * Math.cos(Math.toRadians(angle));
//                    my += r * Math.sin(Math.toRadians(angle));
//                }
                if (angle>=90 & angle < 180) {// 第二区
                    mx += r * Math.cos(Math.toRadians(angle));
                    my -= r * Math.sin(Math.toRadians(angle));
                } else if (angle>=180 & angle < 270) {// 第三区
                    mx += r * Math.cos(Math.toRadians(angle));
                    my -= r * Math.sin(Math.toRadians(angle));

                } else if (angle>=270) {// 第四区
                    mx += r * Math.cos(Math.toRadians(angle));
                    my -= r * Math.sin(Math.toRadians(angle));

                } else if (angle>=0 & angle < 90) {// 第一区
                    mx += r * Math.cos(Math.toRadians(angle));
                    my -= r * Math.sin(Math.toRadians(angle));
                }

                point.setBoard(true);
                point.setX(r+mx);
                point.setY(r+my);
                points.add(point);
            }
            for (int i = 50; i < 60; i++){
                Point point = points.get(i);

                point.setPoint1(points.get(i+1));
                points.get(i+1).setPoint2(point);
            }
            int size = points.size() -1;
            for (int i = 0; i < size;i++){
                findRelativePoint(points.get(i));
            }
        }
    }

    Random random = new Random();
    int cr = getResources().getDimensionPixelSize(R.dimen.public_space_value_2);
    void movePoint(Point point){

        if (point.isBoard()){
            return;
        }
        int angle = point.getAngle();
        int x = 0;
        int y = 0;
        int mcr = point.getSpeed();
//        if (angle>=90 & angle < 180) {// 第二区
//            x -= mcr * Math.cos(Math.toRadians(angle));
//            y -= mcr * Math.sin(Math.toRadians(angle));
//        } else if (angle>=180 & angle < 270) {// 第三区
//            x += mcr * Math.cos(Math.toRadians(angle));
//            y -= mcr * Math.sin(Math.toRadians(angle));
//
//        } else if (angle>=270) {// 第四区
//            x += mcr * Math.cos(Math.toRadians(angle));
//            y += mcr * Math.sin(Math.toRadians(angle));
//
//        } else if (angle>=0 & angle < 90) {// 第一区
//            x -= mcr * Math.cos(Math.toRadians(angle));
//            y += mcr * Math.sin(Math.toRadians(angle));
//        }

        if (angle>=90 & angle < 180) {// 第二区
            x += mcr * Math.cos(Math.toRadians(angle));
            y -= mcr * Math.sin(Math.toRadians(angle));
        } else if (angle>=180 & angle < 270) {// 第三区
            x += mcr * Math.cos(Math.toRadians(angle));
            y -= mcr * Math.sin(Math.toRadians(angle));

        } else if (angle>=270) {// 第四区
            x += mcr * Math.cos(Math.toRadians(angle));
            y -= mcr * Math.sin(Math.toRadians(angle));

        } else if (angle>=0 & angle < 90) {// 第一区
            x += mcr * Math.cos(Math.toRadians(angle));
            y -= mcr * Math.sin(Math.toRadians(angle));
        }

        while (Math.pow(point.getX()+x-point.getoX(),2)+Math.pow(point.getY()+y-point.getoY(),2) >= (r/4)*(r/4) || Math.pow(point.getX()+x-r,2)+Math.pow(point.getY()+y-r,2) >= r*r){
             angle = random.nextInt(360);
            if (angle>=90 & angle < 180) {// 第二区
                x -= mcr * Math.cos(Math.toRadians(angle));
                y -= mcr * Math.sin(Math.toRadians(angle));
            } else if (angle>=180 & angle < 270) {// 第三区
                x += mcr * Math.cos(Math.toRadians(angle));
                y -= mcr * Math.sin(Math.toRadians(angle));

            } else if (angle>=270) {// 第四区
                x += mcr * Math.cos(Math.toRadians(angle));
                y += mcr * Math.sin(Math.toRadians(angle));

            } else if (angle>=0 & angle < 90) {// 第一区
                x -= mcr * Math.cos(Math.toRadians(angle));
                y += mcr * Math.sin(Math.toRadians(angle));
            }
        }
        point.setAngle(angle);
        point.setX(point.getX()+x);
        point.setY(point.getY()+y);
    }

    void findRelativePoint(Point point){
        if (point.getPoint1() == null){
            int size = points.size();
            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i <size ; i++){
                Point mPoint = points.get(i);
                      if (mPoint == point || mPoint == point.getPoint2()|| mPoint == point.getPoint3()){
                          continue;
                      }else if (mPoint.getPoint1() != null && mPoint.getPoint2() != null && mPoint.getPoint3() != null){
                          continue;
                      }else {
                          int margin = (int) (Math.pow(mPoint.getX() - point.getX(),2) + Math.pow(mPoint.getY() - point.getY(),2));
                          if (margin < min && margin < r*r){
                              index = i;
                              min = margin;
                          }
                      }
            }
            if (index == -1){
                return;
            }
            point.setPoint1(points.get(index));
            if (points.get(index).getPoint1() == null){
                points.get(index).setPoint1(point);
            }else if (points.get(index).getPoint2() == null){
                points.get(index).setPoint2(point);
            }else if (points.get(index).getPoint3() == null){
                points.get(index).setPoint3(point);
            }
            findRelativePoint(point);
        }else if(point.getPoint2() == null){
            int size = points.size();
            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i <size ; i++){
                Point mPoint = points.get(i);
                if (mPoint == point || mPoint == point.getPoint1() || mPoint == point.getPoint3()){
                    continue;
                }else if (mPoint.getPoint1() != null && mPoint.getPoint2() != null && mPoint.getPoint3() != null){
                    continue;
                }else {
                    int margin = (int) (Math.pow(mPoint.getX() - point.getX(),2) + Math.pow(mPoint.getY() - point.getY(),2));
                    if (margin < min && margin < r*r){
                        index = i;
                        min = margin;
                    }
                }
            }
            if (index == -1){
                return;
            }
            point.setPoint2(points.get(index));
            if (points.get(index).getPoint1() == null){
                points.get(index).setPoint1(point);
            }else if (points.get(index).getPoint2() == null){
                points.get(index).setPoint2(point);
            }else if (points.get(index).getPoint3() == null){
                points.get(index).setPoint3(point);
            }
            findRelativePoint(point);
        }else if(point.getPoint3() == null){
            int size = points.size();
            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i <size ; i++){
                Point mPoint = points.get(i);
                if (mPoint == point || mPoint == point.getPoint1() || mPoint == point.getPoint2()){
                    continue;
                }else if (mPoint.getPoint1() != null && mPoint.getPoint2() != null && mPoint.getPoint3() != null){
                    continue;
                }else {
                    int margin = (int) (Math.pow(mPoint.getX() - point.getX(),2) + Math.pow(mPoint.getY() - point.getY(),2));
                    if (margin < min && margin < r*r){
                        index = i;
                        min = margin;
                    }
                }
            }
            if (index == -1){
                return;
            }
            point.setPoint3(points.get(index));
            if (points.get(index).getPoint1() == null){
                points.get(index).setPoint1(point);
            }else if (points.get(index).getPoint2() == null){
                points.get(index).setPoint2(point);
            }else if (points.get(index).getPoint3() == null){
                points.get(index).setPoint3(point);
            }
        }
    }
}
