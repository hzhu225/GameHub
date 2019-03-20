package com.cms.gamehub;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils
{
    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils()
    {

    }

    public static List<GameNews> buildListFromUrl(String requestUrl)
    {
        URL reqUrl = createUrl(requestUrl);

        String jsonResponse = null;
        try
        {
            jsonResponse = makeHttpRequest(reqUrl);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        if (TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }

        List<GameNews> gameNewsList = new ArrayList<>();

        try
        {
            JSONObject baseJsonObj = new JSONObject(jsonResponse);

            JSONObject responseObj = baseJsonObj.getJSONObject("response");

            JSONArray resultsArray = responseObj.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++)
            {
                JSONObject newsObj = resultsArray.getJSONObject(i);
                String title = newsObj.getString("webTitle");
                String sectionName  = newsObj.getString("sectionName");

                String author = null;
                JSONArray tagArray = newsObj.getJSONArray("tags");
                if(tagArray.length() > 0)
                {
                    ArrayList<String> authorList = new ArrayList<>();
                    for (int j = 0; j < tagArray.length(); j++)
                    {
                        JSONObject authorObj = tagArray.getJSONObject(j);
                        String authorName = authorObj.getString("webTitle");
                        authorList.add(authorName);
                    }
                    author = TextUtils.join(", ", authorList);
                }

                long publicationTime = 0;
                String publicationDate = newsObj.getString("webPublicationDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try
                {
                    Date d = sdf.parse(publicationDate);
                    publicationTime = d.getTime();
                }
                catch(ParseException e)
                {
                    e.printStackTrace();
                }

                String url = newsObj.getString("webUrl");

                GameNews gameNews = new GameNews(title, sectionName, author, publicationTime,url);
                gameNewsList.add(gameNews);
            }
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return gameNewsList;
    }



    private static URL createUrl(String stringUrl)
    {
        URL url = null;
        try
        {
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }



    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }



    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



}
