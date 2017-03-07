package com.firstproject.mendy.myproject.controller.fragmentregister;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firstproject.mendy.myproject.R;


public class FirstFragment extends Fragment {


    public static final String TAG = "FirstFragment";
    OnFirstFragmentInteractionListener mListener;

    private EditText userNameEditText;
    private EditText passwordEditText;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFirstFragmentInteractionListener) {
            mListener = (OnFirstFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNextFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        userNameEditText = (EditText) getView().findViewById(R.id.fragment_first_userName_textView);
        passwordEditText = (EditText) getView().findViewById(R.id.fragment_first_password_textView);
        getView().findViewById(R.id.fragment_first_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && Validation.validateFirstFragment(userNameEditText, passwordEditText)) {
                    mListener.moveToSecondFragment(userNameEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFirstFragmentInteractionListener {
        void moveToSecondFragment(String userName, String password);
    }

}
