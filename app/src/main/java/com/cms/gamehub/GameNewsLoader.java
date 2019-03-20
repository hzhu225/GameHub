package com.cms.gamehub;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;


public class GameNewsLoader extends AsyncTaskLoader<List<GameNews>>
{
    private String reqUrl;

    public GameNewsLoader(Context context, String url)
    {
        super(context);
        reqUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<GameNews> loadInBackground()
    {
        if(reqUrl == null)
        {
            return null;
        }

        return QueryUtils.buildListFromUrl(reqUrl);
    }
}
