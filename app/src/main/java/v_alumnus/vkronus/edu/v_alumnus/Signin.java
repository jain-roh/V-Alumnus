package v_alumnus.vkronus.edu.v_alumnus;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;
import v_alumnus.vkronus.edu.v_alumnus.util.MyTextChecker;

public class Signin extends Activity {
    Spinner s, s1, s2, s3, s4;
    private String year[], branch[], div[], alumniname[],tp[];
    String token="";

    RadioGroup fbid, twid;
    String alumniusr;
    String alumnipwd;
    EditText usr, alumniProf, alumniType, alumniDesg;
    EditText ph1, ph2, eid1, eid2, add1, add2, city, state, country, postal;
    DatePicker dob;
    String err = "";
    GlobalClass globalVariable;
    int allow = 1;
    String facultyusr,facultypwd;
    String alumni_ph1, alumni_ph2, alumni_add1, alumni_add2, alumni_eid1, alumni_eid2, alumni_city, alumni_state, alumni_country, alumni_postal;
    String alumniyr = "";
    String alumnibranch = "", alumnidiv = "", alumnigen = "", alumninamem = "", alumniproff = "", alumnitype = "", alumnidesg = "";
    String gen[] = {"Select Gender", "Male", "Female"};
    RadioGroup rg;
    RadioButton af, fb, tw;
    EditText pwd, pwd2;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
setTitle("Registration Form");

