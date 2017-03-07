package com.firstproject.mendy.myproject.controller.fragmentregister;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.controller.AddBusinessActivity;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnThirdFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ThirdFragment extends Fragment implements AdapterView.OnItemClickListener {

    private EditText emailEditText, phoneEditText, linkEditText;
    private AutoCompleteTextView location;

    private GooglePlaceAutoCompleteAdapter adapter;
    private LatLng latLng;

    public static final String TAG = "ThirdFragment";
    private OnThirdFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnThirdFragmentInteractionListener) {
            mListener = (OnThirdFragmentInteractionListener) context;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViewById();
    }

    private void findViewById() {
        this.location = (AutoCompleteTextView) getView().findViewById(R.id.fragment_third_loaction_editText);
        this.emailEditText = (EditText) getView().findViewById(R.id.fragment_third_email_editText);
        this.phoneEditText = (EditText) getView().findViewById(R.id.fragment_third_phone_editText);
        this.linkEditText = (EditText) getView().findViewById(R.id.fragment_third_link_editText);

        adapter = new GooglePlaceAutoCompleteAdapter(this.getActivity());

        location.setAdapter(adapter);
        location.setOnItemClickListener(this);

        getView().findViewById(R.id.fragment_third_previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.previousFragment(SecondFragment.TAG);
            }
        });

        Button signUpButton = (Button) getView().findViewById(R.id.fragment_third_signUp_button);
        if (getActivity() instanceof AddBusinessActivity)
            signUpButton.setText("Add Business");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && Validation.validateThirdFragment(location, emailEditText, phoneEditText, linkEditText)){
                        mListener.registerFinish(
                                latLng.longitude,
                                latLng.latitude,
                                phoneEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                "http://" + linkEditText.getText().toString()
                        );
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String address = (String) parent.getItemAtPosition(position);
        latLng = adapter.setLocationFromAddress(address);
    }




    public interface OnThirdFragmentInteractionListener {
        void registerFinish(double longitude, double latitude, String phone, String email, String link);

        void previousFragment(String tag);
    }

}
