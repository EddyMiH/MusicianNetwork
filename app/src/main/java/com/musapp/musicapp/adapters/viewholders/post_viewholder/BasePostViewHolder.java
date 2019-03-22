package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;

public class BasePostViewHolder extends RecyclerView.ViewHolder{

    protected OnViewHolderClickListner mViewHolderClickListner;

    public BasePostViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setOnViewHolderClickListner(OnViewHolderClickListner listener){
        mViewHolderClickListner = listener;
    }

    public interface OnViewHolderClickListner{
        void onItemClicked(int position);
    }
}
