package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;


public class NewsDetails extends ActionBarActivity {
    String id;
    String desc,title,img;
    TextView head=null,det;
    ImageView newsimg;
    String date;
    Bitmap bitmap;
    TextView datetext;
    String name,type;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setTitle("Vector");
        newsimg=(ImageView)findViewById(R.id.imgnews);
        head=(TextView)findViewById(R.id.newshead);
        det=(TextView)findViewById(R.id.newsdet);
        datetext=(TextView)findViewById(R.id.datet);
        Bundle bundle = getIntent().getExtras();
        id=bundle.getString("NID");
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        name=globalVariable.getUsrName();
        type=globalVariable.getType();
        //Toast.makeText(getBaseContext(),id,Toast.LENGTH_SHORT).show();
        new networknews().execute("", "", "");
        if(type.equals("alumni"))
        new network6().execute("");
    }
    class networknews extends AsyncTask<String, Void, String> {
        int id1=-1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewsDetails.this);
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
            List r = new ArrayList();


            ArrayList nameValuePairs = new ArrayList();
//http post
            try {
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/vectornews.php?id="+id);
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

                //nid=new int[jArray.length()];
                //news=new Button[jArray.length()];

                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    title=json_data.getString("vector_title");
                    img=json_data.getString("vector_img");
                    desc=json_data.getString("vector_desc");
                    date=json_data.getString("vector_date");
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/vectorfiles/" + img).getContent());
                    id1=1;
                   /*news[i]=new Button(getBaseContext());
                    news[i].setText(title[i]);
                    news[i].setId(i);
                    news[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                       */

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
            try {
                pDialog.dismiss();

                if(id1==-1)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new networknews().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {
                                    finish();
                                }
                                break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetails.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
                       else if(head!=null) {

                    head.setText(title);

                    datetext.setText(date.toString());
                    //Toast.makeText(getBaseContext(),title,Toast.LENGTH_SHORT).show();
                    det.setText(desc);
                    newsimg.setImageBitmap(bitmap);
                }
                else
                Toast.makeText(getBaseContext(),"No Connection", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }
/*
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
    }*/
class network6 extends AsyncTask<String, Void, String> {


    protected String doInBackground(String... urls) {

        JSONArray jArray = null;

        String result = null;

        StringBuilder sb = null;


        InputStream is = null;
        List r = new ArrayList();


        ArrayList nameValuePairs = new ArrayList();

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/alumniscore.php?id=" + name);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection" + e.toString());
            return e.toString();
        }
//convert response to string


        return null;
    }

    protected void onPostExecute(String feed) {

    }
}
}
