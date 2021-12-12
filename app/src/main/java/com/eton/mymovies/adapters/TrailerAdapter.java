package com.eton.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eton.mymovies.R;
import com.eton.mymovies.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TraileriewHolder> {

    private ArrayList<Trailer> trailers;


    @NonNull
    @Override
    public TraileriewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        return new TraileriewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraileriewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.textViewOfVideo.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    private OnTrailerClickListener onTrailerClickListener;
    public interface OnTrailerClickListener{
        void onTrailerClick(String url);
    }

    class TraileriewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOfVideo;
        public TraileriewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOfVideo = itemView.findViewById(R.id.textViewNameOfVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onTrailerClickListener != null){
                        onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }
}
