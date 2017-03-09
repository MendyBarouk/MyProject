package com.firstproject.mendy.myproject.controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;

/**
 * Created by Mendy on 09/02/2017.
 */

public class EmailDialogFragment extends DialogFragment {

    EditText emailEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_forgot_password, null);
        emailEditText = (EditText) view.findViewById(R.id.fragment_forgot_password_email_editext);


        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Find Your Account")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        new AsyncTask<String, Void, String>() {

                            ProgressDialog progressDialog;
                            @Override
                            protected void onPreExecute() {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setIndeterminate(true);
                                progressDialog.setTitle("please wait...");
                                progressDialog.setMessage("Send Message...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }

                            @Override
                            protected String doInBackground(String... strings) {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return DBManagerFactory.getManager().sendEmailForgotPassword(strings[0]);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                emailEditText.setText(s);
                                progressDialog.dismiss();
                            }
                        }.execute(emailEditText.getText().toString());

                        //Dismiss once everything is OK.
                    }
                });
            }
        });


        return dialog;
    }
}
