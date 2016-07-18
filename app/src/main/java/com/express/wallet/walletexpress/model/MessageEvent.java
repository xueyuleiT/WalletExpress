package com.express.wallet.walletexpress.model;

/**
 * Created by ricky on 15-10-13.
 */
public class MessageEvent {
    private String type;
    private Object extra;


    private MessageEvent(Builder builder) {
        type = builder.type;
        extra = builder.extra;
    }

    public static class Builder{
        private String type;
        private Object extra;
        public Builder(String type){
            this.type =type;
        }

        public Builder extra(Object extra){
            this.extra = extra;
            return this;
        }

        public MessageEvent build(){
            return new MessageEvent(this);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
