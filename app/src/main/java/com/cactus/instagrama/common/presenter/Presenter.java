package com.cactus.instagrama.common.presenter;


public interface Presenter<T> {

  void onSuccess(T response);

  void onError(String message);

  void onComplete();

}
