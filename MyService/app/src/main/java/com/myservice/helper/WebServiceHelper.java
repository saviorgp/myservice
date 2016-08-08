package com.myservice.helper;


import android.content.Context;

import com.myservice.model.Preferences;
import com.myservice.model.component.UserVO;
import com.myservice.model.component.WebServiceWrapper;
import com.myservice.utils.Constants;

import org.json.JSONObject;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class WebServiceHelper {

    private WebServiceHelper(){
    }

    public static void authenticateUser(UserVO userVO, Context ctx) throws Throwable {

        JSONObject user = WebServiceWrapper.doAuthenticate(userVO);

        Preferences.getPreferences(ctx).editPreference(Constants.USER_NAME, user.getString("name") + " " + user.getString("last_name"));
        Preferences.getPreferences(ctx).editPreference(Constants.EMAIL, user.getString("email"));
        Preferences.getPreferences(ctx).editPreference(Constants.SESSION_TOKEN, user.getString("token"));
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