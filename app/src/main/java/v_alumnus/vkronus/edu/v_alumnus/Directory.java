package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Directory extends ActionBarActivity {
    String alumniyr, alumnibranch;
    String year[], branch[],alumniname[],alumniid[],div[];
    String alumnidiv;
    LinearLayout l;
    //ListView s4;
    Spinner s,s1,s2 ;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));

        s=(Spinner) findViewById(R.id.alumni_year1);
        s1=(Spinner) findViewById(R.id.alumni_branch1);
        s2=(Spinner) findViewById(R.id.alumni_div1);
        s1.setEnabled(false);
        l=(LinearLayout)findViewById(R.id.listdetails);
       // s4=(ListView)findViewById(R.id.studname);
        new signup21().execute("", "", "");

    }


    //=============================

    class signup21 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Directory.this);
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
            List r = new ArrayList();


            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectyeardir.php");


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
                year = new String[jArray.length() + 1];
                year[0] = "Select Passout Year";
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    year[i + 1] = json_data.getString("pass_year");//here "Name" is the column name in database
                }
            } catch (JSONException e1) {
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

            try {
                ArrayAdapter adapter = new ArrayAdapter(Directory.this, android.R.layout.simple_spinner_item, year);
                s.setAdapter(adapter);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != 0) {
                            alumniyr = year[position];
                            s1.setEnabled(true);
                            new signup22().execute("", "", "");
                        } else {
                            s1.setSelection(0);
                            s2.setSelection(0);
                            s1.setEnabled(false);
                            s2.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
        class signup22 extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Directory.this);
                pDialog.setMessage("Checking For the Data..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            protected String doInBackground(String... urls) {

                JSONArray jArray = null;

                String result = null;

                StringBuilder sb = null;

                InputStream is = null;
                List r = new ArrayList();


                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectbranchdir.php?yr=" + alumniyr);
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
                    branch = new String[jArray.length() + 1];
                    branch[0] = "Select Branch";
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        branch[i + 1] = json_data.getString("alum_branch");//here "Name" is the column name in database
                    }


                } catch (JSONException e1) {
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

                try {

                    ArrayAdapter adapter = new ArrayAdapter(Directory.this, android.R.layout.simple_spinner_item, branch);

                    s1.setAdapter(adapter);

                    s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i != 0) {
                                alumnibranch = branch[i];
                                s2.setEnabled(true);
                                new signup23().execute("","","");
                            }
                            else
                            {
                                s2.setSelection(0);
                                s2.setEnabled(true);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

                //   TextView tr = (TextView) findViewById(R.id.check);
                // tr.setText(name);

                // TODO: check this.exception

                // TODO: do something with the feed
            }
        }
//=======================================

    class signup23 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(Directory.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                JSONArray jArray = null;

                String result = null;

                StringBuilder sb = null;

                InputStream is = null;
                List r = new ArrayList();


                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectdivdir.php?yr=" + alumniyr + "&branch=" + alumnibranch);
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
                    div=new String[jArray.length()+1];
                    div[0]="Select Division";
                    //alumniname = new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);

                        div[i+1] = json_data.getString("alum_div");//here "Name" is the column name in database

                    }
                    return "ok";

                } catch (JSONException e1) {
                    //   Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                }
            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return "no ok";
            }

        }

        protected void onPostExecute(String feed) {

            pDialog.dismiss();
            try {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Directory.this, android.R.layout.simple_list_item_1, div);
                s2.setAdapter(adapter);
                s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i != 0) {
                            alumnidiv = div[i];
                            new signup24().execute("","","");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
               // s4.setAdapter(adapter);
                //Toast.makeText(getBaseContext(),alumniname[1],Toast.LENGTH_LONG).show();
               /* if(alumniname[1].equals(null))
                {
                    s4.setEnabled(false);
                   }
                else {
    s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i != 0) {
                alumninamem = alumniname[i];
                //s3.setEnabled(true);
            } else {
                //s3.setEnabled(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }
    });
}*/

            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);

            // TODO: check this.exception

            // TODO: do something with the feed
        }

    }




    class signup24 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(Directory.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                JSONArray jArray = null;

                String result = null;
                alumniname=null;
                StringBuilder sb = null;

                InputStream is = null;
                List r = new ArrayList();


                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectnamedir.php?yr=" + alumniyr + "&branch=" + alumnibranch+"&div="+alumnidiv);
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
                    alumniname=new String[jArray.length()];
                    alumniid=new String[jArray.length()];

                    //div[0]="Select Division";
                    //alumniname = new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        alumniname[i] = json_data.getString("alum_name");//here "Name" is the column name in database
                        alumniid[i] = json_data.getString("alumni_usrid");//here "Name" is the column name in database
                    }
                    return "ok";

                } catch (JSONException e1) {
                    //   Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                }
            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return "no ok";
            }

        }

        protected void onPostExecute(String feed) {
            try {
                pDialog.dismiss();
                if(alumniname!=null) {
                    l.removeAllViews();
                    for (int i = 0; i < alumniname.length; i++) {
                        Button b = new Button(Directory.this);
                        TextView t1 = new TextView(Directory.this);
                        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                        b.setText(alumniname[i]);
                        b.setBackgroundColor(Color.parseColor("#5889DB"));
                        b.setId(i);
                        b.setTextColor(Color.parseColor("#ffffff"));
                        b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent r = new Intent(Directory.this, Alumni.class);
                                r.putExtra("AID", alumniid[v.getId()]);
                                r.putExtra("utype","alumni");
                                startActivity(r);
                            }
                        });
                        l.addView(t1);
                        l.addView(b);

                    }
                }
                else
                    Toast.makeText(getBaseContext(),"Error In Connection",Toast.LENGTH_LONG).show();
                // s4.setAdapter(adapter);
                //Toast.makeText(getBaseContext(),alumniname[1],Toast.LENGTH_LONG).show();
               /* if(alumniname[1].equals(null))
                {
                    s4.setEnabled(false);
                   }
                else {
    s4.setOnItemSelectedListener(new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i != 0) {
                alumninamem = alumniname[i];
                //s3.setEnabled(true);
            } else {
                //s3.setEnabled(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }
    });
}*/

            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);

            // TODO: check this.exception

            // TODO: do something with the feed
        }

    }
    }
