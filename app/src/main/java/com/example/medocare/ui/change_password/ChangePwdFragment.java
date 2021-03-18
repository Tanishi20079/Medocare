package com.example.medocare.ui.change_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.medocare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePwdFragment extends Fragment {
    private EditText newpass,retype;
    private Button submit;
    private FirebaseUser userid;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_changepwd, container, false);
        newpass=(EditText)root.findViewById(R.id.newpassword);
        retype=(EditText)root.findViewById(R.id.retype);
        submit=(Button)root.findViewById(R.id.submitpassword);
        userid= FirebaseAuth.getInstance().getCurrentUser();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String newp=newpass.getText().toString();
               String ret=retype.getText().toString();
               if(!(newp.isEmpty()||ret.isEmpty())){
                   if(newp.equals(ret)){
                       userid.updatePassword(newp).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(getContext(),"Password changed successfully!!",Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(getContext(),"Failed to change password",Toast.LENGTH_SHORT).show();

                           }
                       });

                       }
                   else{
                       Toast.makeText(getContext(),"New Password mismatching",Toast.LENGTH_SHORT).show();
                   }
               }
               else{
                   Toast.makeText(getContext(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
               }
            }

        });
        return root;
    }
}