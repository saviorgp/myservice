package com.myservice.utils;

import java.util.Random;

/**
 * Created by AlexGP on 21/01/2016.
 */
public class Utils {

    private static final String
            ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJLMNOPQRSTUVXZ";

    public static String generateRandomToken(){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(6);
        for(int i=0;i<6;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}