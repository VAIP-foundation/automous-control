package com.autonomous.pm.util;

import org.springframework.lang.Nullable;

public class StringUtil
{
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    
    /** 문자열 배열에서 문자열의 포함여부 체크 
     * @param stringlist : String[]  스트링 배열 
     * @param word : String  포함여부를 체크할 문자열
     * @return boolean true if word is contained in stringlist else false.
     */
    public static boolean contains(String[] stringlist, String word )
    {
        boolean isContained = false;
        if(word == null)
            return false;
            
        for(String str :stringlist)
        {
            if(str.compareTo(word)==0){
                isContained=true;
                break;
            }
        }

        return isContained;
    }
}