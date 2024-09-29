package at.tarnoczi.insy_firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StorageFragment extends Fragment {
    Uri imageUri;
    StorageReference storageReference;
    ImageView image;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image = view.findViewById(R.id.firebaseimage);
        Button select = view.findViewById(R.id.selectImage);
        select.setOnClickListener(view1 -> {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 001);
            selectImage();
        });

        Button upload = view.findViewById(R.id.uploadImage);
        upload.setOnClickListener(view1 -> {
            uploadImage();
        });
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void uploadImage() {
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Datei wird hochgeladen...");
        dialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.GERMANY);
        Date now = new Date();
        String fileName = formatter.format(now);


        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        if(imageUri == null) {
            Toast.makeText(getActivity(), "Kein Bild ausgewählt", Toast.LENGTH_LONG).show();
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            return;
        }
        storageReference.putFile(imageUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.setImageURI(null);
                    imageUri = null;
                    Toast.makeText(getActivity(), "Erfolgreich hochgeladen", Toast.LENGTH_LONG).show();
                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Fehler beim Hochladen", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data.getData() != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
}