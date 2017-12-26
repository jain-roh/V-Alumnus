package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.app.Config;
import v_alumnus.vkronus.edu.v_alumnus.gcm.GcmIntentService;
import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;
import v_alumnus.vkronus.edu.v_alumnus.util.LoginUser;

public class MainActivity extends ActionBarActivity {
    private String[] mOptionMenu;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelativeLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitleSection;
    private CharSequence mTitleApp;
    private Fragment mFragment = null;
    ProgressDialog pDialog;
   LoginUser l;String usr,pswd,token,typel;
    Bitmap bitmap;
    Button news,disc,gal,events,dir,birthday;
    String name,type;
    CircleImageView propic;
    Bundle bundle;
    TextView nmusr;

    int REQUEST_EXIT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
       bundle=getIntent().getExtras();
        propic=(CircleImageView)findViewById(R.id.image_view);
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,ChangePicture.class);
                startActivityForResult(r, 1);
            }
        });
    nmusr=(TextView)findViewById(R.id.nmusr);
            try {
            token= GcmIntentService.tokenno;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(bundle.getInt("type")==7)
        {
            propic.setImageBitmap(GlobalClass.getBitimg());
            basicDet();
            createdb();
            nmusr.setText(GlobalClass.getName());

        }
        else if(bundle.getInt("type")==1 || bundle.getInt("type")==2)
        {
            SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
            Cursor res =  mydatabase.rawQuery("select * from UserLogin", null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                usr=res.getString(res.getColumnIndex("Username"));
                pswd=res.getString(res.getColumnIndex("Password"));
                typel=res.getString(res.getColumnIndex("Type"));

                res.moveToNext();
            }


            if(typel.equals("alumni"))
                new network5().execute();
            else
                new network6().execute();
        }

        else if(bundle.getInt("type")==8)
        {
            usr=bundle.getString("usr");
            pswd=bundle.getString("pswd");
            typel=bundle.getString("utype");
            if(typel.equals("alumni"))
                new network5().execute();
            else
                new network6().execute();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));


        news=(Button)findViewById(R.id.News);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,vectornews.class);
                startActivity(r);
            }
        });

        gal=(Button)findViewById(R.id.gallery);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,Gallery.class);
                startActivity(r);
            }
        });
        disc=(Button)findViewById(R.id.Discussion);
        disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,Discussion.class);
                startActivity(r);
            }
        });
       events=(Button)findViewById(R.id.event);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,event.class);
                startActivity(r);
            }
        });
        dir=(Button)findViewById(R.id.directory);
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,Directory.class);
                startActivity(r);
            }
        });

        birthday=(Button)findViewById(R.id.birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(MainActivity.this,birthday.class);
                startActivity(r);
            }
        });

        mOptionMenu = new String[] { "Profile","FriendList","FriendRequest",
                "Logout"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
                .getThemedContext(), R.layout.mytextview,
                mOptionMenu));
       initContentWithFirstFragment();

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0: {
                        if(GlobalClass.getType().equals("alumni")) {

                            Intent r = new Intent(MainActivity.this, AlumniUpdate.class);
                            startActivity(r);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"No Such Feature to Faculty",Toast.LENGTH_SHORT).show();

                    }
                        break;
                    case 1: {
                        Intent r = new Intent(MainActivity.this, FriendList.class);
                        startActivity(r);
                    }
                    break;

                    case 2: {
                            Intent r = new Intent(MainActivity.this, FriendRequest.class);
                            startActivity(r);
                        }
                        break;
                    case 3:
                    {
                        SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
                        mydatabase.delete("UserLogin", null, null);
                        Intent r=new Intent(MainActivity.this,Login.class);
                        finish();
                        startActivity(r);
                    }
                    break;
                    case 4:
                    {

                    }
                    break;
                    default:
                        Toast.makeText(getBaseContext(),"Some Problem while Connecting",Toast.LENGTH_SHORT).show();
                        break;

                }

                /*FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mFragment).commit();
*/
                mDrawerList.setItemChecked(position, true);

                mTitleSection = mOptionMenu[position];
                getSupportActionBar().setTitle("Home");

                mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
            }
        });
        mDrawerList.setItemChecked(0, true);
        mTitleSection = "Home";
        mTitleApp = getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitleSection);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.finish();
                final Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void basicDet()
    {
        name=GlobalClass.getUsrName();
        if(name==null)
            finish();
        type=GlobalClass.getType();
        if(type==null)
            finish();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void initContentWithFirstFragment(){

        mTitleSection =getString(R.string.first_fragment);
        getSupportActionBar().setTitle(mTitleSection);
     }

    class network5 extends AsyncTask<String, Void, String> {
    String name="",img="";
        int id=-1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;

            InputStream is = null;
            List r=new ArrayList();


            ArrayList nameValuePairs = new ArrayList();
//http post
            try {


                try{
                    HttpClient httpclient = new DefaultHttpClient();

                    //Why to use 10.0.2.2
                    HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/alumnilogin.php");
                    nameValuePairs.add(new BasicNameValuePair("usr",usr));
                    nameValuePairs.add(new BasicNameValuePair("pwd",pswd));
                    nameValuePairs.add(new BasicNameValuePair("token",token));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpParams httpParameters = new BasicHttpParams();

                    int timeoutConnection = 3000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
                    int timeoutSocket = 5000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    httpclient.getConnectionManager().closeExpiredConnections();
                    httpclient.getConnectionManager().shutdown();
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection" + e.toString());
                    return e.toString();        }
//convert response to string
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    sb = new StringBuilder();
                    String line=null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result=sb.toString();
                }catch(Exception e){
                    Log.e("log_tag", "Error converting result "+e.toString());
                }

                try {
                    jArray = new JSONArray(result);
                    JSONObject json_data=null;
                    for(int i=0;i<jArray.length();i++){
                        json_data = jArray.getJSONObject(i);
                        id=Integer.parseInt(json_data.getString("alum_id"));
                      name=json_data.getString("alum_name");
                         img=json_data.getString("alumn_pic");
                        if(img.equals("null"))
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                        else
                            bitmap= BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + img).getContent());
                    }
                }
                catch(JSONException e1){
                    System.out.println("e1:"+e1.toString());

                    return e1.toString();
                } catch (Exception e1) {
                    System.out.println("e2:"+e1.toString());
                    return e1.toString();
                }
            }
            catch(Exception e)
            {

                System.out.println("e3:"+e.toString());
                return e.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            try {
                if(id!=0 && id!=-1)
                {

                    GlobalClass.setUsrName(usr);
                    GlobalClass.setPwd(pswd);
                    GlobalClass.setBitimg(bitmap);
                    GlobalClass.setType("alumni");
                    GlobalClass.setImgType("thumb");
                    GlobalClass.setAlumId("" + id);
                    GlobalClass.setImgName(img);
                    GlobalClass.setName(name);
                    nmusr.setText(GlobalClass.getName());

                    propic.setImageBitmap(GlobalClass.getBitimg());
                    createdb();
                    basicDet();
                    if(bundle.getInt("type")==1)
                    {
                        Intent r=new Intent(MainActivity.this,FriendRequest.class);
                        startActivity(r);
                    }
                    else if(bundle.getInt("type")==2)
                    {
                        Intent r=new Intent(MainActivity.this,NewsDetails.class);
                        r.putExtra("NID",bundle.getString("uniqid"));
                        startActivity(r);
                    }
                }
                else if(id==-1) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new network5().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {
                                    finish();
                                }
                                break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Wrong Username or Password",Toast.LENGTH_LONG).show();
                    Intent r= new Intent(MainActivity.this,home.class);
                    startActivity(r);
                    finish();
                }
                pDialog.dismiss();

            }catch (Exception e)
            {
                pDialog.dismiss();
                System.out.print(e.getMessage());
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }


    class network6 extends AsyncTask<String, Void, String> {
        int id=-1;
        String img;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;

            InputStream is = null;
            List r=new ArrayList();


            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//http post
            try {


                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    //Why to use 10.0.2.2
                    HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/faclogin.php?");
                    nameValuePairs.add(new BasicNameValuePair("usr",usr));
                    nameValuePairs.add(new BasicNameValuePair("pwd",pswd));
                    nameValuePairs.add(new BasicNameValuePair("token",token));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection" + e.toString());
                    return e.toString();
                }
//convert response to string
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result " + e.toString());
                }

                try {
                    jArray = new JSONArray(result);
                    JSONObject json_data = null;
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        id = Integer.parseInt(json_data.getString("alum_id"));
                        name=json_data.getString("alum_name");
                        img = json_data.getString("alumn_pic");
                        if (img.equals("null"))
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                        else
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + img).getContent());
                        //here "Name" is the column name in database
                    }
                } catch (JSONException e1) {
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return e1.toString();
                }
            }
            catch(Exception e)
            {
            }
            return null;
        }

        protected void onPostExecute(String feed) {

            try {
                if(id!=-1 && id!=0)
                {
                    //Toast.makeText(getBaseContext(),"Successful Logged In",Toast.LENGTH_LONG).show();
                    GlobalClass.setUsrName(usr);
                    GlobalClass.setPwd(pswd);
                    GlobalClass.setBitimg(bitmap);
                    GlobalClass.setImgType("thumb");
                    GlobalClass.setImgName(img);
                    GlobalClass.setType("faculty");
                    propic.setImageBitmap(GlobalClass.getBitimg());
                    GlobalClass.setAlumId("" + id);
                    GlobalClass.setName(name);
                    nmusr.setText(GlobalClass.getName());
                    createdb();

                    if(bundle.getInt("type")==1)
                    {
                        Intent r=new Intent(MainActivity.this,FriendRequest.class);
                        startActivity(r);
                    }
                    else if(bundle.getInt("type")==2)
                    {
                        Intent r=new Intent(MainActivity.this,NewsDetails.class);
                        r.putExtra("NID",bundle.getString("uniqid"));
                        startActivity(r);
                    }
                    // Toast.makeText(getBaseContext(),globalVariable.getType(),Toast.LENGTH_SHORT).show();
                }
                else if(id==-1) {

                    Toast.makeText(getBaseContext(), "Error In Connection", Toast.LENGTH_LONG).show();
                    new network6().execute();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Wrong Username or Password",Toast.LENGTH_LONG).show();
                    Intent r= new Intent(MainActivity.this,home.class);
                    startActivity(r);
                    finish();
                }
                pDialog.dismiss();

            }catch (Exception e)
            {
                pDialog.dismiss();

            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }

    public void createdb()
    {
        String dbtbl="vchat_"+GlobalClass.getAlumId();
        SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+dbtbl+"(id VARCHAR,friend1 VARCHAR,friend2 VARCHAR,msg TEXT,created_at DATETIME DEFAULT CURRENT_TIMESTAMP,delmsg INT);");

    }

}
