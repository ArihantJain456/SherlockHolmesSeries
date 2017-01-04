package com.example.arihantjain.sherlockholmesseries;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class EpisodeFragment extends Fragment {
    static RecyclerView recyclerView;
    String[] episodeNo4 = {"1","2","3"};
    String[] episodesTitle4 = {"The Six Thatchers","The Lying Detective","The Final Problem"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        return view;

    }

    public void onSeasonClick(int season) {
        recyclerView.setAdapter(null);
        EpisodeModel.season = season;
        if(MainActivity.getCurrentSeason()!=4){
        new JsonTask().execute("https://api.themoviedb.org/3/tv/19885/season/"+season+"?api_key=6594cb5a57506e07a88f170e079495f4&language=en-US");
        }

        else
        seasonFourClicked();
    }

    private void seasonFourClicked() {
        ArrayList<EpisodeModel> models = new ArrayList<>();
        for(int i=0; i<episodeNo4.length;i++){
            EpisodeModel epiModel = new EpisodeModel();
            epiModel.setEpisode("Episode : " + episodeNo4[i]);
            epiModel.setTitle(episodesTitle4[i]);
            models.add(epiModel);
            MainActivity.loading_imageView.setImageResource(0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EpisodeRecycleViewAdapter arrayAdapter = new EpisodeRecycleViewAdapter(getActivity(),models);
        recyclerView.setAdapter(arrayAdapter);

    }

    public class JsonTask extends AsyncTask<String,String,ArrayList<EpisodeModel>>{

        @Override
        protected ArrayList<EpisodeModel> doInBackground(String...str) {
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
                ArrayList<EpisodeModel> episodeModels = new ArrayList<EpisodeModel>();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray episodeArray = parentObject.getJSONArray("episodes");
                for(int i=0;i<episodeArray.length();i++){

                    EpisodeModel episodeModel = new EpisodeModel();
                    episodeModel.setTitle(episodeArray.getJSONObject(i).getString("name"));
                    episodeModel.setEpisode("Episode " + episodeArray.getJSONObject(i).getString("episode_number"));
                    episodeModel.setImage("http://image.tmdb.org/t/p/w500/" + episodeArray.getJSONObject(i).getString("still_path"));
                    episodeModels.add(episodeModel);
                }
                return  episodeModels;
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
        protected void onPostExecute(ArrayList<EpisodeModel> result) {
            super.onPostExecute(result);
            System.out.println("On post execute working");
            MainActivity.loading_imageView.setImageDrawable(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            EpisodeRecycleViewAdapter arrayAdapter = new EpisodeRecycleViewAdapter(getActivity(),result);
            recyclerView.setAdapter(arrayAdapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(),recyclerView,new RecyclerItemClickListener.OnItemClickListener(){

                        @Override
                        public void onItemClick(View view, int position) {
                            if(MainActivity.getCurrentSeason()!=4) {
                                if(MainActivity.detector.isConnected()) {
                                    MainActivity.setCurrentEpisode(position + 1);
                                    Intent intent = new Intent(getActivity(), EpisodeDetailActivity.class);
                                    getActivity().startActivityForResult(intent,102);
                                }
                                else {
                                    Snackbar.make(view,"No Internet Available",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                }
                            }
                            else{
                                Snackbar.make(view,"Not Available Now",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    })
            );
        }

    }


}
