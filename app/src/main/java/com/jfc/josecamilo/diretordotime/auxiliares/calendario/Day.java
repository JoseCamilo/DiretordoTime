package com.jfc.josecamilo.diretordotime.auxiliares.calendario;

/**
 * Created by jose.camilo on 24/11/2016.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.widget.BaseAdapter;

import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

public class Day{


    // magic number=
    // millisec * sec * min * hours
    // 1000 * 60 * 60 * 24 = 86400000
    public static final float MAGIC=86400000F;

    int startDay;
    int monthEndDay;
    int day;
    int year;
    int month;
    Context context;
    BaseAdapter adapter;
    ArrayList<Event> events = new ArrayList<Event>();

    private DatabaseManager db;

    public Day(Context context, int day, int year, int month){
        this.day = day;
        this.year = year;
        this.month = month;
        this.context = context;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(year, month, end);
        TimeZone tz = TimeZone.getDefault();
        monthEndDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
    }

    public Day() {

    }

    public int getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    /**
     * Add an event to the day
     *
     * @param event
     */
    public void addEvent(Event event){
        events.add(event);
    }

    /**
     * Set the start day
     *
     * @param startDay
     */
    public void setStartDay(int startDay){
        this.startDay = startDay;
        new GetEvents().execute();
    }

    public int getStartDay(){
        return startDay;
    }

    public int getNumOfEvenets(){
        return events.size();
    }

    /**
     * Returns a list of all the colors on a day
     *
     * @return list of colors
     */
    public Set<Integer> getColors(){
        Set<Integer> colors = new HashSet<Integer>();
        for(Event event : events){
            colors.add(event.getColor());
        }

        return colors;
    }

    /**
     * Get all the events on the day
     *
     * @return list of events
     */
    public ArrayList<Event> getEvents(){
        return events;
    }

    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
    }

    private class GetEvents extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if (db == null) {
                DatabaseManager.initializeInstance(new DatabaseHelper(context));
                db = DatabaseManager.getInstance();
            }

            Calendar calnew = Calendar.getInstance();
            calnew.set(year, month, day);

            events = db.obterEventosDayOn(calnew.getTimeInMillis());
            return null;
        }

        protected void onPostExecute(Void par){
            adapter.notifyDataSetChanged();
        }

    }

    public int DateToDays (Date date){
        float longDate = (float)date.getTime();
        return ((int)Math.ceil(longDate/MAGIC));
    }

    public Date DaysToDate(int days) {
        return new Date((long) (days*MAGIC));
    }


}
