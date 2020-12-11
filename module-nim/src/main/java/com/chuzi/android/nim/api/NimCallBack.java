package com.chuzi.android.nim.api;

public interface NimCallBack<T> {
    /**
     * 成功时回调
     * @param result 结果
     */
    void onSuccess(T result);

    /**
     * 失败时回调
     * @param code  一般错误适用“-1”，特定状态码单独定义
     * @param message   错误信息（用于提示用户）
     */
    void onFail(int code, String message);
}
