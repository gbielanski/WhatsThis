package com.bielanski.whatsthis.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.database.data.WikiEntity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class WikiAdapter extends RecyclerView.Adapter<WikiAdapter.ViewHolder> {
    public static final String TAG = "WikiAdapter";

    private List<WikiEntity> mWikiList;
    private OnClickWikiHandler mClickHandler;

    public String getWikiAtPosition(int adapterPosition) {
        return mWikiList.get(adapterPosition).getTitle();
    }

    public interface OnClickWikiHandler{
        void wikiOnClick(int position);
    }
    public WikiAdapter(List<WikiEntity> wikiList, OnClickWikiHandler clickHandler) {
        this.mWikiList = wikiList;
        this.mClickHandler = clickHandler;
    }

    public void addWikiList(List<WikiEntity> wikiList){
        this.mWikiList = wikiList;
        notifyDataSetChanged();
    }

    public List<WikiEntity> getWikiList(){
        return mWikiList;
    }


    @NonNull
    @Override
    public WikiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wiki_list_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WikiAdapter.ViewHolder holder, int position) {
        final WikiEntity wikiEntity = mWikiList.get(position);
        holder.title.setText(wikiEntity.getTitle());
        holder.description.setText(wikiEntity.getDescription());
        Bitmap bitmap = BitmapFactory.decodeByteArray(wikiEntity.getImage(), 0, wikiEntity.getImage().length);
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mWikiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.wiki_item_title) TextView title;
        @BindView(R.id.wiki_item_desc) TextView description;
        @BindView(R.id.wiki_item_image) ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.wikiOnClick(getAdapterPosition());
        }
    }
}
