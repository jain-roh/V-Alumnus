package v_alumnus.vkronus.edu.v_alumnus.app;

/**
 * Created by Rohit on 2/17/2016.
 */
public class Config {

    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessagesRequest = false;
    public static boolean appendNotificationMessagesVector = false;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_IDREQ = 100;

    public static final int NOTIFICATION_IDVEC = 101;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 102;
}
