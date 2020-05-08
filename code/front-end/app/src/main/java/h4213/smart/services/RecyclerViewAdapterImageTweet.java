package h4213.smart.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import h4213.smart.R;

public class RecyclerViewAdapterImageTweet extends RecyclerView.Adapter<RecyclerViewAdapterImageTweet.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterImag";
    private ArrayList<String> imageURLS;
    private Context mcontext;

    public RecyclerViewAdapterImageTweet(ArrayList<String> imageURLS, Context mContext){
        this.imageURLS = imageURLS;
        this.mcontext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutimagestweet, parent, false);
        RecyclerViewAdapterImageTweet.ViewHolder holder = new RecyclerViewAdapterImageTweet.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.with(mcontext)
                .load(imageURLS.get(position))
                .error(R.drawable.help)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return imageURLS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            parentLayout = itemView.findViewById(R.id.ImageLayoutTweet);

        }
    }
}
