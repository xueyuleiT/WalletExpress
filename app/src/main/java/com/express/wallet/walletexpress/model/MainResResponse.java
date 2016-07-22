package com.express.wallet.walletexpress.model;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zenghui on 2016/7/22.
 */
public class MainResResponse {

    int msg,count;
    String sum;
    List<LogInfo> logs;
    List<ImgInfo> imgs;

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public List<LogInfo> getLogs() {
        return logs;
    }

    public void setLogs(List<LogInfo> logs) {
        this.logs = logs;
    }

    public List<ImgInfo> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgInfo> imgs) {
        this.imgs = imgs;
    }
}
