package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Alumni extends ActionBarActivity {
String aid;
    CircleImageView pp;
    String name,propic;
    int frndval,frndval2;
    GlobalClass globalClass;
    String msgs="";
    Bitmap img;
    Button frndreq,msgsend,frndnm;
    LinearLayout llsendmsg,chats;
    EditText msg;
    SQLiteDatabase mydatabase;
    private final int FIVE_SECONDS = 5000;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        getSupportActionBar().hide();

        globalClass=(GlobalClass)getApplicationContext();
        pp=(CircleImageView)findViewById(R.id.dpimg);
        chats=(LinearLayout)findViewById(R.id.chats);
        frndnm=(Button)findViewById(R.id.frndnm);

        final Bundle bundle = getIntent().getExtras();
        aid = bundle.getString("AID");
        frndnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bundle.getString("utype").equals("faculty"))
                {
                    Intent r=new Intent(Alumni.this,Faculty.class);
                    r.putExtra("FID",aid);

                    startActivity(r);
                }
                else
                {

                }
            }
        });
        GlobalClass.setAUsr(aid);
        llsendmsg=(LinearLayout)findViewById(R.id.ll_send_message);

        mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
        final Handler handler=new Handler();
        GlobalClass.setChatOpen(0);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (GlobalClass.getChatOpen() == 1) {
                    enterIntoTable();
                    GlobalClass.setChatOpen(0);
                }

                handler.postDelayed(this, 5000);
            }
        }, 5000);


        propic="";
        msg=(EditText)findViewById(R.id.editMessage);
        msgsend=(Button)findViewById(R.id.buttonSend);
        enterIntoTable();

        msgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msg.getText().toString().equals(" ")||msg.getText().toString().equals("") || msg.getText().toString()==null)
                {
                    msg.setError("Please Type Message");
                }
                else {
                    try {
                        String query = URLEncoder.encode(msg.getText().toString(), "utf-8");
                        new SendMsg().execute(query);

                    }
                    catch(Exception e)
                    {

                    }
                }
            }
        });

        frndreq=(Button)findViewById(R.id.frndreq);
        if(aid.equals(globalClass.getUsrName()))
            frndreq.setVisibility(View.GONE);
        new network2().execute();
        if(bundle.getString("utype").equals("alumni"))
        new network4().execute();
        else
            new network5().execute();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alumni, menu);
        return true;
    }




    class network4 extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Alumni.this);
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
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/getalumnidet.php?usrid="+aid);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();        }
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
            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                   name=json_data.getString("alum_name");
                    //ypoint=json_data.getString("alum_total");
                   // mpoint=json_data.getString("alum_month");
                    propic=json_data.getString("alumn_pic");
                    //branch=json_data.getString("alum_branch");
                   // passyear=json_data.getString("pass_year");
                    //bithday=json_data.getString("alumni_dob");
                }
            }
            catch(JSONException e1){
                //   Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);
            pDialog.dismiss();
            try {
                    frndnm.setText(name);
                    new network1().execute("");


                //alumnm.setText(alumname);
                //alumprof.setText(alumproff);
               // alumdsg.setText(alumdesg);
                //alumdsgtype.setText(alumdesgtype);
                //updtb.setOnClickListener(new View.OnClickListener() {
                    /*@Override
                    public void onClick(View v) {
                        if(alumnm.getText().toString().equals(alumname) && alumprof.getText().toString().equals(alumproff) && alumdsg.getText().toString().equals(alumdesg) &&alumdsgtype.getText().toString().equals(alumdesgtype))
                        {
                            Toast.makeText(getBaseContext(), "No Change in data", Toast.LENGTH_SHORT).show();
                        }
                        else if(alumnm.getText().toString().trim().equals(""))
                            alumnm.setError("Alumni Name Cannot be Empty");
                        else if(alumprof.getText().toString().trim().equals(""))
                            alumprof.setError("Alumni Name Cannot be Empty");
                        else
                            new network2().execute("");
                    }
                });*/
                // TextView b=new TextView(ThreadRead.this);
               // new network3().execute("");




            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }
    class network1 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                if(propic.equals("") && propic.equals("null"))
                    img=BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                else
                    img= BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + propic).getContent());

            }
            catch (Exception e) {
                return e.getMessage().toString();
            }
            return null;
        }
        protected void onPostExecute(String feed) {
            pp.setImageBitmap(img);
        }
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



    class network2 extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            JSONArray jArray = null;
            String result = null;
            StringBuilder sb = null;
            InputStream is = null;
            List r=new ArrayList();
            ArrayList nameValuePairs = new ArrayList();
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/friendcheck.php?usrid="+aid+"&myid="+globalClass.getUsrName());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();        }
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
            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    frndval=json_data.getInt("checkd");
                     }
            }
            catch(JSONException e1){
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            try {
                if(frndval==1) {
                    frndreq.setText("Connection Requested");
                    reqFrnd("Cancel Request");
                }
                else if(frndval==2) {
                    frndreq.setText("Respond");
                    reqFrnd("Accept Connection Request");
                }
                else if(frndval==3 || frndval==4) {
                    frndreq.setText("Remove");
                    reqFrnd("Remove from the Connection");
                    llsendmsg.setVisibility(View.VISIBLE);
                }
                else {
                    frndreq.setText("Add");
                    reqFrnd("Send Connection Request");

                }
            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }

        }
    }
    public void reqFrnd(final String arg) {

        frndreq.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        new network3().execute();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE: {
                        if (frndval == 2) {
                            frndval = 7;
                            new network3().execute();
                        }
                    }
                        break;
                }
            }
        };
                AlertDialog.Builder builder = new AlertDialog.Builder(Alumni.this);
                builder.setMessage(arg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }}
            );
    }



    class network3 extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Alumni.this);
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
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2

                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/friendrequest.php?usrid="+aid+"&myid="+globalClass.getUsrName()+"&frndval="+frndval);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();

                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();        }
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
            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    frndval2=Integer.parseInt(json_data.getString("checkdf"));
                }
            }
            catch(JSONException e1){
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            try {                pDialog.cancel();

                if(frndval2==1) {
                    Toast.makeText(getBaseContext(), "Connection Requested", Toast.LENGTH_LONG).show();
                    frndreq.setText("Connection Requested");
                    frndval=1;
                }
                else if(frndval2==0){
                    Toast.makeText(getBaseContext(), "Error in Connecting Request"+frndval2, Toast.LENGTH_LONG).show();

                }
                else if(frndval2==11)
                {
                    Toast.makeText(getBaseContext(), "Connection Request Denied", Toast.LENGTH_LONG).show();
                    frndreq.setText("Add");
                    frndval=0;
                }
                else if(frndval2==10)
                {
                    Toast.makeText(getBaseContext(), "Error while denying the request", Toast.LENGTH_LONG).show();

                }
                else if(frndval2==21)
                {
                    Toast.makeText(getBaseContext(), "Connection Request Accepted", Toast.LENGTH_LONG).show();
                    frndreq.setText("Remove");
                    new network2().execute();
                }
                else if(frndval2==20)
                {
                    Toast.makeText(getBaseContext(), "Error while Accepting the Request", Toast.LENGTH_LONG).show();

                }
                else if(frndval2==71)
                {
                    Toast.makeText(getBaseContext(), "Connection Ignored", Toast.LENGTH_LONG).show();
                    frndreq.setText("Add");
                    frndval=0;
                }
                else if(frndval2==70)
                {
                    Toast.makeText(getBaseContext(), "Error while ignoring connection", Toast.LENGTH_LONG).show();

                }
                else if(frndval2==31)
                {
                    Toast.makeText(getBaseContext(), "Connection Removed", Toast.LENGTH_LONG).show();
                    frndreq.setText("Add");
                    frndval=0;
                }
                else if(frndval2==30)
                {
                    Toast.makeText(getBaseContext(), "Error while removing connection", Toast.LENGTH_LONG).show();

                }
                else if(frndval2==41)
                {
                    Toast.makeText(getBaseContext(), "Connection Removed", Toast.LENGTH_LONG).show();
                    frndreq.setText("Add");
                    frndval=0;
                }
                else if(frndval2==40)
                {
                    Toast.makeText(getBaseContext(), "Error while removing connection", Toast.LENGTH_LONG).show();

                }


            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }

        }
    }







    class SendMsg extends AsyncTask<String, Void, String> {
        String name="";
        String msgss="";
        int id=-1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Alumni.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {
            msgss=urls[0];
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
                    HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/sendmsg.php");
                    nameValuePairs.add(new BasicNameValuePair("aid",aid));
                    nameValuePairs.add(new BasicNameValuePair("myid", GlobalClass.getUsrName()));
                    nameValuePairs.add(new BasicNameValuePair("msg",urls[0]));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpParams httpParameters = new BasicHttpParams();

                    int timeoutConnection = 2000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
                    int timeoutSocket = 2000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);


                    //                HttpParams httpParameters = new BasicHttpParams();

                    //int timeoutConnection = 3000;
                    //HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
                   // int timeoutSocket = 5000;
                   // HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
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
                        id=Integer.parseInt(json_data.getString("checkdc"));
                        //   name=json_data.getString("alum_name");
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

                System.out.println("e3:" + e.toString());
                return e.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
                pDialog.dismiss();
                if(id==-1)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new SendMsg().execute(msgss);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {
                                    finish();
                                }
                                break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(Alumni.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else if(id==0)
                {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            else
                {
                    String dbtbl="vchat_"+GlobalClass.getAlumId();
                    mydatabase.execSQL("INSERT INTO "+dbtbl+" VALUES('1','" + GlobalClass.getUsrName() + "','" + aid + "','" + msgss + "','datetime()','1')");

                    RelativeLayout l=new RelativeLayout(Alumni.this);
                    TextView r=new TextView(Alumni.this);
                    l.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    r.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)r.getLayoutParams();
                    l.setPadding(20, 5, 10, 5);
                    r.setBackgroundColor(Color.parseColor("#FFA8EDB0"));
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                   // Display display = getWindowManager().getDefaultDisplay();
                   // Point size = new Point();
                   // display.getSize(size);
                   // r.setMaxWidth(size.x - 10);
                    //r.setMinWidth(100);
                    r.setLayoutParams(params);
                    r.setMinWidth(25);
                    r.setText(URLDecoder.decode(msgss));
                    r.setTextColor(Color.BLACK);

                    //   Display display = getWindowManager().getDefaultDisplay();
                    // Point size = new Point();
                    // display.getSize(size);
                    // r.setMaxWidth(size.x - 10);
                    //r.setMinWidth(100);
                    r.setMinimumHeight(60);
                    r.setPadding(7, 7, 10, 7);

                    r.setGravity(View.FOCUS_RIGHT);
                    l.addView(r);
                    chats.addView(l);
                    msg.setText("");
                }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }


    public void enterIntoTable()
    {
        chats.removeAllViewsInLayout();
        String dbtbl="vchat_"+GlobalClass.getAlumId();
        Cursor res =  mydatabase.rawQuery("select * from " + dbtbl +" where (friend1='"+aid+"' AND friend2='"+GlobalClass.getUsrName()+"') OR (friend2='"+aid+"' AND friend1='"+GlobalClass.getUsrName()+"')", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){

            RelativeLayout l=new RelativeLayout(Alumni.this);
            TextView r=new TextView(Alumni.this);
            l.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            r.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)r.getLayoutParams();

            if(res.getString(res.getColumnIndex("friend1")).equals(GlobalClass.getUsrName())){
                l.setPadding(50, 5, 10, 5);
                r.setBackgroundColor(Color.parseColor("#FFA8EDB0"));
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            } else {
                l.setPadding(10, 5, 50, 5);
                r.setBackgroundColor(Color.WHITE);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


            }
            r.setLayoutParams(params);
            r.setMinWidth(25);
            try {
                r.setText(URLDecoder.decode(res.getString(res.getColumnIndex("msg")), "utf-8"));
            }
            catch (Exception e) {

            }
            r.setTextColor(Color.BLACK);

            //   Display display = getWindowManager().getDefaultDisplay();
            // Point size = new Point();
            // display.getSize(size);
            // r.setMaxWidth(size.x - 10);
            //r.setMinWidth(100);
            r.setMinimumHeight(60);
            r.setPadding(7, 7, 10, 7);

            r.setGravity(View.FOCUS_RIGHT);
            l.addView(r);
            chats.addView(l);

            res.moveToNext();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalClass.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalClass.activityPaused();
    }



    class network5 extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Alumni.this);
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
            try{
                HttpClient httpclient = new DefaultHttpClient();
                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/getfacdet.php?usrid="+aid);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();
            }
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

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    name=json_data.getString("fac_name");
                    propic=json_data.getString("alumn_pic");


                }}
            catch(JSONException e1){
                //   Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);
            pDialog.dismiss();
            try {
                frndnm.setText(name);
                new network1().execute("");

                //alumnm.setText(alumname);
                //alumprof.setText(alumproff);
                // alumdsg.setText(alumdesg);
                //alumdsgtype.setText(alumdesgtype);
                //updtb.setOnClickListener(new View.OnClickListener() {
                    /*@Override
                    public void onClick(View v) {
                        if(alumnm.getText().toString().equals(alumname) && alumprof.getText().toString().equals(alumproff) && alumdsg.getText().toString().equals(alumdesg) &&alumdsgtype.getText().toString().equals(alumdesgtype))
                        {
                            Toast.makeText(getBaseContext(), "No Change in data", Toast.LENGTH_SHORT).show();
                        }
                        else if(alumnm.getText().toString().trim().equals(""))
                            alumnm.setError("Alumni Name Cannot be Empty");
                        else if(alumprof.getText().toString().trim().equals(""))
                            alumprof.setError("Alumni Name Cannot be Empty");
                        else
                            new network2().execute("");
                    }
                });*/
                // TextView b=new TextView(ThreadRead.this);
                // new network3().execute("");




            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }


}



