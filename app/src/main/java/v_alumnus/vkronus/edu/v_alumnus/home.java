package v_alumnus.vkronus.edu.v_alumnus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import v_alumnus.vkronus.edu.v_alumnus.app.Config;
import v_alumnus.vkronus.edu.v_alumnus.gcm.GcmIntentService;


public class home extends ActionBarActivity {
    Button signup,login;
    String usr="-1",pwd=null,type=null;
    private String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
   /*GCM REGISTRATion

         */
        SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS UserLogin(Username VARCHAR,Password VARCHAR,Type VARCHAR);");
        //mydatabase.execSQL("INSERT INTO UserLogin VALUES('admin','admin');");

        Cursor res =  mydatabase.rawQuery("select * from UserLogin", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            usr=res.getString(res.getColumnIndex("Username"));
            pwd=res.getString(res.getColumnIndex("Password"));
            type=res.getString(res.getColumnIndex("Type"));
            res.moveToNext();
        }
        mydatabase.close();
        if(usr.equals("-1")==false)
        {
            Intent r=new Intent(home.this,MainActivity.class);
            r.putExtra("type",8);
            r.putExtra("usr",usr);
            r.putExtra("pswd",pwd);
            r.putExtra("utype", type);
            startActivity(r);
            finish();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");


                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }



        //End of GCM

        signup=(Button)findViewById(R.id.signup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        actionBar.hide();
        login=(Button)findViewById(R.id.login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent su=new Intent(home.this, Signin.class);
                startActivity(su);
            }
        });
       login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent su=new Intent(home.this, Login.class);
                startActivity(su);
            }
        });

    }
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
