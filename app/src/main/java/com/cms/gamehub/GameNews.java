package com.cms.gamehub;

public class GameNews
{
    private String mTitle;
    private String mSectionName;
    private String mAuthor;
    private long mPublicationTime;            //time in millisecond
    private String mUrl;

    public GameNews(String title, String sectionName, String author, long publicationTime, String url)
    {
        mTitle = title;
        mSectionName = sectionName;
        mAuthor = author;
        mPublicationTime = publicationTime;
        mUrl = url;
    }

    public String getTitle(){return mTitle;}

    public String getSectionName(){return mSectionName;}

    public String getAuthor(){return mAuthor;}

    public long getPublicationTime(){return mPublicationTime;}

    public String getUrl(){return mUrl;}


}
