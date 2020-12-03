package com.example.covidtracker.ui.exposure;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.R;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView infoItemView;

        ViewHolder(View item){
            super(item);
            infoItemView = item.findViewById(R.id.textView);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){

        }

        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
