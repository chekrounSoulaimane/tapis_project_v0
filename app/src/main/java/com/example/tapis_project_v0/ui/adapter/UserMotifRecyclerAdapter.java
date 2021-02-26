package com.example.tapis_project_v0.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.databinding.ItemCardBinding;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.ui.fragment.AjouterMotifFragment;
import com.example.tapis_project_v0.ui.listner.UserMotifsClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserMotifRecyclerAdapter extends RecyclerView.Adapter<UserMotifRecyclerAdapter._ViewHolder> {

    Context mContext;
    private List<UserMotif> mData;
    private UserMotifsClickListener userMotifsClickListener;

    public UserMotifRecyclerAdapter(Context mContext, List<UserMotif> mData, UserMotifsClickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.userMotifsClickListener = listener;
    }

    @NonNull
    @Override
    public UserMotifRecyclerAdapter._ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card,viewGroup,false);
//        return new _ViewHolder(view);

        LayoutInflater layoutInflater= LayoutInflater.from(viewGroup.getContext());
        ItemCardBinding itemCardBinding = ItemCardBinding.inflate(layoutInflater,viewGroup,false);
        return new _ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserMotifRecyclerAdapter._ViewHolder viewHolder, final int i) {
//        viewHolder.mItem = mData.get(i);
        final int pos = viewHolder.getAdapterPosition();
        //Set ViewTag
        viewHolder.itemView.setTag(pos);

        viewHolder.setPostImage(mData.get(i));

//        viewHolder.itemCardBinding.stagItemCourse.setText(mData.get(i).getCourseTitle());
//        viewHolder.itemCardBinding.stagItemQuantityCourse.setText(mData.get(i).getQuantityCourses());

        //1st intent 2 methods
//        if (i%2==1){
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.card_item.getLayoutParams();
//            cardViewMarginParams.setMargins(dpToPx(8), dpToPx(16), 0, 0);
//            viewHolder.card_item.requestLayout();
//        }
//        if (i==1){
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.card_item.getLayoutParams();
//            cardViewMarginParams.setMargins(dpToPx(8), dpToPx(32), 0, 0);
//            viewHolder.card_item.requestLayout();
//        }

        //2nd intent card only bottom margin in xml  and only top margin in adapter- it works
        if (i%2==1){

            int dimenTopPixeles = getDimensionValuePixels(R.dimen.staggedmarginbottom);
            int dimenleftPixeles = getDimensionValuePixels(R.dimen.horizontal_card);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.itemCardBinding.cardItem.getLayoutParams();
//            cardViewMarginParams.setMargins(dpToPx(8), dpToPx(20), 0, 0);
            cardViewMarginParams.setMargins(dimenleftPixeles, dimenTopPixeles, 0, 0);
            viewHolder.itemCardBinding.cardItem.requestLayout();
        }

//      viewHolder.card_item.setBackgroundColor(mContext.getResources().getColor(R.color.color1));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userMotifsClickListener.onDashboardCourseClick(mData.get(i), viewHolder.itemCardBinding.cardViewImage);
//                Toast.makeText(mContext, mData.get(i).getMotif().getLibelle(), Toast.LENGTH_LONG).show();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new AjouterMotifFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, myFragment).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable("userMotif", mData.get(i));
                myFragment.setArguments(bundle);
            }
        });
    }

    public int getDimensionValuePixels(int dimension)
    {
        return (int) mContext.getResources().getDimension(dimension);
    }


    public int dpToPx(int dp)
    {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public long getItemId(int position) {
        UserMotif courseCard = mData.get(position);
        return courseCard.getId();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class _ViewHolder extends RecyclerView.ViewHolder{
//        ImageView imageView;
//        TextView course;
//        TextView quantity_courses;
//        CardView card_item;
//        public UserMotif mItem;
//        public _ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            card_item = itemView.findViewById(R.id.card_item);
//            imageView = itemView.findViewById(R.id.card_view_image);
//            course = itemView.findViewById(R.id.stag_item_course);
//            quantity_courses = itemView.findViewById(R.id.stag_item_quantity_course);
//        }

        ItemCardBinding itemCardBinding;
        public _ViewHolder(@NonNull ItemCardBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;

            //this.itemRecyclerMealBinding.
        }

        void setPostImage(UserMotif userMotif){
            Picasso.get().load(userMotif.getFileUrl()).into(this.itemCardBinding.cardViewImage);
            this.itemCardBinding.stagItemCourse.setText(userMotif.getMotif().getLibelle());
            this.itemCardBinding.stagItemQuantityCourse.setText(userMotif.getMotif().getDescription());
        }

    }
}
