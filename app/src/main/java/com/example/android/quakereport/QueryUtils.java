package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Tag for logging messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String requestURL) {

        URL url = createURL(requestURL);

        String jsonResponse = null;
        try {
            // Preforming http request and getting response
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // extracting data from json response
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        return earthquakes;
    }

    /**
     * Crate URL object from String value
     *
     * @param stringURL String value of query
     * @return returns URL object
     */
    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL", e);
        }
        return url;
    }

    /**
     * Makes http request  and gets json response
     *
     * @param url for http url connection
     * @return string response from API in json form
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String JSONResponse = "";
        // if url don't exist return empty JSONResponse
        if (url == null)
            return JSONResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            // Gets input stream only if response code is 200
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else
                Log.e(LOG_TAG, "Error occurred. Response code: " + urlConnection.getResponseCode());

        } catch (IOException e) {
            Log.e(LOG_TAG, "Http request failed.", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return JSONResponse;
    }

    /**
     * Converts input stream of http response to string
     *
     * @param inputStream from url connection
     * @return string from input stream
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponse) {

        if (jsonResponse == null)
            return null;

        // Create an empty List that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // gets jason object from json response
            JSONObject jsonStringObject = new JSONObject(jsonResponse);

            // Gets json array from root by name
            JSONArray jsonArray = jsonStringObject.optJSONArray("features");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                double magnitude = jsonObject.getJSONObject("properties").optDouble("mag");
                String location = String.valueOf(jsonObject.getJSONObject("properties").optString("place"));
                long time = jsonObject.getJSONObject("properties").optLong("time");
                String url = jsonObject.getJSONObject("properties").optString("url");

                Earthquake earthquake = new Earthquake(location, magnitude, time, url);

                earthquakes.add(earthquake);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}