package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;


public class AlumniUpdate extends ActionBarActivity {
String alumname,alumproff,alumdesg,alumdesgtype,name;
    Button updtb,updta;
    EditText alumnm,alumprof,alumdsg,alumdsgtype,alumph1,alumph2,alumeid1,alumeid2,alumadd1,alumadd2,alumcity,alumstate,alumcon,alumpostal;
String alumniph1,alumniph2,alumnieid1,alumnieid2,alumniadd1,alumniadd2,alumnicity,alumnistate,alumnicon,alumnipos;
   private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_update);
        setTitle("Update Profile");
      updtb=(Button)findViewById(R.id.updtb);
        updta=(Button)findViewById(R.id.updta);
        alumnm=(EditText)findViewById(R.id.alumnm);
        alumprof=(EditText)findViewById(R.id.alumproff);
        alumdsg=(EditText)findViewById(R.id.alumdesg);
        alumdsgtype=(EditText)findViewById(R.id.alumdesgtype);
        alumph1=(EditText)findViewById(R.id.alumni_ph1);
        alumph2=(EditText)findViewById(R.id.alumni_ph2);
        alumeid1=(EditText)findViewById(R.id.alumni_eml1);
        alumeid2=(EditText)findViewById(R.id.alumni_eml2);
        alumadd1=(EditText)findViewById(R.id.alumni_add1);
        alumadd2=(EditText)findViewById(R.id.alumni_add2);
        alumcity=(EditText)findViewById(R.id.alumni_city);
        alumstate=(EditText)findViewById(R.id.alumni_state);
        alumcon=(EditText)findViewById(R.id.alumni_country);
        alumpostal=(EditText)findViewById(R.id.alumni_postal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        name=globalVariable.getUsrName();
        new network().execute("");
        new network3().execute("");
    }


    class network extends AsyncTask<String, Void, String> {



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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/getbasicdet.php?usrid="+name);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

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
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    //  head[i]=json_data.getString("comment_title");
                    alumname=json_data.getString("alum_name");
                    alumproff=json_data.getString("alum_proff");
                    alumdesg=json_data.getString("alum_desg");
                    alumdesgtype=json_data.getString("alum_desg_type");

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
            try {
                    alumnm.setText(alumname);
                alumprof.setText(alumproff);
                alumdsg.setText(alumdesg);
                alumdsgtype.setText(alumdesgtype);
                updtb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(alumnm.getText().toString().equals(alumname) && alumprof.getText().toString().equals(alumproff) && alumdsg.getText().toString().equals(alumdesg) &&alumdsgtype.getText().toString().equals(alumdesgtype))
                        {
                            Toast.makeText(getBaseContext(),"No Change in data",Toast.LENGTH_SHORT).show();
                        }
                        else if(alumnm.getText().toString().trim().equals(""))
                            alumnm.setError("Alumni Name Cannot be Empty");
                        else if(alumprof.getText().toString().trim().equals(""))
                            alumprof.setError("Alumni Name Cannot be Empty");
                        else
                            new network2().execute("");
                    }
                });
                        // TextView b=new TextView(ThreadRead.this);




                }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }

    class network2 extends AsyncTask<String, Void, String> {



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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/putbasicdet.php?usrid="+name+"&alumnm="+alumnm.getText().toString().replaceAll(" ","%20")+"&alumprof="+alumprof.getText().toString().replaceAll(" ","%20")+"&alumdesg="+alumdsg.getText().toString().replaceAll(" ","%20")+"&alumdesgtype="+alumdsgtype.getText().toString().replaceAll(" ","%20"));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

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
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    //  head[i]=json_data.getString("comment_title");


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
            try {
                Toast.makeText(getBaseContext(),"Updated Succesfully",Toast.LENGTH_LONG).show();
                // TextView b=new TextView(ThreadRead.this);





            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }




    class network3 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AlumniUpdate.this);
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


            ArrayList nameValuePairs = new ArrayList();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/getadvdet.php?usrid="+name);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

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
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    //  head[i]=json_data.getString("comment_title");
                    alumniph1=json_data.getString("alumni_ph1");
                    alumniph2=json_data.getString("alumni_ph2");
                    alumnieid1=json_data.getString("alumni_eid1");
                    alumnieid2=json_data.getString("alumni_eid2");
                    alumniadd1=json_data.getString("alumni_add1");
                    alumniadd2=json_data.getString("alumni_add2");
                    alumnicity=json_data.getString("alumni_city");
                    alumnistate=json_data.getString("alumni_state");
                    alumnicon=json_data.getString("alumni_country");
                    alumnipos=json_data.getString("alumni_postal");

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
            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);
            try {

              if(alumniph1.equals(""))
                Toast.makeText(getBaseContext(),"Internet Connection Problem",Toast.LENGTH_LONG).show();
              else
              {
                  alumph1.setText(alumniph1);
                  alumph2.setText(alumniph2);
                  alumeid1.setText(alumnieid1);
                  alumeid2.setText(alumnieid2);
                  alumadd1.setText(alumniadd1);
                  alumadd2.setText(alumniadd2);
                  alumcity.setText(alumnicity);
                  alumstate.setText(alumnistate);
                  alumcon.setText(alumnicon);
                  alumpostal.setText(alumnipos);
                  updta.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if(alumph1.getText().toString().equals(alumniph1) && alumph2.getText().toString().equals(alumniph2) && alumeid1.getText().toString().equals(alumnieid1) && alumeid2.getText().toString().equals(alumnieid2) && alumadd1.getText().toString().equals(alumniadd1) && alumadd2.getText().toString().equals(alumniadd2) && alumcity.getText().toString().equals(alumnicity) && alumstate.getText().toString().equals(alumnistate) && alumcon.getText().toString().equals(alumnicon) && alumpostal.getText().toString().equals(alumnipos))
                          {
                              Toast.makeText(getBaseContext(),"No Chnage In Data",Toast.LENGTH_LONG ).show();
                          }
                          else if(alumph1.getText().toString().trim().equals("") || alumeid1.getText().toString().trim().equals("") || alumadd1.getText().toString().trim().equals("") || alumcity.getText().toString().trim().equals("") || alumstate.getText().toString().trim().equals("") || alumcon.getText().toString().trim().equals("") || alumpostal.getText().toString().trim().equals(""))
                          {
                              Toast.makeText(getBaseContext(),"Some Of The Field is empty",Toast.LENGTH_LONG ).show();
                          }
                          else
                          {
                              new network4().execute("");
                          }

                      }
                  });

              }
                // TextView b=new TextView(ThreadRead.this);





            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }



    class network4 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AlumniUpdate.this);
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


            ArrayList nameValuePairs = new ArrayList();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/putadvdet.php?usrid="+name+"&alumph1="+alumph1.getText().toString().replaceAll(" ","%20")+"&alumph2="+alumph2.getText().toString().replaceAll(" ","%20")+"&alumeid1="+alumeid1.getText().toString().replaceAll(" ","%20")+"&alumeid2="+alumeid2.getText().toString().replaceAll(" ","%20")+"&alumadd1="+alumadd1.getText().toString().replaceAll(" ","%20")+"&alumadd2="+alumadd2.getText().toString().replaceAll(" ","%20")+"&alumcity="+alumcity.getText().toString().replaceAll(" ","%20")+"&alumstate="+alumstate.getText().toString().replaceAll(" ","%20")+"&alumcon="+alumcon.getText().toString().replaceAll(" ","%20")+"&alumpos="+alumpostal.getText().toString().replaceAll(" ","%20"));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

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
                //  head=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    //  head[i]=json_data.getString("comment_title");


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
                Toast.makeText(getBaseContext(),"Updated  Succesfully",Toast.LENGTH_LONG).show();
                // TextView b=new TextView(ThreadRead.this);





            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alumni_update, menu);
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
