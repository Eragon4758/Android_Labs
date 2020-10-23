package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notround);
        ForecastQuery forecast = new ForecastQuery();
        forecast.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>

    {
        private String uv;
        private String max;
        private String min;
        private String tempValue;
        private Bitmap weatherIcon;
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
        protected String doInBackground(String ... args) {
            try{
                //create url object
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream inStream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName();
                        if(tagName.equals("temperature"))
                        {
                            tempValue = xpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "Found value message: "+ tempValue);
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "Found min message: "+ min);
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            Log.e("AsyncTask", "Found max message: "+ max);
                            publishProgress(75);
                        }else if (tagName.equals("weather")) {
                            String f = xpp.getAttributeValue(null, "icon");
                            String fileName = f + ".png";
                            Log.e("AsyncTask", "Looking for icon name: "+ fileName);
                            if (fileExistance(fileName)){
                                Log.e("AsyncTask", "Found icon name \""+fileName+"\" in local");
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(getBaseContext().getFileStreamPath(fileName));
                                }catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                weatherIcon = BitmapFactory.decodeStream(fis);
                            }else {
                                Log.e("AsyncTask", "icon name \""+fileName+"\" doesn't exist in local");
                                Log.e("AsyncTask", "Downloading image from the internet");  //download image
                                Log.e("AsyncTask", "Saving to local.....");
                                weatherIcon = null;
                                URL dlUrl = new URL( "https://openweathermap.org/img/w/" + fileName);
                                HttpURLConnection connection = (HttpURLConnection) dlUrl.openConnection();
                                connection.connect();
                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    weatherIcon = BitmapFactory.decodeStream(connection.getInputStream());
                                }
                                FileOutputStream outputStream = null;
                                try {
                                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    weatherIcon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (Exception e) {
                                    Log.e("AsyncTask", "Download fail");
                                }
                            }
                            publishProgress(100);
                        }
                    }
                    xpp.next(); //advance to next XML event
                }
                URL UVurl = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                uv = String.valueOf(jObject.getDouble("value"));
                Log.e("AsyncTask", "Found UV: " +uv);
            }catch (Exception e)    {
                Log.e("error", e.getMessage());
            }
            return "Finished task";
        }
        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            ProgressBar bar = findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(0);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
            TextView temp = findViewById(R.id.temperatureText);
            temp.setText("Temperature: "+tempValue);
            TextView mintemp = findViewById(R.id.minTemp);
            mintemp.setText("Minimum Temp: "+min);
            TextView maxtemp = findViewById(R.id.maxTemp);
            maxtemp.setText("Maximum Temp: "+max);
            TextView uvrate = findViewById(R.id.UVrating);
            uvrate.setText("UV index: "+uv);
            ImageView image = findViewById(R.id.weatherMap);
            image.setImageBitmap(weatherIcon);
            ProgressBar bar = findViewById(R.id.progressBar);
            bar.setVisibility(View.INVISIBLE);
        }
    }
}