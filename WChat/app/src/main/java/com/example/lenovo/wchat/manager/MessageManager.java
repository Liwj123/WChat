package com.example.lenovo.wchat.manager;

import com.example.lenovo.wchat.callback.IMessageList;

/**
 * Created by Lenovo on 2017/5/10.
 */

public class MessageManager {
    private IMessageList iMessageList;
    private static MessageManager messageManager = new MessageManager();

    public static MessageManager getInstance() {
        return messageManager;
    }

    public IMessageList getiMessageList() {
        return iMessageList;
    }

    public void setiMessageList(IMessageList iMessageList) {
        this.iMessageList = iMessageList;
    }
}
