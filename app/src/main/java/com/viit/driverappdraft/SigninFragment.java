package com.viit.driverappdraft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninFragment extends Fragment {

    EditText mEmailEditText, mPasswordEditText;
    Button mSigninButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);
        mEmailEditText = v.findViewById(R.id.editText_loginEmail);
        mPasswordEditText = v.findViewById(R.id.editText_loginPassword);

        mSigninButton = v.findViewById(R.id.button_Login);
        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Successfully logged in to Firebase", Toast.LENGTH_SHORT).show();
                                LoginFragment loginFragment = new LoginFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment, "loginFragment Transaction").addToBackStack(null).commit();
                            } else {
                                Log.e("Firebase", "Authentication error");
                            }
                        }
                    });
                }
            }
        });

        return  v;
    }
}
