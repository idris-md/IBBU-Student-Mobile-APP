package com.ibbumobile;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.Random;

public class NotificationTrigger extends IntentService {

    private static final int uniqueID = 5127;
    private static final String ALARM = "your.package.ALARM";

    public NotificationTrigger() {
        super("NotificationTrigger");
    }

    public static void starNotification(Context context) {

        Intent intent = new Intent(context, NotificationTrigger.class);
        intent.setAction(ALARM);
        context.startService(intent);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ALARM.equals(action)) {
                handleAlarm();
            }
        }
    }

    private void handleAlarm() {

        String quote = randomQuote();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Toast.makeText(getBaseContext(), "Alarm is here", Toast.LENGTH_LONG);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), QuoteActivity.class);
        intent.putExtra("quote", quote);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("CSC 2018 Daily Quote")
                .setContentText(quote)
                .setSmallIcon(R.drawable.ibbx)
                .setContentIntent(pendingIntent)
                .setSound(sound)
                .addAction(0, "Read Quote", pendingIntent)
                .addAction(0, "Dismiss", null)
                .build();

        notificationManager.notify(uniqueID, notification);

        //Toast.makeText(this, "Did you see notification", Toast.LENGTH_LONG).show();
    }

    private String randomQuote() {

        Random random = new Random();

        String[] quotes = {
                "Be Diligent about yourself apply yourself motivate yourself do it for ourself ",
                "Get up and code",
                "Our deepest strength is not when we are apart but when we come together in ways we uniquely can",
                "Do not hold back what it is that o have a unique ability to offer that's the kind of goodness u whant to be able to harness",
                "You're what you repeatedly do",
                "Good isn't good enough if it can be better and better isn't good enough it can be best",
                "Computer Science isn't about screen time, it's about BRAIN time."
        };

        int index = random.nextInt(quotes.length);
        String selectedQuote = quotes[index];

        return selectedQuote;

    }
}