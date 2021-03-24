package com.cactus.instagrama.main.home.presentation;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cactus.instagrama.R;
import com.cactus.instagrama.common.component.CustomDialog;
import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.view.AbstractFragment;
import com.cactus.instagrama.main.presentation.MainView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends AbstractFragment<HomePresenter> implements MainView.HomeView {

  @BindView(R.id.home_recycler)
  RecyclerView recyclerView;

  private FeedAdapter feedAdapter;
  private MainView mainView;

  public static HomeFragment newInstance(MainView mainView, HomePresenter homePresenter) {
    HomeFragment homeFragment = new HomeFragment();
    homeFragment.setMainView(mainView);
    homeFragment.setPresenter(homePresenter);
    homePresenter.setView(homeFragment);
    return homeFragment;
  }

  public HomeFragment() {
  }

  private void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    feedAdapter = new FeedAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(feedAdapter);

    return view;



  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_settings:
        CustomDialog customDialog = new CustomDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .addButton((v) -> {
                  switch (v.getId()) {
                    case R.string.logout_action:
                      FirebaseAuth.getInstance().signOut();
                      mainView.logout();
                      break;
                    case R.string.cancel:
                      break;
                  }
                }, R.string.logout_action, R.string.cancel)
                .build();
        customDialog.show();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.findFeed();
  }

  @Override
  public void showProgressBar() {
    mainView.showProgressBar();
  }

  @Override
  public void hideProgressBar() {
    mainView.hideProgressBar();
  }

  @Override
  public void showFeed(List<Feed> response) {
    feedAdapter.setFeed(response);
    feedAdapter.notifyDataSetChanged();
  }

  @Override
  protected int getLayout() {
    return R.layout.fragment_main_home;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_profile, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }


  private static class PostViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imagePost;
    private final ImageView imageUser;
    private final TextView textViewCaption;
    private final TextView textViewUsername;

    public PostViewHolder(@NonNull View itemView) {
      super(itemView);
      imagePost = itemView.findViewById(R.id.profile_image_grid);
      imageUser = itemView.findViewById(R.id.home_container_user_photo);
      textViewCaption = itemView.findViewById(R.id.home_container_user_caption);
      textViewUsername = itemView.findViewById(R.id.home_container_user_username);
    }

    public void bind(Feed feed) {
      Glide.with(itemView.getContext()).load(feed.getPhotoUrl()).into(this.imagePost);
      this.textViewCaption.setText(feed.getCaption());

      User user = feed.getPublisher();
      if (user != null) {
        Glide.with(itemView.getContext()).load(user.getPhotoUrl()).into(this.imageUser);
        this.textViewUsername.setText(user.getName());
      }
    }

  }

  private class FeedAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<Feed> feed = new ArrayList<>();

    public void setFeed(List<Feed> feed) {
      this.feed = feed;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      return new PostViewHolder(getLayoutInflater().inflate(R.layout.item_post_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
      postViewHolder.bind(feed.get(i));
    }

    @Override
    public int getItemCount() {
      return feed.size();
    }

  }

}
