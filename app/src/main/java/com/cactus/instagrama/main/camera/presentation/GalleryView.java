package com.cactus.instagrama.main.camera.presentation;

import android.net.Uri;

import com.cactus.instagrama.common.view.View;

import java.util.List;


public interface GalleryView extends View {

  void onPicturesLoaded(List<Uri> uris);
}
