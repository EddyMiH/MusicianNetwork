package com.musapp.musicapp.utils;

import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public final  class CheckUtils {
    private CheckUtils(){}
    private static final String[] mails = {"@gmail.com","@inbox.ru","@list.ru","@bk.ru", "mail.ru"};

    public static boolean checkEditTextEmpty(EditText editText){
       String checkableString =  editText.getText().toString();
       return checkableString.isEmpty() || !(checkableString.trim().length() > 0);
    }
    public static boolean checkIsntOneWord(EditText editText){
       int count = 0;
       for(String str: editText.getText().toString().split(" ")){
          if(str.trim().length() > 0)
              ++count;
          if(count > 2)
              return false;
       }
       return count == 2;
    }
    public static boolean checkIsMail(EditText editText){
        String checkableString = editText.getText().toString();
        boolean contains  = false;
        for(String str:mails){
            if(checkableString.endsWith(str)) {
                contains = true;
                break;
            }
        }
        //TODO check if contains wrong characters
        return contains && StringUtils.countMatches(checkableString, '@') == 1;

    }
    public static boolean checkEqual(EditText editText1, EditText editText2){
        return editText1.getText().toString().equals(editText2.getText().toString());
    }
}
