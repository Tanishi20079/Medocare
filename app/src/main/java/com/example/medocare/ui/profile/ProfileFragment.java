package com.example.medocare.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.medocare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView name,email;
    private String userid;
    String user,mail;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
          name=(TextView) root.findViewById(R.id.prof_name);
          email=(TextView) root.findViewById(R.id.prof_email);
          userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
          DatabaseReference reff= FirebaseDatabase.getInstance().getReference("Users").child(userid);
          reff.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  user=snapshot.child("name").getValue().toString();
                  mail=snapshot.child("email").getValue().toString();
                  name.setText(user);
                  email.setText(mail);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });


        return root;
    }
}