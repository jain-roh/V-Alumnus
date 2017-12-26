package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Faculty extends ActionBarActivity {
    ImageView pp;
    TextView nm,br;
    String name,branch,propic;
    Bitmap img;
    String fid;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);pp=(ImageView)findViewById(R.id.propic);
        nm=(TextView)findViewById(R.id.facname);
        br=(TextView)findViewById(R.id.facbranch);
        Bundle bundle = getIntent().getExtras();
        fid = bundle.getString("FID");
        propic="";
        new network().execute();

    }
    class network extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Faculty.this);
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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/getfacdet.php?usrid="+fid);
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
                    branch=json_data.getString("fac_branch");
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
                nm.setText(nm.getText()+"\t:"+name);


                br.setText(br.getText()+"\t:"+branch);


                pp.setImageBitmap(img);

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faculty, menu);
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
