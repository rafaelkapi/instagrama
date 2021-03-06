package com.cactus.instagrama.main.search.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cactus.instagrama.R;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.view.AbstractFragment;
import com.cactus.instagrama.main.presentation.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends AbstractFragment<SearchPresenter> implements MainView.SearchView {

  @BindView(R.id.search_recycler)
  RecyclerView recyclerView;

  private UserAdapter userAdapter;
  private MainView mainView;

  public static SearchFragment newInstance(MainView mainView, SearchPresenter presenter) {
    SearchFragment searchFragment = new SearchFragment();
    presenter.setView(searchFragment);
    searchFragment.setMainView(mainView);
    searchFragment.setPresenter(presenter);
    return searchFragment;
  }

  private void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  public SearchFragment() {
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

    userAdapter = new UserAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(userAdapter);

    return view;
  }

  @Override
  protected int getLayout() {
    return R.layout.fragment_main_search;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_search, menu);

    MenuItem searchItem = menu.findItem(R.id.action_search);

    SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

    SearchView searchView = null;
    if (searchItem != null)
      searchView = (SearchView) searchItem.getActionView();


    if (searchView != null) {
      searchView.setSearchableInfo(
              searchManager.getSearchableInfo(((AppCompatActivity) getContext()).getComponentName()));
      searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> Log.i("Teste", hasFocus + ""));
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
          return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
          Log.d("Teste", newText);
          if (!newText.isEmpty())
            presenter.findUsers(newText);
          return false;
        }
      });

      searchItem.expandActionView();
    }
  }

  @Override
  public void showUsers(List<User> users) {
    userAdapter.setUser(users, user -> mainView.showProfile(user.getUuid()));
    userAdapter.notifyDataSetChanged();
  }

  interface ItemClickListener {
    void onClick(User user);
  }

  private static class PostViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageUser;
    private final TextView textViewUsername;
    private final TextView textViewName;

    public PostViewHolder(@NonNull View itemView) {
      super(itemView);
      imageUser = itemView.findViewById(R.id.main_search_imageview_user);
      textViewUsername = itemView.findViewById(R.id.main_search_text_view_username);
      textViewName = itemView.findViewById(R.id.main_search_text_view_name);
    }

    public void bind(User user) {
      this.textViewUsername.setText(user.getName());
      Glide.with(itemView.getContext()).load(user.getPhotoUrl()).into(imageUser);
      this.textViewName.setText(user.getName());
    }
  }

  private class UserAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<User> user = new ArrayList<>();
    private ItemClickListener listener;

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = getLayoutInflater().inflate(R.layout.item_user_list, viewGroup, false);
      return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int position) {
      postViewHolder.bind(user.get(position));
      postViewHolder.itemView.setOnClickListener(v -> {
        listener.onClick(user.get(position));
      });
    }

    @Override
    public int getItemCount() {
      return user.size();
    }

    public void setUser(List<User> user, ItemClickListener listener) {
      this.user = user;
      this.listener = listener;
    }

  }

}
