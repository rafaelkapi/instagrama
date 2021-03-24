package com.cactus.instagrama.main.camera.datasource;

import android.net.Uri;

import com.cactus.instagrama.common.presenter.Presenter;


public interface AddDataSource {

  void savePost(Uri uri, String caption, Presenter presenter);

}
