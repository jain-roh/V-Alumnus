package v_alumnus.vkronus.edu.v_alumnus.helper;

/**
 * Created by Rohit on 2/17/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "alumni_notify";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String FRIEND_REQUEST="friendRequest";
    private static final String VECTOR_NOTIFY="vector";
    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addNotificationRequest(String notification) {

        // get old notifications
        String oldNotifications = getNotificationsRequest();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(FRIEND_REQUEST, oldNotifications);
        editor.commit();
    }

    public String getNotificationsRequest() {
        return pref.getString(FRIEND_REQUEST,null);
    }


    public void addNotificationVector(String notification) {

        // get old notifications
        String oldNotifications = getNotificationsVector();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(VECTOR_NOTIFY, oldNotifications);
        editor.commit();
    }

    public String getNotificationsVector() {
        return pref.getString(VECTOR_NOTIFY, null);
    }




    public void clear() {
        editor.clear();
        editor.commit();
    }
}
