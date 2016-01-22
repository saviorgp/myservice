package com.myservice.helper;


import com.myservice.model.component.UserVO;
import com.myservice.model.component.WebServiceWrapper;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class WebServiceHelper {

    private WebServiceHelper(){
    }

    public static void authenticateUser(UserVO userVO) throws Throwable {
        WebServiceWrapper.doAuthenticate(userVO);
    }

    public static void signupUser(UserVO userVO) throws Throwable {
        WebServiceWrapper.doCreateLogin(userVO);
    }

    public static String resetPassword(String email) throws Throwable {
        return WebServiceWrapper.doResetPassword(email);
    }
}