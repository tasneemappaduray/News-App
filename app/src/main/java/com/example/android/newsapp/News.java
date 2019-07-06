package com.example.android.newsapp;

public class News {

    /** Section of the news */
    private String mSectionName;

    /** Title of the article */
    private String mWebTitle;

    /** Date the article was published */
    private String mDate;

    /** Time the article was published*/
    private String mTime;

    /** Author of the published article */
    private String mAuthor;

    /** Website URL of the news*/
    private String mUrl;

    /** Contructs a new {@link News} object
     * @param sectionName is the type of news eg. politics, entertainment, sport
     * @param webTitle is the title of the article
     * @param author is the author of the article
     * @param date is the date the news article was published
     * @param time is the time the article was published*
     * @param url is the link to the url*/
    public News(String sectionName, String webTitle, String author, String date, String time, String url){
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mAuthor = author;
        mDate = date;
        mTime = time;
        mUrl = url;
    }

    /**Returns the section ID of the article */
    public String getSectionName(){ return mSectionName;}

    /**Returns the tile of the article */
    public String getWebTitle(){ return mWebTitle;}

    /**Returns the date of the article */
    public String getDate(){ return mDate;}

    /**Returns the time of the news.*/
    public String getTime() { return mTime; }

    /**Returns the time the article was published */
    public String getAuthor(){ return mAuthor;}

    /** Returns an empty String if there is no author of the article*/
    public boolean hasAuthor() { return mAuthor != ""; }

    /**Returns the website URL to find more information about the earthquake.*/
    public String getUrl() { return mUrl; }
}
