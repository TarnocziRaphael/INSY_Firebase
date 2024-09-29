package at.tarnoczi.insy_firebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button1 = view.findViewById(R.id.check1);
        button1.setOnClickListener(view1 -> {
            EditText inputUser = view.findViewById(R.id.inputUsername);
            String name = inputUser.getText().toString();
            if (name == null || name.equals("")) {
                TextView outpu1 = view.findViewById(R.id.output1);
                outpu1.setText("Es muss ein Benutzername eingegeben werden.");
                return;
            }
            userRegistered(view, name);
        });

        Button button2 = view.findViewById(R.id.check2);
        button2.setOnClickListener(view1 -> {
            getAllUsers(view);
        });
    }

    private void userRegistered(View v, String username) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("User").document(username);
        TextView outpu1 = v.findViewById(R.id.output1);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    outpu1.setText("Ja, der User "+username+" ist in der Datenbank registriert.");
                }
                else {
                    outpu1.setText("Nein, der User "+username+" ist nicht in der Datenbank registriert.");
                }
            }
        });
    }

    private void getAllUsers(View v) {
        FirebaseFirestore.getInstance().collection("User")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    StringBuilder usernames = new StringBuilder();
                    for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                        String docId = document.getId();
                        usernames.append(docId).append("\n");
                    }
                    TextView output = v.findViewById(R.id.output2);
                    output.setText(usernames.toString());
                }
            });
    }
}