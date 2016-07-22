package com.express.wallet.walletexpress.model;

/**
 * Created by zenghui on 2016/7/20.
 */
public class ResponseInfo {

    int msg;
    int userid;
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
