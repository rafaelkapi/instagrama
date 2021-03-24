package com.cactus.instagrama.main.camera.datasource;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.cactus.instagrama.common.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;




public class GalleryLocalDataSource implements GalleryDataSource {

  @Override
  public void findPictures(Context context, Presenter presenter) {


    List<String> images = null;

    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = {
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED
    };

    if (context != null && context.getContentResolver() != null) {
      Cursor cursor = context.getContentResolver().query(uri, projection, null, null,
              MediaStore.Images.Media.DATE_ADDED);

      if (cursor != null) {
        images = new ArrayList<>();

        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnIndexFolder = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int columnIndexDataAdded = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

        while (cursor.moveToNext()) {
          String absolutePathImage = cursor.getString(columnIndexData);
          String folder = cursor.getString(columnIndexFolder);
          String dateAdded = cursor.getString(columnIndexDataAdded);

//          Log.d("Teste Folder", folder);
//          Log.d("Teste Date", dateAdded);
          images.add(absolutePathImage);
          if(images.size() == 8) break;
        }
      }
    }

    if (images != null && !images.isEmpty())
      presenter.onSuccess(images);
  }

}
