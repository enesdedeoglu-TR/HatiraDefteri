package tr.edu.yildiz.enes.gunluk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStreamReader;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class PlacesApi {

    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList = new ArrayList<String>();

        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();

        try{
            StringBuilder sb = new StringBuilder("https://api.mapbox.com/geocoding/v5/mapbox.places/");
            sb.append(input + ".json?");
            sb.append("access_token=pk.eyJ1IjoiZW5lc2RlZGUiLCJhIjoiY2w0OHVrYmlwMGdkNDNjcjJlMnQ5YWsybCJ9.s6DLXw3ud0utoqEGBMXF0g");
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff = new char[1024];

            while((read = inputStreamReader.read(buff)) != -1){
                jsonResult.append(buff, 0,read);
            }

        }catch(IOException ex){
            ex.printStackTrace();
            Log.v("Url Error: ", ex.getMessage());
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray prediction = jsonObject.getJSONArray("features");
            for(int i=0; i< prediction.length(); i++){
                String selectedLocation = prediction.getJSONObject(i).getString("place_name")+ " | " +prediction.getJSONObject(i).getJSONArray("center").get(0).toString() + " | " +prediction.getJSONObject(i).getJSONArray("center").get(1).toString();
                arrayList.add(selectedLocation);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Log.v("JSON Error: ", ex.getMessage());
        }

        return arrayList;
    }

}
