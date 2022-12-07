package com.example.androidpizzeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllToppingsAdapter extends RecyclerView.Adapter<AllToppingsAdapter.AllToppingsViewHolder> {
    private ArrayList<AllToppings> mAllToppingsList;
    private OnItemClickListener mListener;
    
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    
    public static class AllToppingsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public AllToppingsViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AllToppingsAdapter(ArrayList<AllToppings> AllToppingsList) {
        mAllToppingsList = AllToppingsList;
    }
    
    @Override
    public AllToppingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_toppings, parent, false);
        AllToppingsViewHolder output = new AllToppingsViewHolder(v, mListener);
        return output;
    }

    @Override
    public void onBindViewHolder(AllToppingsViewHolder holder, int position) {
        AllToppings currentTopping = mAllToppingsList.get(position);
        holder.mTextView.setText(currentTopping.getText());
    }

    @Override
    public int getItemCount() {
        return mAllToppingsList.size();
    }

}
