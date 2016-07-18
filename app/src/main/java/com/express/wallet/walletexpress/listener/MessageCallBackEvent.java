package com.express.wallet.walletexpress.listener;

import com.express.wallet.walletexpress.model.MessageEvent;

/**
 * Created by ricky on 15-10-22.
 */
public interface MessageCallBackEvent {
    void onEvent(MessageEvent event);
}
