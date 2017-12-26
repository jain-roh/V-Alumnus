package v_alumnus.vkronus.edu.v_alumnus.gcm;

/**
 * Created by Rohit on 2/17/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import v_alumnus.vkronus.edu.v_alumnus.Alumni;
import v_alumnus.vkronus.edu.v_alumnus.MainActivity;
import v_alumnus.vkronus.edu.v_alumnus.Signin;
import v_alumnus.vkronus.edu.v_alumnus.app.Config;
import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String extra="";
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String image = bundle.getString("image");
        int type=Integer.parseInt(bundle.getString("type"));
        if(type==2)
        {
            extra=bundle.getString("uniqid");
        }

        if(type==5)
        {
            enterDb(bundle.getString("myid"),bundle.getString("uid"),bundle.getString("mesg"),bundle.getString("dbtbl"));
        }
       // String timestamp = bundle.getString("created_at");
        String timestamp = "Check 1";
        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Title: " + title);
        Log.e(TAG, "message: " + message);
        Log.e(TAG, "image: " + image);
        Log.e(TAG, "timestamp: " + timestamp);

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        } else {

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);
            if(type==1)
            resultIntent.putExtra("type",1);
            else if(type==2)
            {
                resultIntent.putExtra("type",2);
                resultIntent.putExtra("uniqid",extra);
            }

            if (TextUtils.isEmpty(image)) {
                showNotificationMessage(getApplicationContext(),type, title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(),type, title, message, timestamp, resultIntent, image);
            }
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context,int type, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(type,title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context,int type, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(type, title, message, timeStamp, intent, imageUrl);
    }

    public void enterDb(String usrid,String uid,String msg,String dbtbl)
    {
        SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);


        mydatabase.execSQL("INSERT INTO " + dbtbl + " VALUES('1','" + usrid + "','" + uid + "','" + msg + "','datetime()','1')");

        if(GlobalClass.isActivityVisible())
        {
            if(GlobalClass.getAUsr().equals(usrid))
            {
                GlobalClass.setChatOpen(1);
            }
        }
    }

}
