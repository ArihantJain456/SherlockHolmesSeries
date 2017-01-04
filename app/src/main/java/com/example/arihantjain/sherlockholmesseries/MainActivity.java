package com.example.arihantjain.sherlockholmesseries;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends YouTubeBaseActivity implements Communicator,YouTubePlayer.OnInitializedListener{
    private static final String API_KEY ="AIzaSyBAZ3AG-L5FTfWzSMMqFHV0VVooczs9tSo";
    private static final int EPISODE_ACIVITY = 102;
    NestedScrollView nestedScrollView;
    TextView seasonText;
    boolean isFullscreen;
    static ImageView loading_imageView , load;
    boolean youtubeInitialized;
    public static ConnectionDetector detector;
    YouTubePlayerView youTubePlayerView ;
    YouTubePlayer player;
    ArrayList<SeasonModel> seasonModels;
    static BottomSheetBehavior bottomSheetBehavior;
    String[] SeasonTrailer = {"Nj7ZSUkTTVI","vydmN9pzDzg","9UcR9iKArd0","qlcWFoNqZHc"};
    Random random ;
    SeasonFragment fragment;
    private final int SplashScreen_CODE = 101;

    public static int getCurrentEpisode() {
        return currentEpisode;
    }

    public static void setCurrentEpisode(int currentEpisode) {
        MainActivity.currentEpisode = currentEpisode;
    }

    public static int getCurrentSeason() {
        return currentSeason;
    }

    public static void setCurrentSeason(int currentSeason) {
        MainActivity.currentSeason = currentSeason;
    }


    public static int currentEpisode = 0,currentSeason = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState==null){
            startActivityForResult(new Intent(this,SplashScreen.class),SplashScreen_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detector = new ConnectionDetector(this);
        fragment = new SeasonFragment();
        EpisodeFragment fragment1 = new EpisodeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.bottom_fragment,fragment1);
        fragmentTransaction.add(R.id.fragment,fragment);
        fragmentTransaction.commit();
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.season_player);
        youTubePlayerView.initialize(API_KEY,this);
        loading_imageView = (ImageView)findViewById(R.id.loading_data_image);
        load = (ImageView) findViewById(R.id.loading_screen);
        random = new Random();
        seasonText = (TextView)findViewById(R.id.Season_num);
        nestedScrollView = (NestedScrollView) findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);
        bottomSheetBehavior.setPeekHeight(0);
    }

    @Override
    public void onBackPressed() {
        if(isFullscreen){
            player.setFullscreen(false);
        }
        else if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public void respond(int pos) {
        currentSeason = pos+1;
        seasonText.setText("Season : "+currentSeason);
        if(currentSeason!=4)
            loading_imageView.setImageResource(R.drawable.image_placeholder);
        if(youtubeInitialized)
            player.cueVideo(SeasonTrailer[pos]);
        FragmentManager manager= getFragmentManager();
        EpisodeFragment episodeFragment= (EpisodeFragment) manager.findFragmentById(R.id.bottom_fragment);
        episodeFragment.onSeasonClick(pos+1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SplashScreen_CODE){
                seasonModels = (ArrayList<SeasonModel>) data.getSerializableExtra("seasons");
                fragment.onLoadingSeasons(seasonModels);
            }
        }
        else{
            if(requestCode == SplashScreen_CODE){
                finish();
            }
            if(requestCode == EPISODE_ACIVITY){
                Toast.makeText(this, "ret" + SeasonTrailer[currentSeason-1], Toast.LENGTH_SHORT).show();
                if(youtubeInitialized) {
                    player.cueVideo(SeasonTrailer[currentSeason - 1]);
                    player.play();
                }
            }
        }
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youtubeInitialized = true;
        player = youTubePlayer;
        player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullscreen = b;
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        if(!b) {
            int i = Math.abs(random.nextInt()) % SeasonTrailer.length;
            youTubePlayer.cueVideo(SeasonTrailer[i]);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(MainActivity.this,"Please install YouTube App", Toast.LENGTH_LONG).show();
        youtubeInitialized = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("seasonsPut",seasonModels);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            seasonModels = (ArrayList<SeasonModel>) savedInstanceState.getSerializable("seasonsPut");
            fragment.onLoadingSeasons(seasonModels);
        }
    }
}

