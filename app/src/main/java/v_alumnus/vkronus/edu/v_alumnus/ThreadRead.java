package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;


public class ThreadRead extends ActionBarActivity {
String tid;
    String desc[],commentortype[],commentdate[];
    String mainhead=null,maindesc,mainname,maindate,maintype;
    int mainlikers,mainviews;
    int c;
    Button off,cmnt,date,creator,like,view;
    ArrayList<String> commentbyy,commentby;
       String name,type,cmntu;
    EditText cmnttext;
   GlobalClass globalVariable;
    Dialog dialog;
    LinearLayout ltp;
    int offset;
    TextView t1,t2;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_read);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        Bundle bundle = getIntent().getExtras();
        tid = bundle.getString("TID");
        offset=0;
        //Toast.makeText(getBaseContext(),tid+"",Toast.LENGTH_LONG).show();
        ltp=(LinearLayout)findViewById(R.id.cmnt);
        t1=(TextView)findViewById(R.id.titlehead);
        t2=(TextView)findViewById(R.id.titledesc);
        off=(Button)findViewById(R.id.next10);
        date=(Button)findViewById(R.id.date);
      globalVariable = (GlobalClass) getApplicationContext();
        name=globalVariable.getUsrName();
        type=globalVariable.getType();
        creator=(Button)findViewById(R.id.almfac);
        view=(Button)findViewById(R.id.Views);
        like=(Button)findViewById(R.id.like);
        commentbyy=new ArrayList<String>();
        commentby=new ArrayList<String>();

        c=0;
        try {
            cmnt = (Button) findViewById(R.id.comnt);

            cmnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(ThreadRead.this);
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setContentView(R.layout.commentthread);
                    dialog.setTitle("Comment On Thread");

                    cmnttext = (EditText) dialog.findViewById(R.id.cmnttext);
                    Button save = (Button) dialog.findViewById(R.id.save);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cmnttext.getText().toString().equals(""))
                                Toast.makeText(getBaseContext(), "Comment Field is Empty", Toast.LENGTH_LONG).show();
                            else {
                                try {
                                    cmntu = URLEncoder.encode(cmnttext.getText().toString(), "utf-8");
                                }
                                catch(Exception e)
                                {

                                }
                                if (globalVariable.isOnline())
                                    new network3().execute("", "", "");
                                else
                                    Toast.makeText(getBaseContext(), "No Connection", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                    dialog.show();

                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (globalVariable.isOnline()) {
                        new network5().execute("", "", "");
                         } else
                        Toast.makeText(getBaseContext(), "No Connection", Toast.LENGTH_LONG).show();


                }
            });
            like.setEnabled(false);
            off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (globalVariable.isOnline())
                        new network2().execute("", "", "");
                }
            });
            if (globalVariable.isOnline()) {
                  new network1().execute("", "", "");
            } else
                Toast.makeText(getBaseContext(), "No Connection", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thread_read, menu);
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

    class network1 extends AsyncTask<String, Void, String> {
        int chk=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThreadRead.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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

                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/threadinfo.php?id="+tid);
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


            }
            catch (UnknownHostException e)
            {
                Toast.makeText(getBaseContext(),"Connection Was Not Established" , Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
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

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    mainhead=json_data.getString("thread_title");
                    maindesc=json_data.getString("thread_desc");
                    mainname=json_data.getString("thread_creator");
                    maintype=json_data.getString("thread_aof");
                    maindate=json_data.getString("thread_date");
                    mainviews=Integer.parseInt(json_data.getString("thread_views"));
                    mainlikers=Integer.parseInt(json_data.getString("thread_likers"));
                    chk=1;
                    //name=json_data.getString("NAME");//here "Name" is the column name in database
                    //return name;
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
            pDialog.dismiss();
            if(chk==0)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                new network1().execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE: {
                                finish();
                            }
                            break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ThreadRead.this);
                builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
            else if (mainhead != null) {
                try {
                    t1.setText(URLDecoder.decode(mainhead, "utf-8"));
                    t2.setText(URLDecoder.decode(maindesc, "utf-8"));
                }
                catch(Exception e)
                {

                }
                date.setText("created on :" + maindate);
                creator.setText("by : " + mainname);
                creator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (maintype.equals("alumni")) {
                            Intent r1 = new Intent(ThreadRead.this, Alumni.class);
                            r1.putExtra("AID", mainname);
                            r1.putExtra("utype","alumni");

                            startActivity(r1);
                        } else {
                            Intent r1 = new Intent(ThreadRead.this, Alumni.class);
                            r1.putExtra("AID", mainname);
                            r1.putExtra("utype","faculty");
                            startActivity(r1);
                        }

                    }
                });
                mainviews = mainviews + 1;
                view.setText("View : " + mainviews);
                like.setText(like.getText().toString() + "(" + mainlikers + ")");


                new network2().execute("", "", "");
            }
            else
                Toast.makeText(getBaseContext(),"Connection Problem",Toast.LENGTH_LONG).show();

        }


    }


    class network2 extends AsyncTask<String, Void, String> {
       @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThreadRead.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;
            desc=null;
            StringBuilder sb = null;



            InputStream is = null;
            List r=new ArrayList();


            ArrayList nameValuePairs = new ArrayList();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/threadcomment.php?id="+tid+"&offset="+offset+"&mainview="+mainviews);
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


            }
            catch (UnknownHostException e)
            {
                Toast.makeText(getBaseContext(),"Connection Was Not Established" , Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
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

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                //  head=new String[jArray.length()];
                desc=new String[jArray.length()];
                commentdate=new String[jArray.length()];
                commentortype=new String[jArray.length()];
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    //  head[i]=json_data.getString("comment_title");
                    desc[i]=json_data.getString("comment_desc");
                    commentby.add(json_data.getString("comment_by"));

                    commentdate[i]=json_data.getString("comment_date");

                    commentbyy.add(json_data.getString("commentor_type"));
                }
            }
            catch(JSONException e1){
                //Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                //Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
           pDialog.dismiss();

            if(desc==null && offset>0)
                Toast.makeText(getBaseContext(),"No More Comments",Toast.LENGTH_LONG).show();
                else if(desc==null)
                Toast.makeText(getBaseContext(),"No Comments Yet",Toast.LENGTH_LONG).show();
            else
            {
                for(int i=0;i<desc.length;i++)
                {
                    LinearLayout l=new LinearLayout(ThreadRead.this);


                    l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setWeightSum(2);
                    Button bd=new Button(ThreadRead.this);
                    Button b=new Button(ThreadRead.this);
                    bd.setText("created on : " + commentdate[i] + " by : ");
                    LinearLayout.LayoutParams p1=new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
                    p1.weight=1;

                    bd.setLayoutParams(p1);

                    bd.setTextSize(10);
                    bd.setBackgroundColor(Color.parseColor("#000000"));
                    bd.setTextColor(Color.parseColor("#ffffff"));
                    b.setText("" + commentby.get(c));
                    b.setTextSize(10);
                    b.setBackgroundColor(Color.parseColor("#000000"));
                    b.setTextColor(Color.parseColor("#ffffff"));

                    b.setLayoutParams(p1);
                    b.setId(c);
                    c++;
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (commentbyy.get(v.getId()).equals("alumni")) {
                                Intent r1 = new Intent(ThreadRead.this, Alumni.class);
                                r1.putExtra("AID", commentby.get(v.getId()));
                                r1.putExtra("utype","alumni");
                                startActivity(r1);
                            } else {
                                Intent r1 = new Intent(ThreadRead.this, Alumni.class);
                                r1.putExtra("AID", commentby.get(v.getId()));
                                r1.putExtra("utype","faculty");
                                startActivity(r1);
                            }
                        }});

                    // TextView b=new TextView(ThreadRead.this);
                    TextView b1=new TextView(ThreadRead.this);

                    // b.setText(head[i]);
                    b1.setText(URLDecoder.decode(desc[i]));
                    // b.setTextSize(16);
                    b1.setTextSize(14);
                    // b.setMinimumHeight(20);

                    // b.setBackgroundColor(Color.parseColor("#B9C3E9"));
                    b1.setBackgroundColor(Color.parseColor("#DBE2FE"));
                    //b.setTextColor(Color.parseColor("#000000"));
                    b1.setTextColor(Color.parseColor("#000000"));
                    //b.setId(i);
                    b1.setId(i);
                    //b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                    l.addView(bd);
                    l.addView(b);
                    ltp.addView(l);
                    ltp.addView(b1);

                }
                offset=offset+desc.length;
                off.setVisibility(View.VISIBLE);
            }
            // TODO: check this.exception
