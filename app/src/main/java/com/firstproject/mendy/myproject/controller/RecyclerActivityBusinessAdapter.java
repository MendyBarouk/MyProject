package com.firstproject.mendy.myproject.controller;

import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.entities.BusinessActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mendy on 18/01/2017.
 */

public class RecyclerActivityBusinessAdapter extends RecyclerView.Adapter<RecyclerActivityBusinessAdapter.ViewHolderActivityBusiness> {


    public static final String POSITION = "Position";
    private List<BusinessActivity> businessActivities;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private final ViewHolderActivityBusiness.OnEditDeleteInteractionListener listener;

    public RecyclerActivityBusinessAdapter(ViewHolderActivityBusiness.OnEditDeleteInteractionListener listener, List<BusinessActivity> businessActivities){
        this.businessActivities = businessActivities;
        binderHelper.setOpenOnlyOne(true);
        this.listener = listener;
    }

    @Override
    public ViewHolderActivityBusiness onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_business, parent, false);
        return  new ViewHolderActivityBusiness(listener, view);
    }

    @Override
    public void onBindViewHolder(ViewHolderActivityBusiness holder, int position) {
        final BusinessActivity businessActivity = businessActivities.get(position);

        binderHelper.bind(holder.swipeRevealLayout, String.valueOf(businessActivity.getId()));

        holder.bind(businessActivity);

    }

    @Override
    public int getItemCount() {
        return businessActivities.size();
    }



    public static class ViewHolderActivityBusiness extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fromTextView, toTextView, verbalDescTextView, priceTextView;
        private View deleteTextView, editTextView;
        private SwipeRevealLayout swipeRevealLayout;
        private OnEditDeleteInteractionListener listener;

        public ViewHolderActivityBusiness(OnEditDeleteInteractionListener listener, View itemView) {
            super(itemView);

            fromTextView = (TextView) itemView.findViewById(R.id.item_activity_business_from_textView);
            toTextView = (TextView) itemView.findViewById(R.id.item_activity_business_to_textView);
            verbalDescTextView = (TextView) itemView.findViewById(R.id.item_activity_business_verbalDescription_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.item_activity_business_price_textView);
            deleteTextView = itemView.findViewById(R.id.item_activity_business_delete_textView);
            editTextView = itemView.findViewById(R.id.item_activity_business_edit_textView);
            swipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.item_activity_business_swipe_layout);
            this.listener = listener;

            verbalDescTextView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (((TextView) v).getText().toString().split("\n").length > 3) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }
            });
        }

        public void bind(BusinessActivity businessActivity){
            deleteTextView.setOnClickListener(this);
            editTextView.setOnClickListener(this);


            fromTextView.setText(getDate(businessActivity.getFromDate()));
            toTextView.setText(getDate(businessActivity.getToDate()));
            verbalDescTextView.setText(businessActivity.getDescription());
            priceTextView.setText("$ " + businessActivity.getPrice());
        }

        public static String getDate(long milliSeconds)
        {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            Log.d("Mendy", formatter.format(calendar.getTime()));
            return formatter.format(calendar.getTime());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_activity_business_delete_textView:
                    this.listener.onDelete(getAdapterPosition());
                    break;
                case R.id.item_activity_business_edit_textView:
                    this.listener.onEdit(getAdapterPosition());
                    break;
            }
        }

        public interface OnEditDeleteInteractionListener{
            void onDelete(int position);
            void onEdit(int position);
        }
    }
}
