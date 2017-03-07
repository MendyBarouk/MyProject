package com.firstproject.mendy.myproject.controller.fragmentregister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.controller.AddBusinessActivity;
import com.firstproject.mendy.myproject.model.entities.TypeOfBusiness;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText nameCompanyEditText;
    private TypeOfBusiness typeOfCompany;
    private ImageView logoImageView;


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri logoUri;
    private String logoName;


    public static final String TAG = "SecondFragment";
    OnSecondFragmentInteractionListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSecondFragmentInteractionListener) {
            mListener = (OnSecondFragmentInteractionListener) context;
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
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner descSpinner = (Spinner) getView().findViewById(R.id.fragment_second_typeOfCompany_spinner);
        nameCompanyEditText = (EditText) getView().findViewById(R.id.fragment_second_nameOfCompany_textView);

        List<String> desc = new ArrayList<>();
        desc.add("Type of company");
        for (TypeOfBusiness d : TypeOfBusiness.values()) {
            desc.add(d.toString());
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, desc);
        descSpinner.setAdapter(adapter);
        descSpinner.setSelection(0);

        descSpinner.setOnItemSelectedListener(this);

        logoImageView = (ImageView) getView().findViewById(R.id.fragment_second_logo_imageView);
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Logo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // TODO: 12/02/2017
                            getFileUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, logoUri);
                            getActivity().startActivityForResult(intent, SecondFragment.REQUEST_CAMERA);

                        } else if (items[which].equals("Choose from Library")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            // TODO: 12/02/2017

                            startActivityForResult(intent, SecondFragment.SELECT_FILE);

                        } else if (items[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        getView().findViewById(R.id.fragment_second_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && Validation.validateSecondFragment(nameCompanyEditText, typeOfCompany))
                    mListener.moveToThirdFragment(nameCompanyEditText.getText().toString(), typeOfCompany.toString(), logoUri, logoName);
            }
        });

        Button previousButton = (Button) getView().findViewById(R.id.fragment_second_previous_button);

        if (getActivity() instanceof AddBusinessActivity)
            previousButton.setVisibility(View.GONE);
        else {
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.previousFragment(FirstFragment.TAG);
                }
            });
        }
    }

    private void getFileUri() {
        logoName = System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + logoName);
        logoUri = Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                logoImageView.setImageURI(logoUri);
            } else if (requestCode == 2) {
                logoUri = Uri.parse(getRealPathFromURI(data.getData()));
                logoImageView.setImageURI(logoUri);
            }
        }
    }

    private String getRealPathFromURI(Uri data) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(data,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO...
        if (position > 0)
            typeOfCompany = TypeOfBusiness.values()[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSecondFragmentInteractionListener {
        void moveToThirdFragment(String nameCompagny, String typeOfCompany, Uri logoUri, String logoName);

        void previousFragment(String tag);
    }
}
