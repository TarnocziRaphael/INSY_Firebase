package at.tarnoczi.insy_firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

public class CrashFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crash, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button crash = view.findViewById(R.id.crashApp);
        crash.setOnClickListener(view1 -> {
            throw new RuntimeException("App crashed!");
        });

        Button crash2 = view.findViewById(R.id.crashApp2);
        crash2.setOnClickListener(view1 -> {
            throw new IllegalArgumentException("App crashed in another way!");
        });
    }
}