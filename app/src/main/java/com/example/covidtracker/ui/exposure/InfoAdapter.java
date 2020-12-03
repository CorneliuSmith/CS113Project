package com.example.covidtracker.ui.exposure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.R;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private ArrayList<Information> data;
    private LayoutInflater inflater;
    private ItemClickListener itemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView infoItemView;

        ViewHolder(View item){
            super(item);
            infoItemView = item.findViewById(R.id.rvInformation);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public InfoAdapter(Context context, ArrayList<Information> data){
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_information_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.infoItemView.setText(data.get(position).toString());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    void setClickListener(ItemClickListener itemListener){
        this.itemListener = itemListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
