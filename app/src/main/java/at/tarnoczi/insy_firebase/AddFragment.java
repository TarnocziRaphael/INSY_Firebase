package at.tarnoczi.insy_firebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class AddFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button store = view.findViewById(R.id.addItem);
        store.setOnClickListener(view1 -> {
            EditText inputName = view.findViewById(R.id.inputUsername);
            String name = inputName.getText().toString();
            EditText inputMail = view.findViewById(R.id.inputEmail);
            String email = inputMail.getText().toString();
            EditText inputPassword = view.findViewById(R.id.inputPassword);
            String password = inputPassword.getText().toString();
            saveData(view, name, email, password);
        });

    }
    private void saveData(View view, String username, String email, String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", username);
        data.put("email", email);
        data.put("password", password.hashCode());
        TextView output = view.findViewById(R.id.output);
        output.setText("");
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("User").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    output.setText("Es existiert bereits eine Person mit diesem Benutzenamen.");
                }
                else {
                    docRef.set(data);
                    Log.i("Dokument angelegt.", "Ein Dokument für den Benutzer "+username+" wurde angelegt.");
                    reset(view);
                }
             }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                output.setText("Es ist ein Fehler aufgetreten. Bitte versuche es gleich erneut nochmail.");
            }
        });
    }
    private void reset(View v) {
        EditText username = v.findViewById(R.id.inputUsername);
        username.setText("");
        EditText email = v.findViewById(R.id.inputEmail);
        email.setText("");
        EditText password = v.findViewById(R.id.inputPassword);
        password.setText("");
    }
}