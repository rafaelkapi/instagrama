package com.cactus.instagrama.main.camera.presentation;

import android.net.Uri;


public interface AddView {

  void onImageLoaded(Uri uri);

  void dispose();

}
