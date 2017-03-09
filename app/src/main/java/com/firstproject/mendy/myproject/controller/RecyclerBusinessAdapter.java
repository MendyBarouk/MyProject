package com.firstproject.mendy.myproject.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.backend.PHPContract;
import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mendy on 15/01/2017.
 */

class RecyclerBusinessAdapter extends RecyclerView.Adapter<RecyclerBusinessAdapter.ViewHolderBusiness> {

    static final int BACK_FROM_DETAIL_ACTIVITY = 3;
    static final String POSITION = "position";

    private List<Business> businessList;
    private Activity activity;

    RecyclerBusinessAdapter(Activity activity){
        try {
            this.businessList = User.getInstance().getListBusiness();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.activity = activity;
    }

    @Override
    public ViewHolderBusiness onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business, parent, false);
        return new ViewHolderBusiness(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderBusiness holder, int position) {
        final Business business = businessList.get(position);
        Geocoder geocoder;
        Address address = null;
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(business.getLatitude(), business.getLongitude(), 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address != null) {
            holder.addressOfCompany.setText(address.getCountryName() + ", " + address.getLocality());
        }
        holder.nameOfCompany.setText(business.getName());
        holder.typeOfCompany.setText(business.getType());
        holder.logoImageView.setImageBitmap(byteArrayToBitmap(business.getLogoBitmap()));
        holder.infoOfBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailBusinessActivity.class);
                intent.putExtra(POSITION, holder.getAdapterPosition());
                intent.putExtra(PHPContract.BusinessContract.BUSINESS_TABLE, business);
                activity.startActivityForResult(intent, BACK_FROM_DETAIL_ACTIVITY);
            }
        });
        holder.position = holder.getAdapterPosition();
    }

    private static Bitmap byteArrayToBitmap(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(is);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }




    class ViewHolderBusiness extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameOfCompany;
        private TextView addressOfCompany;
        private TextView infoOfBusiness;
        private TextView typeOfCompany;
        private ImageView logoImageView;

        int position;
        private ViewHolderBusiness(View itemView) {
            super(itemView);

            nameOfCompany = (TextView) itemView.findViewById(R.id.item_business_name_textView);
            addressOfCompany = (TextView) itemView.findViewById(R.id.item_business_address_textView);
            infoOfBusiness = (TextView) itemView.findViewById(R.id.item_business_info_textView);
            typeOfCompany = (TextView) itemView.findViewById(R.id.item_business_typeCompany_textView);
            logoImageView = (ImageView) itemView.findViewById(R.id.item_business_logo_imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RecyclerBusinessAdapter.this.activity, ListBusinessActivityActivity.class);
            intent.putExtra(PHPContract.BusinessContract.ID, businessList.get(position).getId());
            RecyclerBusinessAdapter.this.activity.startActivity(intent);
        }
    }
}
