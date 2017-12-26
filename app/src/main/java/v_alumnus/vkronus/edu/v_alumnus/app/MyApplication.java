package v_alumnus.vkronus.edu.v_alumnus.app;

/**
 * Created by Rohit on 2/17/2016.
 */
import android.app.Application;

import v_alumnus.vkronus.edu.v_alumnus.helper.MyPreferenceManager;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
}
