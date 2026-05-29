package vukasin.janjic.eventsapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
    public static final String BASE_URL = "http://192.168.1.10:3000";

    public static JSONObject getJSONObjectFromUrl(String urlString) throws IOException, JSONException{
        HttpURLConnection urlConnection=null;
        URL url = new URL(urlString);
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode!= HttpURLConnection.HTTP_OK){
                return null;
            }
            BufferedReader br =new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            br.close();

            return new JSONObject(sb.toString());
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }

    }

    public static JSONArray getJSONArrayFromUrl(String urlString) throws IOException,JSONException{
        HttpURLConnection urlConnection=null;
        URL url = new URL(urlString);

        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK){
                return null;
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            br.close();

            return new JSONArray(sb.toString());
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }

    public static  JSONObject postJSONObjectToUrl(String urlString, JSONObject jsonObject) throws IOException,JSONException{
        HttpURLConnection urlConnection =null;
        URL url = new URL(urlString);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.connect();

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            BufferedReader br;
            if(responseCode >=200 && responseCode<300){
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            }else{
                br= new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!= null){
                sb.append(line);
            }
            br.close();

            if(sb.length()==0){
                return null;
            }
            return new JSONObject(sb.toString());
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }

    public static JSONObject putJSONObjectToUrl(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.connect();

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            if (sb.length() == 0) {
                return null;
            }

            return new JSONObject(sb.toString());

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


}
