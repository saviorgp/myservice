package com.myservice.model.component;

import com.myservice.exceptions.LoginException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class WebServiceWrapper {

    private static String SERVER_URL = "http://myservice-cecode.rhcloud.com/api/";
    
    private static String CREATE_LOGIN = "signup";

    private static String AUTHENTICATE = "authenticate";

    private final static int LOGIN_FAILED_HTTP_CODE = 422;

    private final static int CREATE_LOGIN_FAILED_HTTP_CODE = 409;

    private final static String ERROR_REASON_DESC = "description";

    private WebServiceWrapper(){

    }

    public static void doCreateLogin(UserVO user) throws Throwable {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("name", user.getName());
            json.put("last_name", user.getLastName());
            json.put("email", user.getEmail());
            json.put("password", user.getPassword());
            json.put("city", user.getCity());
            json.put("state", user.getState());
            json.put("email", user.getEmail());
            json.put("phone", user.getPhone());
            json.put("neighborhood", user.getAddress());

            HttpPost post = new HttpPost(SERVER_URL + CREATE_LOGIN);
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");

            System.out.println(json);

            StringEntity se = new StringEntity(json.toString());

            post.setEntity(se);
            response = client.execute(post);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                    System.out.println(output);
                }
            }

            if(response.getStatusLine().getStatusCode() == CREATE_LOGIN_FAILED_HTTP_CODE){
                // TODO impl. tratamento de erro.
                json = new JSONObject(output.toString());
                throw new Exception(json.getString(ERROR_REASON_DESC));
            }

        } catch(Throwable t) {
            throw t;
        }
    }


    public static void doAuthenticate(UserVO user) throws Exception {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("email", user.getEmail());
            json.put("password", user.getPassword());

            HttpPost post = new HttpPost(SERVER_URL + AUTHENTICATE);
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");

            System.out.println(json);

            StringEntity se = new StringEntity(json.toString());

            post.setEntity(se);
            response = client.execute(post);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                    System.out.println(output);
                }
            }

            if(response.getStatusLine().getStatusCode() == LOGIN_FAILED_HTTP_CODE){
                json = new JSONObject(output.toString());
                throw new LoginException(json.getString(ERROR_REASON_DESC));
            }

        } catch(Throwable t) {
           throw t;
        }
    }

    public static JSONObject doGet(){
        JSONObject result  = new JSONObject();
        try {

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            HttpGet get = new HttpGet(SERVER_URL + "lastPosition");
            get.addHeader(HTTP.CONTENT_TYPE, "application/json");

            response = client.execute(get);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                    System.out.println(output);
                }
            }

            result = new JSONObject(output.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}