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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class facultyreg extends ActionBarActivity {
        String usr,pwd;
    String name[],ucd[];

    Button submit;
    EditText uc;
    Spinner s1,s2;
    String brnch,nm,uniq;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyreg);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));

        Bundle bundle = getIntent().getExtras();
        usr = bundle.getString("usr");
        pwd=bundle.getString("pwd");
        uc=(EditText)findViewById(R.id.unique);
        uc.setEnabled(false);
        submit=(Button)findViewById(R.id.facsubmit);
        submit.setEnabled(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uc.getText().toString().equals(""))
                {
                    Toast.makeText(getBaseContext(),"Enter Unique Code",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (uniq.equals(uc.getText().toString()))
                        new facmain().execute("");
                    else
                        Toast.makeText(getBaseContext(),"Wrong Unique Code",Toast.LENGTH_LONG).show();
                }
            }
        });
        s1=(Spinner)findViewById(R.id.fac_div);
        s2=(Spinner)findViewById(R.id.fac_name);
        s2.setEnabled(false);
        new signupfac1().execute("");
    }

    class signupfac1 extends AsyncTask<String, Void, String> {

        String branch[];
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(facultyreg.this);
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
            List r = new ArrayList();


            try {
                // Toast.makeText(getBaseContext(),"usr="+alumniusr+"&py="+alumniyr+"&ab="+alumnibranch+"&ad="+alumnidiv+"&an="+alumninamem+"&ag="+alumnigen+"&ap="+alumniproff+"&add="+alumnidesg+"&adt="+alumnitype,Toast.LENGTH_LONG).show();
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/facbranch.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();
            }
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
                 branch=new String[jArray.length()+1];
                JSONObject json_data = null;
                branch[0]="Select Section";
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    branch[i+1]=json_data.getString("fac_branch");
                }
                return "executed";

            } catch (JSONException e1) {
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                return e1.toString();
            }

        }

        protected void onPostExecute(String feed) {
            pDialog.dismiss();
            try {
                ArrayAdapter adapter = new ArrayAdapter(facultyreg.this, android.R.layout.simple_spinner_item, branch);

                s1.setAdapter(adapter);
                s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != 0) {
                            brnch = branch[position];
                            s2.setEnabled(true);
                            new signupfac2().execute("", "", "");
                        } else {
                            s2.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }






        class signupfac2 extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(facultyreg.this);
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
                List r = new ArrayList();


                try {
                    // Toast.makeText(getBaseContext(),"usr="+alumniusr+"&py="+alumniyr+"&ab="+alumnibranch+"&ad="+alumnidiv+"&an="+alumninamem+"&ag="+alumnigen+"&ap="+alumniproff+"&add="+alumnidesg+"&adt="+alumnitype,Toast.LENGTH_LONG).show();
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/facname.php?brnch=" + brnch.replaceAll(" ","%20"));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection" + e.toString());
                    return e.toString();
                }
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
                    name = new String[jArray.length() + 1];
                    JSONObject json_data = null;
                    name[0] = "Select Name";
                    ucd=new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        name[i + 1] = json_data.getString("fac_name");
                        ucd[i]=json_data.getString("fac_uc");
                    }
                    return "executed";

                } catch (JSONException e1) {
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return e1.toString();
                }

            }

            protected void onPostExecute(String feed) {
                pDialog.dismiss();
                try {
                    ArrayAdapter adapter = new ArrayAdapter(facultyreg.this, android.R.layout.simple_spinner_item, name);

                    s2.setAdapter(adapter);
                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position != 0) {
                                nm = name[position];
                                uc.setEnabled(true);
                                uniq=ucd[position-1];
                                submit.setEnabled(true);
                                //new facmain().execute("", "", "");
                            } else {
                                uc.setEnabled(false);
                                submit.setEnabled(false);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }


                    });
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facultyreg, menu);
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


    class facmain extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(facultyreg.this);
            pDialog.setMessage("Creating Account ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;

            InputStream is = null;
            List r = new ArrayList();


            try {
                // Toast.makeText(getBaseContext(),"usr="+alumniusr+"&py="+alumniyr+"&ab="+alumnibranch+"&ad="+alumnidiv+"&an="+alumninamem+"&ag="+alumnigen+"&ap="+alumniproff+"&add="+alumnidesg+"&adt="+alumnitype,Toast.LENGTH_LONG).show();
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/facreg.php?usr="+usr+"&pwd="+pwd+"&brnch=" + brnch.replaceAll(" ", "%20")+"&name="+nm.replaceAll(" ","%20"));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
                return e.toString();
            }
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
               // branch = new String[jArray.length() + 1];
                JSONObject json_data = null;
                //name[0] = "Select Name";
                //ucd=new String[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                   // name[i + 1] = json_data.getString("fac_name");
                    //ucd[i]=json_data.getString("fac_uc");
                }
                return "executed";

            } catch (JSONException e1) {
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                return e1.toString();
            }

        }

        protected void onPostExecute(String feed) {
            pDialog.dismiss();
            try {
               Toast.makeText(getBaseContext(),"Data Inserted",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }







}
