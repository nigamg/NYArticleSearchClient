package com.gn.demo.nyarticlesearchclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gn.demo.nyarticlesearchclient.R;
import com.gn.demo.nyarticlesearchclient.activity.ArticleActivity;
import com.gn.demo.nyarticlesearchclient.model.NYArticle;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by workboard on 2/12/16.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridView> {

    private Context context;
    private ArrayList<NYArticle> articles;


    public GridAdapter(Context context, ArrayList<NYArticle> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public GridView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        GridView gridView = new GridView(layoutView);
        return gridView;
    }

    @Override
    public void onBindViewHolder(GridView holder, final int position) {
        NYArticle nyArticle = this.articles.get(position);
        //Log.i("DEBUG", " content" + nyArticle.getHeadLine());

        holder.imageView.setImageResource(0);
        if(nyArticle.getThumbNail() != null && nyArticle.getThumbNail().trim().length() != 0){
            Picasso.with(context).load(nyArticle.getThumbNail()).into(holder.imageView);
        }
        holder.textView.setText(nyArticle.getHeadLine());

        // animate
        animate(holder);

        /**
         * attach grid item click event
         */
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context, ArticleActivity.class);
                NYArticle article = articles.get(position);
                i.putExtra("article", article);
                context.startActivity(i);
            }
        });
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }


    public static class GridView extends RecyclerView.ViewHolder {
        CardView cv;
        public ImageView imageView;
        public TextView textView;

        public GridView(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.img_name);

        }
    }
}
