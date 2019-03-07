package com.musapp.musicapp.currentinformation;

import com.musapp.musicapp.model.User;

public final  class CurrentUser {
    private CurrentUser(){}

    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
       currentUser = user;
    }
}
