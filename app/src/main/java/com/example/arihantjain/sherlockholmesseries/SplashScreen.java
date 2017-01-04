package com.example.arihantjain.sherlockholmesseries;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    ImageView loadingImage;
    TextView internetStatus;
    ArrayList<SeasonModel> seasonModels;
    boolean datacollected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loadingImage = (ImageView)findViewById(R.id.screen_image);
        seasonModels = new ArrayList<SeasonModel>();
        internetStatus = (TextView)findViewById(R.id.internet_status);
        getDataForSeasons();
        loadingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataForSeasons();
            }
        });
    }

    public class JsonTask extends AsyncTask<String,String,ArrayList<SeasonModel>> {

        @Override
        protected ArrayList<SeasonModel> doInBackground(String... str) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(str[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                datacollected = true;

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray episodeArray = parentObject.getJSONArray("seasons");
                for (int i = 1; i < episodeArray.length(); i++) {

                    SeasonModel seasonModel = new SeasonModel();
                    seasonModel.setSeasonDate(episodeArray.getJSONObject(i).getString("air_date"));
                    seasonModel.setSeasonNo(episodeArray.getJSONObject(i).getInt("season_number"));
                    seasonModel.setSeasonImage(("http://image.tmdb.org/t/p/w500" + episodeArray.getJSONObject(i).getString("poster_path")));
                    seasonModels.add(seasonModel);
                }
                seasonModels.get(seasonModels.size() - 1).setSeasonImage("https://images-na.ssl-images-amazon.com/images/M/MV5BMTk3OTc4NTE1OF5BMl5BanBnXkFtZTgwMTEyODQ0OTE@._V1_SX300.jpg");
                return seasonModels;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("JSONDATA","Malform");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONDATA","JSON");
            } catch (IOException e) {
                e.printStackTrace();
                datacollected = false;
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SeasonModel> result) {
            super.onPostExecute(result);
            if(datacollected) {
                Intent seasonIntent = new Intent();
                seasonIntent.putExtra("seasons", result);
                setResult(RESULT_OK, seasonIntent);
                finish();
            }
            else {
                loadingImage.setImageResource(R.drawable.refresh);
                loadingImage.setClickable(true);
                internetStatus.setText("Bad Internet\n" + "Please check your Internet settings.");

            }
        }
    }
    public void getDataForSeasons(){
        loadingImage.setImageResource(R.drawable.progress_image);
        internetStatus.setText("");
        if(MainActivity.detector.isConnected()) {
            new JsonTask().execute("https://api.themoviedb.org/3/tv/19885?api_key=6594cb5a57506e07a88f170e079495f4&language=en-US");
            loadingImage.setClickable(false);
        }
        else {
            loadingImage.setImageResource(R.drawable.refresh);
            loadingImage.setClickable(true);
            internetStatus.setText("No Internet\n" + "Please check your Internet settings.");
        }
    }
}
