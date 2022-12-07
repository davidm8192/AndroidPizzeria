package com.example.androidpizzeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllToppingsAdapter extends RecyclerView.Adapter<AllToppingsAdapter.AllToppingsViewHolder> {
    private ArrayList<AllToppings> mAllToppingsList;
    
    public static class AllToppingsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public AllToppingsViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);
        }
    }

    public AllToppingsAdapter(ArrayList<AllToppings> AllToppingsList) {
        mAllToppingsList = AllToppingsList;
    }
    
    @Override
    public AllToppingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_toppings, parent, false);
        AllToppingsViewHolder output = new AllToppingsViewHolder(v);
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
