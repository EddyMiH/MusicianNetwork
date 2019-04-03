package com.musapp.musicapp.model;

public class Message {
    private String messageText;
    private String creatorId;
    private String creationDate;
    private String creatorPic;


    public Message(){}
    public Message(String messageText, String creatorId, String creationDate, String creatorPic){
        this.messageText = messageText;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.creatorPic = creatorPic;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorPic() {
        return creatorPic;
    }

    public void setCreatorPic(String creatorPic) {
        this.creatorPic = creatorPic;
    }

}
