package com.musapp.musicapp.pattern;

import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.ImagePostViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.MusicPostViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.VideoPostViewHolder;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.uploads.ImageUpload;
import com.musapp.musicapp.uploads.MusicUpload;
import com.musapp.musicapp.uploads.VideoUpload;

public final class  UploadsAdapterFactory {
    private UploadsAdapterFactory(){}

    public static <T extends BaseUpload, Y extends BasePostViewHolder> BaseUploadsAdapter<T, Y> setAdapterTypeByInputType(PostUploadType type){
       switch (type){
           case IMAGE :
               return (BaseUploadsAdapter<T, Y>) new BaseUploadsAdapter<ImageUpload , ImagePostViewHolder>();
           case VIDEO:
               return (BaseUploadsAdapter<T, Y>) new BaseUploadsAdapter<VideoUpload, VideoPostViewHolder>();
           case MUSIC:
               return (BaseUploadsAdapter<T, Y>) new BaseUploadsAdapter<MusicUpload, MusicPostViewHolder>();
           default:
               return (BaseUploadsAdapter<T, Y>) new BaseUploadsAdapter<BaseUpload, BasePostViewHolder>();
       }
}

}
