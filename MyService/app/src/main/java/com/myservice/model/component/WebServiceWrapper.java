package com.myservice.model.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

import com.myservice.exceptions.CreateUserException;
import com.myservice.exceptions.LoginException;
import com.myservice.model.Preferences;
import com.myservice.utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class WebServiceWrapper {

    private static String SERVER_URL = "http://myservice-cecode.rhcloud.com/api/";
    
    private static String CMD_CREATE_LOGIN = "signup";

    private static String CMD_CREATE_AD = "advertisement";

    private static String CMD_AUTHENTICATE = "authenticate";

    private static String CMD_RESET_PASSWORD = "password/reset";

    private static String CMD_CHG_PASSWORD = "password/change?token=";
   
    private final static int LOGIN_FAILED_HTTP_CODE = 422;

    private final static int LOGIN_INTERNAL_ERROR_CODE = 500;

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
            json.put("email", user.getEmail().trim());
            json.put("password", user.getPassword());
            json.put("city", user.getCity());
            json.put("state", user.getState());
            json.put("phone", user.getPhone());
            json.put("neighborhood", user.getNeighborhood());
            json.put("street", user.getAddress());
            json.put("number", user.getNumber());
            json.put("complement", user.getNeighborhood());
            json.put("zip_code", user.getZipCode());

            HttpPost post = new HttpPost(SERVER_URL + CMD_CREATE_LOGIN);
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
                json = new JSONObject(output.toString());
                throw new CreateUserException(json.getString(ERROR_REASON_DESC));
            } else if(response.getStatusLine().getStatusCode() == LOGIN_INTERNAL_ERROR_CODE){
                String msg = output.toString().replace("[","");
                msg = msg.replace("]", "");
                msg = msg.replaceAll("\"", "");
                throw new CreateUserException(msg);
            }

        } catch(Throwable t) {
            throw t;
        }
    }

    public static JSONObject search(String query, Integer current_page, FilterVO filterVO) throws Exception{

        StringBuilder result = new StringBuilder();
        StringBuffer queryStr = new StringBuffer("http://myservice-cecode.rhcloud.com/api/advertisements?page=" + current_page);
        HttpURLConnection conn = null;

        if(query != null && query.length() > 0){
            queryStr.append("&query=" + query);
        }

        try {

            if(filterVO != null){

                if(filterVO.getCategoriaID() != null){
                    queryStr.append("&category_id=" + filterVO.getCategoriaID());
                }

                if(filterVO.getPrecoOrder()){
                    queryStr.append("&order_by_price=" + "asc");
                }
                else{
                    queryStr.append("&order_by_price=" + "desc");
                }

                if(filterVO.getDataOrder()){
                    queryStr.append("&created_at=" + "asc");
                }
                else{
                    queryStr.append("&created_at=" + "desc");
                }
            }


            URL url = new URL(queryStr.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == 200) {

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

                return new JSONObject(sb.toString());
            }
            else{
                Log.e("BILA", String.valueOf(conn.getResponseCode()));
            }

        }catch( Exception e) {
            e.printStackTrace();
            Log.e("WEB", e.getMessage());
        }
        finally {
            conn.disconnect();
        }


        return new JSONObject(result.toString());
    }

    public static JSONObject categories(String parent) throws Exception{

        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL url = new URL("http://myservice-cecode.rhcloud.com/api/categories");

            if(parent != null){
                url = new URL("http://myservice-cecode.rhcloud.com/api/categories?parent_id=" + parent);
            }

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == 200) {

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

                return new JSONObject(sb.toString());
            }
            else{
                Log.e("BILA", String.valueOf(conn.getResponseCode()));
            }

        }catch( Exception e) {
            e.printStackTrace();
            Log.e("WEB", e.getMessage());
        }
        finally {
            conn.disconnect();
        }


        return new JSONObject(result.toString());
    }

    public static JSONObject doAuthenticate(UserVO user) throws Exception {

        JSONObject userResponse = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("email", user.getEmail().trim());
            json.put("password", user.getPassword());

            HttpPost post = new HttpPost(SERVER_URL + CMD_AUTHENTICATE);
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
            } else if(response.getStatusLine().getStatusCode() == LOGIN_INTERNAL_ERROR_CODE){
                String msg = output.toString().replace("[","");
                msg = msg.replace("]", "");
                msg = msg.replaceAll("\"", "");
                throw new LoginException(msg);
            }

            json = new JSONObject(output.toString());
            //user = json.getJSONObject("user").getString("token");
            userResponse = json.getJSONObject("user");

        } catch(Throwable t) {
           throw t;
        }
        
        return userResponse;
    }

    public static String doResetPassword(String email) throws Exception {
        String newPasswd = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("email", email.trim());
            
            HttpPost post = new HttpPost(SERVER_URL + CMD_RESET_PASSWORD);
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

            json = new JSONObject(output.toString());
            newPasswd = json.getString("password");

        } catch(Throwable t) {
            throw t;
        }
        
        return newPasswd;
    }

    public static boolean doChangePassword(String passwd, String token) throws Exception {
        boolean result = false;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("password", passwd.trim());

            HttpPost post = new HttpPost(SERVER_URL + CMD_CHG_PASSWORD + token);
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

            json = new JSONObject(output.toString());
            result = json.getBoolean("success");

        } catch(Throwable t) {
            throw t;
        }

        return result;
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

    public static JSONObject createAd(Advertisement advertisement, Context ctx){

        String token =   (String) Preferences.getPreferences(ctx).getSharedPreference(Constants.SESSION_TOKEN);

        JSONObject result  = new JSONObject();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(SERVER_URL + CMD_CREATE_AD +  "?token=" + token);

        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        try {
            JSONObject json  = null;

            entity.addPart("category_id", new StringBody(String.valueOf(advertisement.getCategory().getId())));
            entity.addPart("title", new StringBody(advertisement.getTitle()));
            entity.addPart("description", new StringBody(advertisement.getDescription()));
            entity.addPart("price", new StringBody(String.valueOf(advertisement.getPrice())));
            entity.addPart("expiration_at", new StringBody(DateFormat.format("dd/MM/yyyy", advertisement.getExpiration_at()).toString()));

            if(advertisement.getPhoto() != null){
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    advertisement.getPhoto().compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, advertisement.getTitle());
                    entity.addPart("photo", bab);
                }
                catch(Exception e){
                    entity.addPart("photo", new StringBody(""));
                }
            }

           //post.addHeader( "Content-Type", "multipart/form-data; ");
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);

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

            if(response.getStatusLine().getStatusCode() == 412){
                json = new JSONObject(output.toString());
                throw new CreateUserException(json.getString(ERROR_REASON_DESC));
            } else if(response.getStatusLine().getStatusCode() == LOGIN_INTERNAL_ERROR_CODE){
                String msg = output.toString().replace("[","");
                msg = msg.replace("]", "");
                msg = msg.replaceAll("\"", "");
                throw new CreateUserException(msg);
            }

            result =  new JSONObject(output.toString());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CreateUserException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static JSONObject createService(Advertisement advertisement, Context ctx){

        String token =
                (String) Preferences.getPreferences(ctx).getSharedPreference(Constants.SESSION_TOKEN);
        JSONObject result  = new JSONObject();

        try {

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("category_id", advertisement.getCategory().getId());
            json.put("title", advertisement.getTitle());
            json.put("description", advertisement.getDescription());
            json.put("price", "0");
            json.put("expiration_at", "1/10/2020");

            HttpPost post = new HttpPost(SERVER_URL + CMD_CREATE_AD +  "?token=" + token);
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");
            post.setEntity(new StringEntity(json.toString()));
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

            if(response.getStatusLine().getStatusCode() == 412){
                json = new JSONObject(output.toString());
                throw new CreateUserException(json.getString(ERROR_REASON_DESC));
            } else if(response.getStatusLine().getStatusCode() == LOGIN_INTERNAL_ERROR_CODE){
                String msg = output.toString().replace("[","");
                msg = msg.replace("]", "");
                msg = msg.replaceAll("\"", "");
                throw new CreateUserException(msg);
            }

            result =  new JSONObject(output.toString());

        } catch(Exception e) {
            Log.e("error", e.getMessage());
        }

        return result;
    }

    public static JSONObject logout(Context ctx) throws Exception{

        String token =
                (String) Preferences.getPreferences(ctx).getSharedPreference(Constants.SESSION_TOKEN);
        JSONObject result  = new JSONObject();

        HttpURLConnection conn = null;

        try {
            URL url = new URL(SERVER_URL + "logout" +  "?token=" + token);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == 200) {

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

                return new JSONObject(sb.toString());
            }
            else{
                Log.e("BILA", String.valueOf(conn.getResponseCode()));
            }

        }catch( Exception e) {
            e.printStackTrace();
            Log.e("WEB", e.getMessage());
        }
        finally {
            conn.disconnect();
        }


        return new JSONObject(result.toString());

    }
}