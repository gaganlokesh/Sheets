package com.example.android.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gagan Lokesh on 14-01-2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static List<News> mNewsData;
    private Context mContext;
    private int lastPostiton = -1;

     NewsAdapter(Context context, ArrayList<News> news) {

         mNewsData= news;
         mContext = context;

    }
    void clear(){
         int size = mNewsData.size();
         mNewsData.clear();
         notifyItemRangeRemoved(0, size);
//        notifyDataSetChanged();
    }

    void addAll(List<News> news){
        int size = news.size();
        mNewsData.addAll(news);
        notifyItemRangeInserted(0, size);
        notifyDataSetChanged();
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        News currentNews = mNewsData.get(position);

        String newsTitle = currentNews.getContentTitle();

        if(newsTitle.startsWith("Medical News Today:")){
            newsTitle = newsTitle.replace("Medical News Today: ", "");
        }
        holder.titleTextView.setText(newsTitle);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        // load image into image view using Glide

        Glide.with(mContext).load(currentNews.getImageUrl())
                .thumbnail(0.1f)
                .apply(options)
                .into(holder.imageView);


        if(position > lastPostiton){
            lastPostiton = position;
        }

    }

    @Override
    public int getItemCount() {
        return mNewsData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

     class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {


        TextView titleTextView;
        ImageView imageView;


        NewsViewHolder (View itemView){

            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.newsImage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            News currentNews = mNewsData.get(getAdapterPosition());

            String title = currentNews.getContentTitle();

            String content = currentNews.getContent();

            String image = currentNews.getImageUrl();

            String url = currentNews.getUrl();

            String dateObject = currentNews.getDate();

            Intent intent = new Intent(mContext, DetailActivity.class);

            intent.putExtra(Constants.INTENT_TITLE, title);
            intent.putExtra(Constants.INTENT_DESCRIPTION, content);
            intent.putExtra(Constants.INTENT_IMAGE_URL, image);
            intent.putExtra(Constants.INTENT_URL, url);
            intent.putExtra(Constants.INTENT_DATE, dateObject);

            mContext.startActivity(intent);
            }


        }
    }


