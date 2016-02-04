package com.myservice.helper;


import android.content.Context;

import com.myservice.model.Preferences;
import com.myservice.model.component.UserVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.utils.Constants;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class WebServiceHelper {

    private WebServiceHelper(){
    }

    public static void authenticateUser(UserVO userVO, Context ctx) throws Throwable {
        String authToken = WebServiceWrapper.doAuthenticate(userVO);
        Preferences.getPreferences(ctx).editPreference(Constants.SESSION_TOKEN, authToken);
    }

    public static void signupUser(UserVO userVO) throws Throwable {
        WebServiceWrapper.doCreateLogin(userVO);
    }

    public static String resetPassword(String email) throws Throwable {
        return WebServiceWrapper.doResetPassword(email);
    }

    public static boolean changePassword(String passwd, Context ctx) throws Throwable {
        String token = 
              (String) Preferences.getPreferences(ctx).getSharedPreference(Constants.SESSION_TOKEN);
        return WebServiceWrapper.doChangePassword(passwd,token);
    }
}