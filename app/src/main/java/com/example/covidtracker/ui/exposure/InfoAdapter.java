package com.example.covidtracker.ui.exposure;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class InfoAdapter extends RecyclerView.Adapter{

    private ArrayList<InformationViewModel> data;
    private LayoutInflater inflater;
    private ItemClickListener itemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView infoItemView;

        ViewHolder(View item){
            super(item);
            infoItemView = (TextView) item.findViewById(R.id.info_text_view);
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }

        public void bindData(final InformationViewModel item){
            infoItemView.setText(Html.fromHtml("<b>" + item.getName()  + "</b>"));
            infoItemView.append("\n" + item.toString());
        }
    }

    public InfoAdapter(Context context, ArrayList<InformationViewModel> data){
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Objects.requireNonNull(parent).getContext()).inflate(viewType, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bindData(data.get(position));
    }


    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(final int position){
        return R.layout.recyclerview_information_item;
    }

    void setClickListener(ItemClickListener itemListener){
        this.itemListener = itemListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
