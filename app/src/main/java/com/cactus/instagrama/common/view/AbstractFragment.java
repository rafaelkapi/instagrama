package com.cactus.instagrama.common.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cactus.instagrama.common.util.Colors;
import com.cactus.instagrama.common.util.Drawables;


import butterknife.ButterKnife;



public abstract class AbstractFragment<P> extends Fragment implements com.cactus.instagrama.common.view.View {

  protected P presenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayout(), container, false);

    ButterKnife.bind(this, view);

    return view;
  }

  @Nullable
  @Override
  public Context getContext() {
    return super.getContext();
  }

  @Override
  public void showProgressBar() {
  }

  @Override
  public void hideProgressBar() {
  }

  @Override
  public void setStatusBarDark() {

  }

  public Drawable findDrawable(@DrawableRes int drawableId) {
    return Drawables.getDrawable(getContext(), drawableId);
  }

  public int findColor(@ColorRes int colorId) {
    return Colors.getColor(getContext(), colorId);
  }

  public void setPresenter(P presenter) {
    this.presenter = presenter;
  }

  protected abstract @LayoutRes
  int getLayout();

}