        try {
            rg = (RadioGroup) findViewById(R.id.selectcat);
            globalVariable=(GlobalClass)getApplicationContext();
            final Button signupbt1 = (Button) findViewById(R.id.nextbtn);
            s = (Spinner) findViewById(R.id.alumni_year);

            af = (RadioButton) findViewById(R.id.radio_alumni);
            af.setSelected(true);
            fbid = (RadioGroup) findViewById(R.id.fb);
            fb = (RadioButton) findViewById(R.id.alumni_fb_yes);
            fb.setChecked(true);
            twid = (RadioGroup) findViewById(R.id.tw);
            tw = (RadioButton) findViewById(R.id.alumni_tw_yes);
            tw.setChecked(true);


            s1 = (Spinner) findViewById(R.id.alumni_branch);
            s2 = (Spinner) findViewById(R.id.alumni_div);
            s3 = (Spinner) findViewById(R.id.alumni_gen);
            s4 = (Spinner) findViewById(R.id.alumni_name);

            alumniProf = (EditText) findViewById(R.id.alumniProf);
            alumniType = (EditText) findViewById(R.id.alumniType);
            alumniDesg = (EditText) findViewById(R.id.alumniDesignation);


            ph1 = (EditText) findViewById(R.id.alumni_ph1);

            ph2 = (EditText) findViewById(R.id.alumni_ph2);
            add1 = (EditText) findViewById(R.id.alumni_add1);
            add2 = (EditText) findViewById(R.id.alumni_add2);
            eid1 = (EditText) findViewById(R.id.alumni_eml1);
            eid2 = (EditText) findViewById(R.id.alumni_eml2);
            city = (EditText) findViewById(R.id.alumni_city);

            state = (EditText) findViewById(R.id.alumni_state);

            country = (EditText) findViewById(R.id.alumni_country);
            postal = (EditText) findViewById(R.id.alumni_postal);
            dob = (DatePicker) findViewById(R.id.DOB);
            //dob.setMaxDate(Calendar.getInstance().get(Calendar.YEAR));
            ArrayAdapter spingen = new ArrayAdapter(Signin.this, android.R.layout.simple_spinner_item, gen);
            s3.setAdapter(spingen);
            final Button alumnisignupbt2 = (Button) findViewById(R.id.alumninextbtn1);
            alumnisignupbt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allow = 1;
                    if (s.getSelectedItemPosition() == 0) {
                        Toast.makeText(getBaseContext(), "Please Select Passout Year", Toast.LENGTH_LONG).show();
                        allow = 0;
                    } else if (s1.getSelectedItemPosition() == 0) {
                        Toast.makeText(getBaseContext(), "Please Select Branch", Toast.LENGTH_LONG).show();
                        allow = 0;
                    } else if (s2.getSelectedItemPosition() == 0) {

                        Toast.makeText(getBaseContext(), "Please Select Division", Toast.LENGTH_LONG).show();
                        allow = 0;
                    } else if (s3.getSelectedItemPosition() == 0) {

                        Toast.makeText(getBaseContext(), "Please Select Gender", Toast.LENGTH_LONG).show();
                        allow = 0;
                    } else if (s4.getSelectedItemPosition() == 0) {

                        Toast.makeText(getBaseContext(), "Please Select Name", Toast.LENGTH_LONG).show();
                        allow = 0;
                    } else if (alumniProf.getText().toString().equals(' ') || alumniProf.getText().toString().length() == 0) {
                        alumniProf.setError("Please Enter Profession");
                        allow = 0;
                    } else {
                        if (allow == 1) {
                            alumniyr = year[s.getSelectedItemPosition()];
                            alumnibranch = branch[s1.getSelectedItemPosition()];
                            alumnidiv = div[s2.getSelectedItemPosition()];
                            alumnigen = gen[s3.getSelectedItemPosition()];
                            alumninamem = MyTextChecker.getValue(alumniname[s4.getSelectedItemPosition()]);
                            alumniproff = MyTextChecker.getValue(alumniProf.getText().toString());
                            alumnidesg = MyTextChecker.getValue(alumniDesg.getText().toString());
                            alumnitype = MyTextChecker.getValue(alumniType.getText().toString());

                            findViewById(R.id.login_form2).setVisibility(View.GONE);
                            findViewById(R.id.login_form3).setVisibility(View.VISIBLE);
                        }
                    }
                }
            });


            final Button alumniresetbt2 = (Button) findViewById(R.id.alumniresetbtn1);
            alumniresetbt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.setSelection(0);
                    s1.setSelection(0);
                    s2.setSelection(0);
                    s3.setSelection(0);
                    s4.setSelection(0);
                    alumniProf.setText("");
                    alumniDesg.setText("");
                    alumniType.setText("");
                }
            });


            final Button alumnisignupbt3 = (Button) findViewById(R.id.alumninextbtn2);
            alumnisignupbt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allow = 1;
                    if (ph1.getText().toString().length() == 0 || ph1.getText().toString().equals(' ')) {
                        ph1.setError("Please Enter Your Phone Number");
                        allow = 0;
                    } else if (add1.getText().toString().length() == 0 || add1.getText().toString().equals(' ')) {

                        add1.setError("Please Enter Your Address");
                        allow = 0;

                    } else if (eid1.getText().toString().length() == 0 || eid1.getText().toString().equals(' ')) {

                        eid1.setError("Please Enter Your Email Id");
                        allow = 0;

                    } else if (city.getText().toString().length() == 0 || city.getText().toString().equals(' ')) {

                        city.setError("Please Enter City");
                        allow = 0;

                    } else if (state.getText().toString().length() == 0 || state.getText().toString().equals(' ')) {

                        state.setError("Please Enter Your State");
                        allow = 0;

                    } else if (country.getText().toString().length() == 0 || country.getText().toString().equals(' ')) {

                        country.setError("Please Enter Your Country");
                        allow = 0;

                    } else if (postal.getText().toString().length() == 0 || postal.getText().toString().equals(' ')) {

                        ph1.setError("Please Enter Your Phone Number");
                        allow = 0;

                    } else {
                        if (allow == 1) {

                            alumni_ph1 = ph1.getText().toString();
                            alumni_ph2 = ph2.getText().toString();
                            alumni_add1 = MyTextChecker.getValue(add1.getText().toString());
                            alumni_add2 = MyTextChecker.getValue(add2.getText().toString());
                            alumni_eid1 = eid1.getText().toString();
                            alumni_eid2 = eid2.getText().toString();
                            alumni_city = MyTextChecker.getValue(city.getText().toString());
                            alumni_state = MyTextChecker.getValue(state.getText().toString());
                            alumni_country = MyTextChecker.getValue(country.getText().toString());
                            alumni_postal = MyTextChecker.getValue(postal.getText().toString());

                            new signup().execute("", "", "");

                        }
                    }
                }
            });

            usr = (EditText) findViewById(R.id.eid);
            pwd = (EditText) findViewById(R.id.password);
            pwd2 = (EditText) findViewById(R.id.cnfpassword);

            final Button reset1 = (Button) findViewById(R.id.resetbtn);
            reset1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usr.setText("");
                    pwd.setText("");
                    pwd2.setText("");
                }
            });


            s1.setEnabled(false);
            s2.setEnabled(false);
            s3.setEnabled(false);
            s4.setEnabled(false);

            signupbt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        err = "";
                        allow = 2;

                        if(globalVariable.isOnline())
                        {
                            allow=1;
                        }
                        if (usr.getText().toString().trim().equals(" ")||usr.getText().toString().trim().equals("") || usr.getText().length() == 0) {
                            usr.setError("\n Please Enter Username ");
                            allow = 0;
                        }
                        if (pwd.getText().toString().trim().equals("") || pwd.getText().length() == 0) {
                            pwd.setError("Please Enter Password");
                            allow = 0;
                        }
                        if (pwd2.getText().toString().equals(pwd.getText().toString())) {

                        } else {
                            pwd2.setError("Passwords Do not Match");
                            allow = 0;
                        }
                        if (rg.getCheckedRadioButtonId() == -1) {
                            err = "Please Select One of both the Fields";
                        }

                        if (allow == 1) {
                            switch (rg.getCheckedRadioButtonId()) {
                                case R.id.radio_alumni: {
                                    alumniusr = usr.getText().toString();
                                    alumnipwd = pwd.getText().toString();
                                    // Toast.makeText(getBaseContext(),"Executed",Toast.LENGTH_LONG).show();
                                    //  setContentView(R.layout.signup1);
                                    new signup1().execute("");

                                }
                                break;
                                case R.id.radio_faculty: {
                                    facultyusr=usr.getText().toString();
                                    facultypwd=pwd.getText().toString();
                                    new signupfac1().execute("");
                                }
                                break;
                                default: {
                                    err += "\n Please Select Valid Option(*Select Alumni Or Faculty)";
                                }
                                break;
                            }


                        }

                        else if(allow==2)
                            Toast.makeText(getBaseContext(),"Error In Connection",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    //  int selectedId;

                    // selectedId = rg.getCheckedRadioButtonId();
                    //     Toast.makeText(getBaseContext(),""+selectedId,Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    class signup1 extends AsyncTask<String, Void, String> {
        int chk;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
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
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/alumnicheck.php?usr=" + alumniusr);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();
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
                JSONObject json_data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    chk = json_data.getInt("alum_id");
                }
                return chk + "";

            } catch (JSONException e1) {
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                return e1.toString();
            }

        }

        protected void onPostExecute(String feed) {
            pDialog.dismiss();
            if (chk != 0) {
                usr.setError("Username Already Taken");

            } else {
                findViewById(R.id.email_login_form1).setVisibility(View.GONE);
                findViewById(R.id.login_form2).setVisibility(View.VISIBLE);

                try {

                    new signup21().execute("","","");

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    class signup21 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
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


            try{
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectyr.php");


                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection"+e.toString());
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
                year=new String[jArray.length()+1];
                year[0]="Select Passout Year";
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    year[i+1]=json_data.getString("alumni_yr");//here "Name" is the column name in database
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
            try {
                pDialog.dismiss();
               ArrayAdapter adapter1= new ArrayAdapter(Signin.this, android.R.layout.simple_spinner_item, year);
                s.setAdapter(adapter1);

                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != 0) {
                            alumniyr = year[position];
                            s1.setEnabled(true);
                            new signup22().execute("", "", "");
                        } else {
                            s1.setSelection(0);
                            s1.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });

            }
            catch (Exception e){
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();}

        }
    }



    class signup22 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
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


            try{
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectbranch.php?yr="+alumniyr);
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection"+e.toString());
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
                branch=new String[jArray.length()+1];
                branch[0]="Select Branch";
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    branch[i+1]=json_data.getString("alumni_branch");//here "Name" is the column name in database
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
            try {
pDialog.dismiss();
                ArrayAdapter adapter = new ArrayAdapter(Signin.this, android.R.layout.simple_spinner_item, branch);
                s1.setAdapter(adapter);
                s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0)
                        {
                            alumnibranch=branch[i];
                            s2.setEnabled(true);
                            new signup23().execute("","","");
                        }
                        else
                        {
                            s2.setSelection(0);
                            s2.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }


        }
    }



    class signup23 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
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


            try{
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectdiv.php?yr="+alumniyr+"&branch="+alumnibranch);
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection"+e.toString());
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
                div=new String[jArray.length()+1];
                div[0]="Select Division";
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    div[i+1]=json_data.getString("alumni_div");//here "Name" is the column name in database
                }
            }
            catch(JSONException e1){

                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();

                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            try {
pDialog.dismiss();
                ArrayAdapter adapter = new ArrayAdapter(Signin.this, android.R.layout.simple_spinner_item, div);

                s2.setAdapter(adapter);
                s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0)
                        {
                            alumnidiv=div[i];
                            s3.setEnabled(true);
                        }
                        else
                        {
                            s3.setSelection(0);
                            s3.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position!=0)
                        {
                            alumnigen=gen[position];
                            s4.setEnabled(true);
                            new signup24().execute("","","");
                        }
                        else
                        {
                            s4.setSelection(0);
                            s4.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }


        }
    }



    class signup24 extends AsyncTask<String, Void, String> {

        private Exception exception;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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

                    HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/selectname.php?yr=" + alumniyr + "&branch=" + alumnibranch + "&div=" + alumnidiv + "&gen=" + alumnigen);
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
                    alumniname = new String[jArray.length() + 1];
                    alumniname[0] = "Select Name";
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        alumniname[i + 1] = json_data.getString("alumni_name");//here "Name" is the column name in database

                    }
                    return "ok";

                } catch (JSONException e1) {
                   Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
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
                if(alumniname==null)
                {
                    Toast.makeText(getBaseContext(),"Error In Connection",Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayAdapter adapter = new ArrayAdapter(Signin.this, android.R.layout.simple_spinner_item, alumniname);

                    s4.setAdapter(adapter);
                }
pDialog.dismiss();


            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }


        }

    }




    class signup extends AsyncTask<String, Void, String> {
        String chk="nk",res="Account Not Created";
        private Exception exception;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {
            chk="connectionissue";
            String onfb="Yes",ontw="Yes";
            if(fb.isChecked())
            {
                onfb="Yes";
            }
            else
            {
                onfb="No";
            }

            if(tw.isChecked())
            {
                ontw="Yes";
            }
            else
            {
                ontw="No";
            }

            try {
                JSONArray jArray = null;
                String result = null;
                StringBuilder sb = null;
                InputStream is = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/alumnireg.php");
                List r = new ArrayList();
                try {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("usr", alumniusr));
                    params.add(new BasicNameValuePair("pwd", alumnipwd));
                    params.add(new BasicNameValuePair("py", alumniyr));
                    params.add(new BasicNameValuePair("ab", alumnibranch));
                    params.add(new BasicNameValuePair("ad", alumnidiv));
                    params.add(new BasicNameValuePair("an", alumninamem));
                    params.add(new BasicNameValuePair("ag", alumnigen));
                    params.add(new BasicNameValuePair("ap", alumniproff));
                    params.add(new BasicNameValuePair("add", alumnidesg));
                    params.add(new BasicNameValuePair("adt", alumnitype));
                    params.add(new BasicNameValuePair("adob", dob.getYear() + "-" + dob.getMonth() + "-" + dob.getDayOfMonth()));
                    params.add(new BasicNameValuePair("aph1", alumni_ph1.replaceAll(" ","")));
                    params.add(new BasicNameValuePair("aph2", alumni_ph2.replaceAll(" ","")));
                    params.add(new BasicNameValuePair("aeid1", alumni_eid1));
                    params.add(new BasicNameValuePair("aeid2", alumni_eid2));
                    params.add(new BasicNameValuePair("aof", onfb));
                    params.add(new BasicNameValuePair("aol", ontw));
                    params.add(new BasicNameValuePair("aad1", alumni_add1));
                    params.add(new BasicNameValuePair("aad2", alumni_add2));
                    params.add(new BasicNameValuePair("ac", alumni_city));
                    params.add(new BasicNameValuePair("as", alumni_state));
                    params.add(new BasicNameValuePair("acon", alumni_country));
                    params.add(new BasicNameValuePair("apos", alumni_postal));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
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
                    JSONObject json_data = null;
                    for(int i=0;i<jArray.length();i++) {
                        json_data = jArray.getJSONObject(i);
                        chk = json_data.getString("yon");
                        res = json_data.getString("check");
                    }
                    return "ok";

                } catch (JSONException e1) {
                    Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                   Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
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
                if(chk.equals("ok")){
                Toast.makeText(getBaseContext(),res,Toast.LENGTH_LONG).show();
                Intent r=new Intent(Signin.this,Login.class);
                    startActivity(r);
                    finish();
                }
                else if(chk.equals("connectionissue"))
                    Toast.makeText(getBaseContext(),"Error in Connection",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(),res,Toast.LENGTH_LONG).show();

pDialog.dismiss();

            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }


        }



    }

    class signupfac1 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signin.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        int chk=-1;

        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;

            InputStream is = null;
            List r = new ArrayList();


            try {
               // Toast.makeText(getBaseContext(),"usr="+alumniusr+"&py="+alumniyr+"&ab="+alumnibranch+"&ad="+alumnidiv+"&an="+alumninamem+"&ag="+alumnigen+"&ap="+alumniproff+"&add="+alumnidesg+"&adt="+alumnitype,Toast.LENGTH_LONG).show();
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://sagarsmailbox.esy.es/facregchk.php?usr="+facultyusr);
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
               // usr=new String[jArray.length()];
                JSONObject json_data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    chk=json_data.getInt("alum_id");
                }
                return chk + "";

            } catch (JSONException e1) {
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                return e1.toString();
            }

        }

        protected void onPostExecute(String feed) {
                try {
                    pDialog.dismiss();
                    if(chk!=0)
                    {
                        usr.setError("Username Already Taken");

                    }
                    else if(chk==-1)
                    {
                        Toast.makeText(getBaseContext(), "Error Connecting to Server", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(getBaseContext(), "Redirected TO Next Page", Toast.LENGTH_SHORT).show();
                    Intent r=new Intent(Signin.this,facultyreg.class);
                        r.putExtra("usr",facultyusr);
                        r.putExtra("pwd",facultypwd);
                        startActivity(r);


                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }


        }

}


