package com.cactus.instagrama.main.camera.datasource;

import android.net.Uri;

import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.model.Post;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;


public class AddFireDataSource implements AddDataSource {

  @Override
  public void savePost(Uri uri, String caption, Presenter presenter) {
    String host = uri.getHost();

    if (host == null) // for gallery
        uri = Uri.fromFile(new File(uri.toString()));

    String uid = FirebaseAuth.getInstance().getUid();

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference imgRef = storageRef.child("images/")
            .child(uid)
            .child(uri.getLastPathSegment());

    imgRef.putFile(uri)
            .addOnSuccessListener(taskSnapshot -> {

              imgRef.getDownloadUrl()
                      .addOnSuccessListener(uriResponse -> {

                        Post post = new Post();
                        post.setPhotoUrl(uriResponse.toString());
                        post.setCaption(caption);
                        post.setTimestamp(System.currentTimeMillis());

                        DocumentReference postRef = FirebaseFirestore.getInstance()
                                .collection("posts")
                                .document(uid)
                                .collection("posts")
                                .document();

                        postRef.set(post)
                                .addOnSuccessListener(aVoid -> {

                                  DocumentReference me = FirebaseFirestore.getInstance()
                                          .collection("user")
                                          .document(uid);

                                  FirebaseFirestore.getInstance().runTransaction(transaction -> {
                                    DocumentSnapshot snapshot = transaction.get(me);

                                    User user = snapshot.toObject(User.class);
                                    int posts = user.getPosts() + 1;
                                    transaction.update(me, "posts", posts);

                                    return null;
                                  });

                                  me.get().addOnCompleteListener(t -> {
                                    if (t.isSuccessful()) {

                                      User meUser = t.getResult().toObject(User.class);

                                                                        // save posts in feed followers
                                  FirebaseFirestore.getInstance().collection("followers")
                                          .document(uid)
                                          .collection("followers")
                                          .get()
                                          .addOnCompleteListener(task -> {

                                            if (!task.isSuccessful()) return;

                                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                            for (DocumentSnapshot doc : documents) {


                                              User user = doc.toObject(User.class);

                                              Feed feed = new Feed();
                                              feed.setPhotoUrl(post.getPhotoUrl());
                                              feed.setCaption(post.getCaption());
                                              feed.setTimestamp(post.getTimestamp());
                                              feed.setUuid(postRef.getId());
                                              feed.setPublisher(meUser);

                                              FirebaseFirestore.getInstance()
                                                      .collection("feed")
                                                      .document(user.getUuid())
                                                      .collection("posts")
                                                      .document(postRef.getId())
                                                      .set(feed);


                                            }

                                          });

                                    }
                                  });




                                  String id = postRef.getId();

                                  Feed feed = new Feed();
                                  feed.setPhotoUrl(post.getPhotoUrl());
                                  feed.setCaption(post.getCaption());
                                  feed.setTimestamp(post.getTimestamp());
                                  feed.setUuid(id);


                                  FirebaseFirestore.getInstance().collection("user")
                                          .document(uid)
                                          .get()
                                          .addOnCompleteListener(task -> {

                                            User user = task.getResult().toObject(User.class);

                                            feed.setPublisher(user);

                                            FirebaseFirestore.getInstance()
                                                    .collection("feed")
                                                    .document(uid)
                                                    .collection("posts")
                                                    .document(id)
                                                    .set(feed);


                                          });

                                  presenter.onSuccess(null);
                                  presenter.onComplete();


                                })
                                .addOnFailureListener(e -> presenter.onError(e.getMessage()));

                      })
                      .addOnFailureListener(e -> presenter.onError(e.getMessage()));

            })
            .addOnFailureListener(e -> presenter.onError(e.getMessage()));
  }

}
