package com.example.medocare.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.medocare.First_Fragment;
import com.example.medocare.R;
import com.example.medocare.Second_Fragment;
import com.example.medocare.SetReminders;


public class HomeFragment extends Fragment {

    //First_Fragment fragment;
    //Second_Fragment secfrag;
   private TextView viewremider,viewprescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
/*
        fragment = new First_Fragment();
        secfrag = new Second_Fragment();
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragment);
        fragmentTransaction.add(R.id.fragment2, secfrag);
        fragmentTransaction.commit();

 */
        viewprescription=(TextView)root.findViewById(R.id.viewprescrip);
        viewremider=(TextView) root.findViewById(R.id.viewreminder);
        viewprescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"switch to all prescriptions",Toast.LENGTH_LONG).show();
            }
        });
        viewremider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), SetReminders.class);
                startActivity(i);

            }
        });

        return root;
    }
}