package com.musapp.musicapp.firebase;

public interface DBAsyncTaskResponse {
    void doOnResponse(String str);

    void doForResponse(String str, Object obj);
}
