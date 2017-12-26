package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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


public class birthday extends ActionBarActivity {
    LinearLayout linear;
    String[] an,alumusrid;
private ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
           setTitle("Birthdays");
        StrictMode.enableDefaults();

        linear = (LinearLayout) findViewById(R.id.linear);
        new network1().execute("","","");
    }



    class network1 extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(birthday.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        public String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;



            InputStream is = null;
            List r=new ArrayList();


            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/bdayevent.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
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
                an=new String[jArray.length()];
                alumusrid=new String[jArray.length()];

                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    an[i]=json_data.getString("alum_name");
                    alumusrid[i]=json_data.getString("alumni_usrid");
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

        public void onPostExecute(String feed) {
            pDialog.dismiss();
            try {
                if (an==null) {
                    Button b = new Button(birthday.this);
                    b.setText("Problem In Internet Connection");
                    b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setBackgroundColor(Color.parseColor("#4d4dff"));
                    b.setTextColor(Color.parseColor("#ffffff"));
                    Toast.makeText(getBaseContext(), "Problem In Connection", Toast.LENGTH_SHORT).show();

                    linear.addView(b);
                } else if (an[0].equals("null")) {
                    Button b = new Button(birthday.this);
                    b.setText("No Birthday's Today");
                    b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setBackgroundColor(Color.parseColor("#4d4dff"));
                    b.setTextColor(Color.parseColor("#ffffff"));
                    linear.addView(b);
                    ;

                    Toast.makeText(getBaseContext(), "No Birthdays", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < an.length; i++) {
                        Button b = new Button(birthday.this);
                        TextView t1 = new TextView(birthday.this);
                        b.setId(i);
                        b.setText(an[i]);
                        b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        b.setBackgroundColor(Color.parseColor("#4d4dff"));
                        b.setTextColor(Color.parseColor("#ffffff"));
                        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent r1 = new Intent(birthday.this, Alumni.class);
                                r1.putExtra("AID", alumusrid[v.getId()]);
                                r1.putExtra("utype","alumni");
                                startActivity(r1);
                            }
                        });
                        linear.addView(b);
                        linear.addView(t1);
                    }
                }
            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }







    }