new network4().execute();
            // TODO: do something with the feed
        }

    }


    class network4 extends AsyncTask<String, Void, String> {

    /*    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThreadRead.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }*/
int c=0;
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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/threadliker.php?id="+tid+"&usr="+name+"&type="+type);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //Toast.makeText(getBaseContext(),"id="+tid+"&usr="+name+"&type="+type,Toast.LENGTH_LONG).show();
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

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    c=json_data.getInt("liker_id");
                    //name=json_data.getString("NAME");//here "Name" is the column name in database
                    //return name;
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
           // pDialog.dismiss();
        if(c!=0)
        {
            like.setEnabled(false);
            like.setBackgroundColor(Color.parseColor("#FFBDBDBD"));
            //Toast.makeText(getBaseContext(),"There is data",Toast.LENGTH_SHORT).show();
        }
            else{
            like.setEnabled(true);
            like.setBackgroundColor(Color.parseColor("#ff087701"));
        }
        }
        // TODO: check this.exception

        // TODO: do something with the feed

    }



    class network3 extends AsyncTask<String, Void, String> {
        String c="";
       @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThreadRead.this);
            pDialog.setMessage("Check For the Data..");
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


            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/commentonthread.php");
                nameValuePairs.add(new BasicNameValuePair("tid",tid));
                nameValuePairs.add(new BasicNameValuePair("dcp",cmntu ));
                nameValuePairs.add(new BasicNameValuePair("usr",name));
                nameValuePairs.add(new BasicNameValuePair("type",type));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                Toast.makeText(ThreadRead.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ThreadRead.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    c=json_data.getString("check");

                }
            }
            catch(JSONException e1){
                Toast.makeText(ThreadRead.this,e1.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(ThreadRead.this,e1.getMessage(),Toast.LENGTH_SHORT).show();
                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
           pDialog.dismiss();
            if(c.equals("0"))
            Toast.makeText(getBaseContext(),"Error While Commenting",Toast.LENGTH_LONG).show();
            else if(c.equals(""))
                Toast.makeText(getBaseContext(),"Error in Connection",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getBaseContext(),"Comment Posted",Toast.LENGTH_LONG).show();
            dialog.hide();
            }
        }



    class network5 extends AsyncTask<String, Void, String> {

           @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ThreadRead.this);
                pDialog.setMessage("Check For the Data..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        int c=0;
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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/threadlike.php?id="+tid+"&usr="+name+"&type="+type+"&like="+mainlikers+1);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //Toast.makeText(getBaseContext(),"id="+tid+"&usr="+name+"&type="+type,Toast.LENGTH_LONG).show();
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

            try{
                jArray = new JSONArray(result);
                JSONObject json_data=null;

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    c=json_data.getInt("c");

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
            pDialog.dismiss();
            if(c!=0)
            {
                like.setEnabled(false);
                mainlikers=mainlikers+1;
                like.setText("Like"+"("+mainlikers+")");
                like.setBackgroundColor(Color.parseColor("#FFBDBDBD"));
                //Toast.makeText(getBaseContext(),"There is data",Toast.LENGTH_SHORT).show();
            }
            else{
                like.setEnabled(true);
                like.setBackgroundColor(Color.parseColor("#ff087701"));
            }
        }
        // TODO: check this.exception

        // TODO: do something with the feed

    }




}

