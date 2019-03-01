package com.musapp.musicapp.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.model.Genre;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Genre> genres;

    public GridViewAdapter(Context mContext, List<Genre> genres) {
        this.mContext = mContext;
        this.genres = genres;
    }

    @Override
    public int getCount() {
        return genres.size();
    }

    @Override
    public Genre getItem(int position) {
        return genres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Genre genre = genres.get(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.genre_item_grid_view, null);

            ImageView imageView = convertView.findViewById(R.id.genre_image_view);
            TextView textView = convertView.findViewById(R.id.genre_label_text_view);

            final ViewHolder viewHolder = new ViewHolder(imageView, textView);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        //TODO set image of genre, have to write after Genre class creation
        //viewHolder.image.setImageResource(genre.getImageResource());
        viewHolder.name.setText(genre.getName());
        if (!genre.isChecked()){
            viewHolder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhiteTransparent));
        }else{
            viewHolder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorGenreChecked));

        }
        return convertView;
    }

    public void setItemCheck(int position, boolean value){
        genres.get(position).setChecked(value);
    }

    public List<Genre> getData(){
        return genres;
    }
    public void setData(List<Genre> list){
        genres.clear();
        genres.addAll(list);
    }

    private class ViewHolder{
        private final ImageView image;
        private final TextView name;

        public ViewHolder(ImageView image, TextView name) {
            this.image = image;
            this.name = name;
        }
    }
}

