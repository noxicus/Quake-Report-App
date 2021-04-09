/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Request url to USGS
     */
    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";
    EarthquakeAdapter earthquakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
//        List<Earthquake> earthquakes = QueryUtils.extractEarthquakes(REQUEST_URL);

        // Create a new {@link ArrayAdapter} of earthquakes
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
//        earthquakeListView.setAdapter(adapter);

        //Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Crate earthquake adapter
        earthquakeAdapter = new EarthquakeAdapter(getApplicationContext(), R.layout.list_item, new ArrayList<Earthquake>());

        //Set adapter on Listview
        earthquakeListView.setAdapter(earthquakeAdapter);

        // On click listener - when item is clicked url od earthquake is opened
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake = earthquakeAdapter.getItem(position);
                Uri webpage = Uri.parse(earthquake.getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });

        FetchEarthquakesDataTask fetchEarthquakesDataTask = new FetchEarthquakesDataTask();
        fetchEarthquakesDataTask.execute(REQUEST_URL);

    }

//    private void makeUI(List<Earthquake> earthquakes) {
//
//        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        // Crate earthquake adapter
//        final EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(getApplicationContext(), R.layout.list_item, earthquakes);
//
//        //Set adapter on Listview
//        earthquakeListView.setAdapter(earthquakeAdapter);
//
//        // On click listener - when item is clicked url od earthquake is opened
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Earthquake earthquake = earthquakeAdapter.getItem(position);
//                Uri webpage = Uri.parse(earthquake.getmUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//                startActivity(intent);
//            }
//        });
//    }

    // Class that extends async task who will perform background task of extracting data trough http request and response
    private class FetchEarthquakesDataTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... strings) {
            if (strings.length < 0 || strings[0] == null)
                return null;

            List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(strings[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {

            earthquakeAdapter.clear();

            if (earthquakes != null && !earthquakes.isEmpty())

                earthquakeAdapter.addAll(earthquakes);
        }
    }
}
