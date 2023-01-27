package com.example.rss_reader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetRssFeed {
    private ArrayList<Feed> feeds = new ArrayList<Feed>();
    private String userId;
    private String stringUrl;

    public GetRssFeed(String userId, String stringUrl) {
        this.userId = userId;
        this.stringUrl = stringUrl;
        readXml();
    }

    public void readXml(){
        try{
            URL rssurl = new URL(stringUrl);
            InputStream stream = rssurl.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(stream,"utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && parser.getName().toString().equals("item")){
                    Boolean endLoop = true;
                    Feed feed = new Feed();
                    while (endLoop) {
                        if(eventType == XmlPullParser.START_TAG){
                            if(eventType == XmlPullParser.START_TAG) {
                                if ((parser.getAttributeValue(null, "type") != null)) {
                                    if(parser.getAttributeValue(null, "type").contains("image")){
                                            feed.setImageLink(parser.getAttributeValue(null, "url"));
                                            try {
                                                URL url = new URL(parser.getAttributeValue(null, "url"));
                                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                                feed.setImage(image);
                                            } catch(IOException e) {
                                                System.out.println(e);
                                            }
                                        }
                                    }
                                }
                                if(parser.getName().toString().equals("title")){
                                    feed.setTitle(parser.nextText());
                                }
                                else if(parser.getName().toString().equals("link")){
                                    feed.setLink(parser.nextText());
                                }
                                else if(parser.getName().toString().equals("description")){
                                    feed.setDescription(parser.nextText());
                                }
                                else if(parser.getName().toString().equals("pubDate")){
                                    feed.setPubDate(parser.nextText());
                                }
                            }
                            else if(eventType == XmlPullParser.END_TAG){
                                if(parser.getName().toString().equals("item"))
                                    endLoop = false;
                            }
                            eventType = parser.next();
                        }
                        feed.setUserId(userId);
                        feeds.add(feed);
                }
                eventType = parser.next();
            }
        } catch (MalformedURLException ue){
            System.out.println("Malformed URL");
        } catch (IOException ioe){
            System.out.println("Something went wrong reading the contents");
        } catch (XmlPullParserException e) {
            System.out.println("parser ye kaka ");
        }
    }
    public ArrayList<Feed> getFeeds() {
        return feeds;
    }
}
