package com.cs446.group18.timetracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.NotificationConstant;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.ui.MainActivity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ForecastingService extends Service implements NotificationStrategy{

    private Context context;
    android.content.res.Resources res;
    private NotificationManagerCompat notificationManager;
    private TimeEntryRepository timeEntryRepository;

    private List<Date> timeEntriesDate = new ArrayList<Date>();
    private int numOfGeneration = 500;
    private int DNAsize = 17;
    private int Popsize = 50;
    private int MAX = 86399;
    private int MIN = 0;
    private double CROSS_RATE = 0.8;
    private double MUTATION_RATE = 0.3;

    int targetSecond = 0;
    private int min = MAX;

    public ForecastingService(Context context){
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        this.timeEntryRepository = TimeEntryRepository.getInstance(TimeTrackerDatabase.getInstance(context).timeEntryDao());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        res = getResources();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void startForecastingService(long eventID, Context context) {
        // add all geolocation to the quad tree

        new Thread(() -> {
            List<TimeEntry> timeEntries = timeEntryRepository.getTimeEntriesByEventIDStatic(eventID);
            Log.d("startForecastingService", "startForecastingService has timeEntry: " + timeEntries.size());
            for (TimeEntry timeEntry: timeEntries) {
                Log.d("startForecastingService", "startForecastingService has timeEntry: " + timeEntry.getStartTimeStr());
                this.timeEntriesDate.add(timeEntry.getStartTime());
            }

            this.targetSecond = gettargetSecond(timeEntriesDate);
            String messages[] = new String[1];
            messages[0] = getForecastingResult().substring(11);
            notify(res, messages);
        }).start();

    }


    @Override
    public void notify(Resources resources, String[] messages) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(context, NotificationConstant.FORECASTING_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("Forecasting service"))
                .setContentTitle("Suggested schedule for Study: " + messages[0])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.rgb(15, 163, 232))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(3, notification);
    }

    // get Forecasting Result
    public String getForecastingResult(){

        int targetHours = getHours(targetSecond);
        int targetMinutes = getMinutes(targetSecond, targetHours);
        int targetSeconds = getSeconds(targetSecond,targetHours, targetMinutes);
        Date targetDate = getDate(targetHours, targetMinutes, targetSeconds);

        Date res = targetDate;

        String[] pop = getInitPopDNA(MAX, MIN, Popsize);

        for(int i = 0; i<numOfGeneration; ++i){
            int[] fitness = getFitness(pop, targetSecond);
            pop = select(pop, fitness, MAX);

            // Convert DNA to integer
            int[] popInt = getPopIntArray(pop);
            int totalSecond = getMinValue(popInt);
//            System.out.println("totalSecond is: " + totalSecond);
            if(Math.abs(targetSecond-totalSecond) < min){
                min = Math.abs(targetSecond-totalSecond);
                int hours = getHours(totalSecond);
                int minutes = getMinutes(totalSecond, hours);
                int seconds = getSeconds(totalSecond,hours, minutes);
                res = getDate(hours, minutes, seconds);

            }

            String[] pop_copy = pop;
            for(int j = 0; j < pop.length; ++j){
                String child = crossover(pop[j], pop_copy, CROSS_RATE, Popsize, DNAsize);
                child = mutation(child, MUTATION_RATE, DNAsize);
                pop[j] = child;
            }
        }

        return res.toString();
    }

    // getFitness
    private static int[] getFitness(String[] pop, int targetSecond) {
        int[] res = new int[pop.length];
        for(int i = 0; i < pop.length; ++i) {
            res[i] = Math.abs(targetSecond - translateFromDNA(pop[i]));
        }
        return res;
    }

    // Natural Selection
    private static String[] select(String[] pop, int[] fitness, int MAX) {
        int len = pop.length;
        String[] res = new String[len];

        // Assign smaller value with larger weights
        int[] invertedFitenss = invertWeights(fitness, MAX);

        // Add elements
        for(int i = 0; i<len; ++i) {
            int tmp = myRand(getPopIntArray(pop), invertedFitenss, len);
            res[i] = translateToDNA(tmp);
        }

        return res;
    }


    // Initialize Pop DNA
    private static String[] getInitPopDNA(int MAX, int MIN, int Popsize) {
        String[] pop = new String[Popsize];
        for(int i = 0; i < Popsize; ++i) {
            int tmp = (int)Math.round(Math.random() * (MAX - MIN + 1) + MIN);
            String str = translateToDNA(tmp);
            pop[i] = str;
        }
        return pop;
    }


    // Crossover: Using single point crossover
    private static String crossover(String parent, String[] pop, double CROSS_RATE, int Popsize, int DNAsize){
        int maxPop = Popsize-1;
        int maxDNA = DNAsize-1;
        int min = 0;
        int rangePop = maxPop - min + 1;
        int rangeDNA = maxDNA - min + 1;

        if(Math.random() < CROSS_RATE){

            // choose another parent from pop, range is [0,49]
            int idx = (int)(Math.random() * rangePop) + min;
            String anotherParent = pop[idx];

            int crossPoint = (int)(Math.random() * rangeDNA) + min;
            return concatTwoStrings(parent, anotherParent, crossPoint);
        }
        return parent;
    }


    // Mutation: Using single point mutation
    private static String mutation(String child, double MUTATION_RATE, int DNAsize){
        StringBuilder sb = new StringBuilder(child);

        if(Math.random() < MUTATION_RATE){
            int maxDNA = DNAsize-1;
            int min = 0;
            int rangeDNA = maxDNA - min + 1;
            int mutationPoint = (int)(Math.random() * rangeDNA) + min;
            if(sb.charAt(mutationPoint) == '0'){
                sb.setCharAt(mutationPoint, '1');
            }else{
                sb.setCharAt(mutationPoint, '0');
            }
        }

        return sb.toString();

    }

    /*
     **************************
     ***** Util Functions *****
     **************************
     */

    // Convert int to Binary
    private static String translateToDNA(int totalSecond) {
        String res = String.format("%17s", Integer.toBinaryString(totalSecond)).replace(' ', '0');
        return res;
    }


    // Convert binary to int
    private static int translateFromDNA(String DNA) {
        return Integer.parseInt(DNA,2);
    }


    // Return Target Time Entry
    private static int gettargetSecond(List<Date> dates) {
        long totalSeconds = 0L;
        long averageSeconds = 0L;
        for(Date date: dates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second  = calendar.get(Calendar.SECOND);
            int tmp = hour * 60 * 60 + minute * 60 + second;

            totalSeconds += tmp;
        }

        if(dates.size() == 0){
            averageSeconds = totalSeconds;
        }else{
            averageSeconds = totalSeconds / dates.size();
        }

        return (int)averageSeconds;
    }


    // Get The Most Early Date
    private static Date getMostEarlyDate(List<Date> timeEntries) {
        return Collections.min(timeEntries);
    }


    // Get The Latest Date
    private static Date getLatestDate(List<Date> timeEntries) {
        return Collections.max(timeEntries);
    }


    // Get number of generations
    private static int getNumOfGeneration(int start, int end, int interval) {
        return (end-start)/interval;
    }


    static int findCeil(int arr[], int r, int l, int h)
    {
        int mid;
        while (l < h)
        {
            mid = l + ((h - l) >> 1); // Same as mid = (l+h)/2
            if(r > arr[mid])
                l = mid + 1;
            else
                h = mid;
        }
        return (arr[l] >= r) ? l : -1;
    }

    // The main function that returns a random number
    // from arr[] according to distribution array
    // defined by freq[]. n is size of arrays.
    static int myRand(int arr[], int freq[], int n)
    {
        // Create and fill prefix array
        int prefix[] = new int[n], i;
        prefix[0] = freq[0];
        for (i = 1; i < n; ++i)
            prefix[i] = prefix[i - 1] + freq[i];

        // prefix[n-1] is sum of all frequencies.
        // Generate a random number with
        // value from 1 to this sum
        int r = ((int)(Math.random()*(323567)) % prefix[n - 1]) + 1;

        // Find index of ceiling of r in prefix arrat
        int indexc = findCeil(prefix, r, 0, n - 1);
        return arr[indexc];
    }

    // Convert Pop[] to int[]
    private static int[] getPopIntArray(String[] strs) {
        int res[] = new int[strs.length];
        for(int i = 0; i < strs.length; ++i) {
            res[i] = translateFromDNA(strs[i]);
        }
        return res;
    }


    private static int[] invertWeights(int[] fitness, int MAX){
        for(int i = 0; i < fitness.length; ++i) {
            fitness[i] = MAX - fitness[i];
        }
        return fitness;
    }


    private static String concatTwoStrings(String s1, String s2, int concatPoint){
        String sub1 = s1.substring(0,concatPoint);
        String sub2 = s2.substring(concatPoint);
        return sub1+sub2;
    }


    // getting the Minimum value
    private static int getMinValue(int[] array) {
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }


    // get hour from total second
    private static int getHours(int totalSecond){
        return totalSecond / 3600;
    }


    // get minute from total second
    private static int getMinutes(int totalSecond, int hours){
        return (totalSecond - hours * 3600) / 60;
    }


    // get minute from total second
    private static int getSeconds(int totalSecond, int hours, int minutes){
        return (totalSecond - hours * 3600) - minutes * 60;
    }


    // get hh:mm:ss of current Date
    private static Date getDate(int hours, int minutes, int seconds){
        Date currDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        Date res = new Date(year-1900, month, day, hours, minutes, seconds);

        return res;
    }


}
