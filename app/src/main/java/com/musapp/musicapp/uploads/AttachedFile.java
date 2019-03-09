package com.musapp.musicapp.uploads;

import android.arch.persistence.room.Ignore;

import com.musapp.musicapp.enums.PostUploadType;

import java.util.ArrayList;
import java.util.List;

public class AttachedFile {
    @Ignore
    private String primaryKey;

    private List<String> filesUrls;
    private PostUploadType fileType;

    public AttachedFile() {
        filesUrls = new ArrayList<>();
        fileType = PostUploadType.NONE;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<String> getFilesUrls() {
        return filesUrls;
    }

    public void setFilesUrls(List<String> filesUrls) {
        this.filesUrls = filesUrls;
    }

    public void addFile(String url){
        filesUrls.add(url);
    }

    public PostUploadType getFileType() {
        return fileType;
    }

    public void setFileType(PostUploadType fileType) {
        this.fileType = fileType;
    }
}