package h4213.smart.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import h4213.smart.R;
import h4213.smart.models.Interests;

public class RecyclerViewAdapterSubCategory extends RecyclerView.Adapter<RecyclerViewAdapterSubCategory.ViewHolderSubCategory>{

    private static final String TAG = "RecyclerViewAdapterSubC";

    private ArrayList<String> subCategoryNames;
    private Context mContext;
    private RecyclerViewAdapterSubCategory adapter;
    private Typeface font ;
    private Interests myInterests;

    public RecyclerViewAdapterSubCategory(ArrayList<String> subCategoryName, Context context){
        this.subCategoryNames = subCategoryName;
        this.mContext = context;
        font = Typeface.createFromAsset(mContext.getAssets(), "Croissant_One.ttf");
        myInterests = Interests.getInstance();
    }

    @NonNull
    @Override
    public ViewHolderSubCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutsubcategory, parent, false);
        ViewHolderSubCategory holder = new ViewHolderSubCategory(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderSubCategory holder, int position) {
        holder.subCategoryName.setText("  "+ subCategoryNames.get(position)+ "  ");
        holder.subCategoryName.setTypeface(font);
        holder.subCategoryName.setTextColor(Color.parseColor("#CDCDCD"));

        // init the subcategories already selected in previous uses
        String anInterest = subCategoryNames.get(position);
        if(myInterests.getInterests().size() > 0 && myInterests.containInteret(anInterest)){
            holder.subCategoryName.setBackgroundResource(R.drawable.button_subcategory_selected);
            holder.subCategoryName.setTextColor(Color.WHITE);
        }

        holder.subCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // add to selection and change background
                String subCategory = (String) holder.subCategoryName.getText();
                Log.d(TAG, "we have clicked on an element");

                if(!myInterests.containInteret(subCategory)){
                    myInterests.addInterest(subCategory);
                    holder.subCategoryName.setBackgroundResource(R.drawable.button_subcategory_selected);
                    holder.subCategoryName.setTextColor(Color.WHITE);

                } else {
                    myInterests.removeInteret(subCategory);
                    holder.subCategoryName.setBackgroundResource(R.drawable.button_subcategory);
                    holder.subCategoryName.setTextColor(Color.parseColor("#CDCDCD"));

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryNames.size();
    }

    public class ViewHolderSubCategory extends RecyclerView.ViewHolder{

        Button subCategoryName;
        ConstraintLayout parentLayout;

        public ViewHolderSubCategory(@NonNull View itemView) {
            super(itemView);
            subCategoryName = itemView.findViewById(R.id.buttonSubCat);
            parentLayout = itemView.findViewById(R.id.SubCategoryLayout);
        }
    }
}
