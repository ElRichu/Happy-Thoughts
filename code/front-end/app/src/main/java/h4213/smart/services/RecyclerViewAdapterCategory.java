package h4213.smart.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import h4213.smart.R;


public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.ViewHolder>{

    private ArrayList<String> categoryNames;
    private ArrayList<ArrayList<String>> listSubCategories;
    private RecyclerViewAdapterSubCategory adapter;
    private Typeface font ;
    private Context mContext;


    public RecyclerViewAdapterCategory(ArrayList<String> categoryNames, ArrayList<ArrayList<String>> subCategorynames, Context mContext){
        this.categoryNames = categoryNames;
        this.listSubCategories = subCategorynames;
        this.mContext = mContext;
        font = Typeface.createFromAsset(mContext.getAssets(), "Croissant_One.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcategory, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        // display text
        holder.categoryName.setText(categoryNames.get(position));
        holder.categoryName.setTypeface(font);
        holder.categoryName.setTextColor(Color.parseColor("#808080"));
        holder.categoryName.setTextSize(20);

        // start new recycler
        adapter = new RecyclerViewAdapterSubCategory( this.listSubCategories.get(position), mContext);
        holder.RecyclersubCategoryNames.setLayoutManager(new GridLayoutManager(mContext, 2));
        holder.RecyclersubCategoryNames.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        RecyclerView RecyclersubCategoryNames;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            RecyclersubCategoryNames = itemView.findViewById(R.id.recyclerViewSubCategory);
            parentLayout = itemView.findViewById(R.id.CategoryLayout);
        }
    }
}
