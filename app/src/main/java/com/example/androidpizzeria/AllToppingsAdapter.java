package com.example.androidpizzeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * AllToppings adapter for the recycler view. Extends the RecyclerView.Adapter for AllToppings. Allows the user
 * to interact with boxes in the recycler view.
 *
 * @author David Ma, Ethan Kwok
 */
public class AllToppingsAdapter extends RecyclerView.Adapter<AllToppingsAdapter.AllToppingsViewHolder> {
    private ArrayList<AllToppings> mAllToppingsList;
    private OnItemClickListener mListener;

    /**
     * Interface for OnItemClickListener
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Defines when an item/box in the recycler view is clicked
     * @param listener item clicks OnItemClickListener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * ViewHolder for AllToppings Adapter.
     */
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

    /**
     * Constructor for AllToppingsAdapter. Sets the AllToppingsList from the inputted ArrayList.
     * @param AllToppingsList ArrayList of AllToppings to put into mAllToppingsList.
     */
    public AllToppingsAdapter(ArrayList<AllToppings> AllToppingsList) {
        mAllToppingsList = AllToppingsList;
    }

    /**
     * OnCreate method that defines the ViewHolder for AllToppings.
     * @param parent ViewGroup for onCreate method
     * @param viewType int representing the viewType
     * @return AllToppingsViewHolder
     */
    @Override
    public AllToppingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_toppings, parent, false);
        AllToppingsViewHolder output = new AllToppingsViewHolder(v, mListener);
        return output;
    }

    /**
     * Sets the view based on the selected box in the recycler view. Matches it to the recycler view events.
     * @param holder AllToppingsViewHolder
     * @param position int representing the index of the selected box in the array list.
     */
    @Override
    public void onBindViewHolder(AllToppingsViewHolder holder, int position) {
        AllToppings currentTopping = mAllToppingsList.get(position);
        holder.mTextView.setText(currentTopping.getText());
    }

    /**
     * Getter method to find the size of mAllToppingsList array list.
     * @return int representing the size.
     */
    @Override
    public int getItemCount() {
        return mAllToppingsList.size();
    }

}
