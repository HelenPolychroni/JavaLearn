package com.example.learnjava.Topic1;

public interface ScoreUpdateCallback {
    void onSuccess(String newScore);
    void onFailure(Exception e);
}
