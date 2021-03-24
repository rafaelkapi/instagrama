package com.cactus.instagrama.register.datasource;

import android.net.Uri;
import android.util.Log;

import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;




public class RegisterFireDataSource implements RegisterDataSource {

  @Override
  public void createUser(String name, String email, String password, Presenter presenter) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
              User user = new User();
              user.setEmail(email);
              user.setName(name);
              user.setUuid(authResult.getUser().getUid());

              FirebaseFirestore.getInstance().collection("user")
                      .document(authResult.getUser().getUid())
                      .set(user)
                      .addOnSuccessListener(aVoid -> presenter.onSuccess(authResult.getUser()))
                      .addOnCompleteListener(task -> presenter.onComplete());
            })
            .addOnFailureListener(e -> {
              presenter.onError(e.getMessage());
              presenter.onComplete();
            });
  }

  @Override
  public void addPhoto(Uri uri, Presenter presenter) {
    String uid = FirebaseAuth.getInstance().getUid();
    if (uri == null || uid == null || uri.getLastPathSegment() == null)
      return;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference imgRef = storageRef.child("images/")
            .child(FirebaseAuth.getInstance().getUid())
            .child(uri.getLastPathSegment());

    imgRef.putFile(uri)
            .addOnSuccessListener(taskSnapshot -> {
              long totalByteCount = taskSnapshot.getTotalByteCount();
              Log.i("Teste", "File uploaded size: " + totalByteCount);

              imgRef.getDownloadUrl()
                      .addOnSuccessListener(uriResponse -> {
                        DocumentReference docUser = FirebaseFirestore.getInstance().collection("user").document(uid);
                        docUser.get()
                                .addOnSuccessListener(documentSnapshot -> {
                                  User user = documentSnapshot.toObject(User.class);
                                  user.setPhotoUrl(uriResponse.toString());

                                  docUser.set(user)
                                          .addOnSuccessListener(aVoid -> {
                                            presenter.onSuccess(true);
                                            presenter.onComplete();
                                          });
                                });
                      });
            });
  }

}
