package h4213.smart.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import h4213.smart.R;
import h4213.smart.models.Tweets;

public class RecyclerViewAdapterTweet extends RecyclerView.Adapter<RecyclerViewAdapterTweet.ViewHolder>{
    private ArrayList<Tweets> listTweets;
    private Context mContext;
    private RecyclerViewAdapterImageTweet adapter;

    public RecyclerViewAdapterTweet(ArrayList<Tweets> listTweets, Context mContext){
        this.listTweets = listTweets;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layouttweets, parent, false);
        RecyclerViewAdapterTweet.ViewHolder holder = new RecyclerViewAdapterTweet.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText("@"+listTweets.get(position).getUserName());
        holder.tweetText.setText(listTweets.get(position).getText());
        holder.date.setText(listTweets.get(position).getDate());

        if(!listTweets.get(position).getImages().isEmpty()){
            // start new recycler
            adapter = new RecyclerViewAdapterImageTweet( listTweets.get(position).getImages(), mContext);
            holder.images.setLayoutManager(new LinearLayoutManager(mContext));
            holder.images.setAdapter(adapter);
        } else {
            holder.images.setVisibility(View.GONE);
        }

        if(listTweets.get(position).getScore().equals("1")){
            holder.tweetScore.setText("+");
        } else {
            holder.tweetScore.setText("++");
        }
    }

    @Override
    public int getItemCount() {
        return listTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView tweetText;
        ConstraintLayout parentLayout;
        TextView date ;
        TextView tweetScore;
        RecyclerView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userTweetName);
            parentLayout = itemView.findViewById(R.id.TweetLayout);
            tweetText = itemView.findViewById(R.id.tweetText);
            date = itemView.findViewById(R.id.userTweetDate);
            images = itemView.findViewById(R.id.ImageRecyclerView);
            tweetScore = itemView.findViewById(R.id.tweetScore);
        }
    }
}
