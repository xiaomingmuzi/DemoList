package com.lixm.chat.network;

import org.xutils.common.Callback;

/**
 * User: LXM
 * Date: 2016-03-31
 * Time: 13:40
 * Detail:网络请求回调接口
 */
public interface NetCallBackInterface<T> {
        void onSuccess(T result);

        void onError(Throwable ex, boolean isOnCallback);

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
}
