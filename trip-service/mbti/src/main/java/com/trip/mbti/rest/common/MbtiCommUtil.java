package com.trip.mbti.rest.common;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MbtiCommUtil {

    /*
     * StringTokenizer nullchekk return
     */
    public static StringTokenizer StringTokenizerMbti(String str, String delim){
        
        StringTokenizer st = null;
        if(str != null){
            st = new StringTokenizer(str, delim);
        }

        return st;
    }

    /*
     * StringTokenizer Add List
     */
    public static void StringTokenizerAddList(StringTokenizer st, Set<String> list){
        if(st == null){
            return;
        }
        while(st.hasMoreTokens()){
            list.add(st.nextToken());
        }
    }


}
