package com.express.wallet.walletexpress.model;

/**
 * Created by zenghui on 15/11/30.
 */
public class Point {

    private int x, y, r;

    public Point(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    Point point1,point2,point3;

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public void setPoint3(Point point3) {
        this.point3 = point3;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
