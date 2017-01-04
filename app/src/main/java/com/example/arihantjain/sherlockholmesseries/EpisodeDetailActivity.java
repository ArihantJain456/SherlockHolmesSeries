package com.example.arihantjain.sherlockholmesseries;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EpisodeDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public String[][] Videos = {{"MiglWGPP0nA","y_GGbRkqqFg","AviDWKhmVdU"},{"JE4wTmbyV8g","bm78r2innnE","wVoo9RqrHtw"},{"O7cKIjNIPoY","N-lMgIg2p1c","LI7T69cA3Kg"},{"O11V8ArXTpc","O11V8ArXTpc","O11V8ArXTpc"}};
    static TextView title_text,duration_text,summary_text,episode_text,rate_text;
    static RatingBar ratingBar;
    public static final String API_KEY = "AIzaSyBAZ3AG-L5FTfWzSMMqFHV0VVooczs9tSo";
    public String VIDEO_ID ;
    EpisodeModel episodeModel;
    static ImageView loadingImage;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode_detail);
       if(savedInstanceState == null)
        new EpisodeData().execute("http://www.omdbapi.com/?t=Sherlock&Season=" + MainActivity.getCurrentSeason() + "&Episode=" + MainActivity.getCurrentEpisode());
        VIDEO_ID = Videos[MainActivity.getCurrentSeason()-1][MainActivity.getCurrentEpisode()-1];
        loadingImage = (ImageView)findViewById(R.id.loading_content);
        loadingImage.setImageResource(R.drawable.progress_image);
        title_text = (TextView) findViewById(R.id.Title);
        duration_text = (TextView) findViewById(R.id.duration);
        summary_text = (TextView) findViewById(R.id.summary);
        episode_text = (TextView) findViewById(R.id.EpisodeNo);
        rate_text = (TextView) findViewById(R.id.ratingText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.episode_youtube_player);
        youTubePlayerView.initialize(API_KEY,this);
}
    public  void loadEpisodeData(EpisodeModel episodeModel){
        if(episodeModel!=null) {
            title_text.setText(episodeModel.getTitle());
            episode_text.setText("Episode : " + episodeModel.getEpisode());
            summary_text.setText("Summary : " + episodeModel.getSummary());
            duration_text.setText("Duraton : " + episodeModel.getDuration());
            rate_text.setText("RATING : " + Float.parseFloat(episodeModel.getRatings()) + "/10");
            ratingBar.setRating(Float.parseFloat(episodeModel.getRatings()));
        }
        else {

            Toast.makeText(EpisodeDetailActivity.this,"Weak internet connection",Toast.LENGTH_LONG).show();
            Toast.makeText(EpisodeDetailActivity.this,"Error in loading data",Toast.LENGTH_LONG).show();
        }
        loadingImage.setImageResource(0);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }

    public class EpisodeData extends AsyncTask<String,String,EpisodeModel> {
        @Override
        protected EpisodeModel doInBackground(String...str) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(str[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine())!=null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                EpisodeModel episodeModel = new EpisodeModel();
                episodeModel.setTitle(parentObject.getString("Title"));
                episodeModel.setEpisode(parentObject.getString("Episode"));
                episodeModel.setDuration(parentObject.getString("Runtime"));
                episodeModel.setRatings(parentObject.getString("imdbRating"));
                episodeModel.setSummary(parentObject.getString("Plot"));
                episodeModel.setVideo_Id("MiglWGPP0nA");
                return  episodeModel;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();}
            finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(EpisodeModel result) {
            super.onPostExecute(result);
            loadEpisodeData(result);
            episodeModel = result;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("episodeput",episodeModel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        episodeModel = (EpisodeModel) savedInstanceState.getSerializable("episodeput");
        loadEpisodeData(episodeModel);
    }
}
