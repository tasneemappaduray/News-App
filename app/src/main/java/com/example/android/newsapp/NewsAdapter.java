package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        News currentNews = getItem(position);

        TextView sectionTextView = listItemView.findViewById(R.id.section_name);
        String sectionName = currentNews.getSectionName();
        sectionTextView.setText(sectionName);

        TextView webTitleView = listItemView.findViewById(R.id.web_title);
        String webTitle = currentNews.getWebTitle();
        webTitleView.setText(webTitle);


        /** Populate author field if news item has an author*/
        TextView authorTextView = listItemView.findViewById(R.id.author);

        if (currentNews.hasAuthor()) {
            authorTextView.setText(currentNews.getAuthor());
            authorTextView.setVisibility(View.VISIBLE);
        } else {
            authorTextView.setVisibility(View.GONE);
        }

        /** Create a new Date object from the time the news article was published*/
        String dateString = currentNews.getTime();

        /** Find the TextView with view ID date*/
        TextView dateView = listItemView.findViewById(R.id.date);
        /** Format the date string (i.e. "Mar 3, 1984")*/
        String formattedDate = formatDate(dateString);
        /** Display the date of the current news article in that TextView*/
        dateView.setText(formattedDate);

        /** Find the TextView with view ID time*/
        TextView timeView = listItemView.findViewById(R.id.time);
        /** Format the time string (i.e. "4:30PM")*/
        String formattedTime = formatTime(dateString);
        /** Display the time of the current news article in that TextView*/
        timeView.setText(formattedTime);


        return listItemView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date String.
     */
    private String formatDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return newDateFormat.format(date);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date String.
     */
    private String formatTime(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(date);
    }
}
