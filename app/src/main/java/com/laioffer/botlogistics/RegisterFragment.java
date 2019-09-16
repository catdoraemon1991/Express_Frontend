package com.laioffer.botlogistics;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.laioffer.entity.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends OnBoardingBaseFragment {

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        submitButton.setText(getString(R.string.register));
        // register the account to firebase
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
//                final String email = emailEditText.getText().toString();
                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username)) {
                            Toast.makeText(getContext(), "Username taken, try another one",
                                    Toast.LENGTH_LONG).show();
                        } else if (!username.isEmpty() && !password.isEmpty()) {
                            final User user = new User();
                            user.setUser_account(username);
                            user.setUser_password(Utils.md5Encryption(password));
                            user.setUser_timestamp(System.currentTimeMillis());
//                            user.setUser_email(email);
                            database.child("user").child(user.getUser_account()).setValue(user);
                            Toast.makeText(getContext(), "Signed up successfully",
                                    Toast.LENGTH_LONG).show();
                            goToLogin();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;

    }

    private void goToLogin() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            ((OnBoardingActivity)activity).setCurrentPage(0);
        }
    }



    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }


}
