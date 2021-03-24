package com.cactus.instagrama.main.profile.datasource;

import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.model.Post;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.model.UserProfile;
import com.cactus.instagrama.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class ProfileFireDataSource implements ProfileDataSource {

  @Override
  public void findUser(String uid, Presenter<UserProfile> presenter) {
    FirebaseFirestore.getInstance()
            .collection("user")
            .document(uid)
            .get()
            .addOnSuccessListener(documentSnapshot -> {

              User user = documentSnapshot.toObject(User.class);

              FirebaseFirestore.getInstance()
                      .collection("posts")
                      .document(uid)
                      .collection("posts")
                      .orderBy("timestamp", Query.Direction.DESCENDING)
                      .get()
                      .addOnSuccessListener(queryDocumentSnapshots -> {

                        List<Post> posts = new ArrayList<>();
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documents) {
                          Post post = doc.toObject(Post.class);
                          posts.add(post);
                        }

                        FirebaseFirestore.getInstance()
                                .collection("followers")
                                .document(uid)
                                .collection("followers")
                                .document(FirebaseAuth.getInstance().getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot1 -> {

                                  boolean following = documentSnapshot1.exists();

                                  presenter.onSuccess(new UserProfile(user, posts, following));
                                  presenter.onComplete();

                                })
                                .addOnFailureListener(e -> presenter.onError(e.getMessage()));


                      })
                      .addOnFailureListener(e -> presenter.onError(e.getMessage()));


            })
            .addOnFailureListener(e -> presenter.onError(e.getMessage()));
  }

  @Override
  public void follow(String uid) {
    DocumentReference toFollow = FirebaseFirestore.getInstance()
            .collection("user")
            .document(uid);


    toFollow.get()
            .addOnCompleteListener(task -> {
              User user = task.getResult().toObject(User.class);

              FirebaseFirestore.getInstance().runTransaction(transaction -> {
                int followers = user.getFollowers() + 1;
                transaction.update(toFollow, "followers", followers);

                return null;
              });


              DocumentReference fromFollower = FirebaseFirestore.getInstance()
                      .collection("user")
                      .document(FirebaseAuth.getInstance().getUid());


              fromFollower.get()
                      .addOnCompleteListener(task1 -> {


                        User me = task1.getResult().toObject(User.class);

                        FirebaseFirestore.getInstance().runTransaction(transaction -> {
                          int following = me.getFollowing() + 1;
                          transaction.update(fromFollower, "following", following);

                          return null;
                        });


                        FirebaseFirestore.getInstance()
                                .collection("followers")
                                .document(uid)
                                .collection("followers")
                                .document(FirebaseAuth.getInstance().getUid())
                                .set(me);

                      });


              FirebaseFirestore.getInstance()
                      .collection("posts")
                      .document(uid)
                      .collection("posts")
                      .orderBy("timestamp", Query.Direction.DESCENDING)
                      .limit(10)
                      .get()
                      .addOnCompleteListener(taskRes -> {
                        if (taskRes.isSuccessful()) {
                          List<DocumentSnapshot> documents = taskRes.getResult().getDocuments();

                          for (DocumentSnapshot doc : documents) {
                            Post post = doc.toObject(Post.class);

                            Feed feed = new Feed();
                            feed.setPhotoUrl(post.getPhotoUrl());
                            feed.setCaption(post.getCaption());
                            feed.setTimestamp(post.getTimestamp());
                            feed.setUuid(doc.getId());

                            feed.setPublisher(user);

                            FirebaseFirestore.getInstance()
                                    .collection("feed")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("posts")
                                    .document(doc.getId())
                                    .set(feed);

                          }

                        }
                      });


            });
  }

  @Override
  public void unfollow(String uid) {
    DocumentReference toUnfollow = FirebaseFirestore.getInstance().collection("user")
            .document(uid);

    toUnfollow.get()
            .addOnCompleteListener(task -> {

              User user = task.getResult().toObject(User.class);

              FirebaseFirestore.getInstance().runTransaction(transaction -> {
                int followers = user.getFollowers() - 1;
                transaction.update(toUnfollow, "followers", followers);

                return null;
              });

              DocumentReference fromFollower = FirebaseFirestore.getInstance().collection("user")
                      .document(FirebaseAuth.getInstance().getUid());

              FirebaseFirestore.getInstance().runTransaction(transaction -> {
                User follower = transaction.get(fromFollower).toObject(User.class);
                int following = follower.getFollowing() - 1;
                transaction.update(fromFollower, "following", following);

                return null;
              });

              FirebaseFirestore.getInstance()
                      .collection("followers")
                      .document(uid)
                      .collection("followers")
                      .document(FirebaseAuth.getInstance().getUid())
                      .delete();

              FirebaseFirestore.getInstance()
                      .collection("feed")
                      .document(FirebaseAuth.getInstance().getUid())
                      .collection("posts")
                      .whereEqualTo("publisher.uuid", uid)
                      .get()
                      .addOnCompleteListener(taskRes -> {

                        if (taskRes.isSuccessful()) {
                          List<DocumentSnapshot> documents = taskRes.getResult().getDocuments();

                          for (DocumentSnapshot doc : documents) {
                            DocumentReference reference = doc.getReference();
                            reference.delete();
                          }

                        }

                      });

            });
  }

}
