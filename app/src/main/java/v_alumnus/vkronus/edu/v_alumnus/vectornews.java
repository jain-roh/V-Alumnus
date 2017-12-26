package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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


public class vectornews extends ActionBarActivity {

    LinearLayout l;
    int offset=0;
    int limit=10;
    String title[];
    String nid[];
    ArrayList<String> imgloc;
    ArrayList<Bitmap> img;
    Button more;
    int k=0,k1=0;
ScrollView sc2;

private ProgressDialog pDialog;
//FrameLayout ln;
    //ListView listnews;
    //TextView newhead,newdesc;
    //String newsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);
        setTitle("Vector");
        imgloc=new ArrayList<String>();
        img=new ArrayList<Bitmap>();
        sc2=(ScrollView)findViewById(R.id.scrollView2);
      /*  sc2.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = sc2.getScrollY(); //for verticalScrollView
                if(scrollY>=sc2.getChildAt(0).getHeight()-150) {
                    new network().execute("");
                  //  Toast.makeText(getApplicationContext(), "" + scrollY, Toast.LENGTH_LONG).show();
                    //DO SOMETHING WITH THE SCROLL COORDINATES
                }
            }
        });*/
        more=(Button)findViewById(R.id.morenews);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new network().execute("");
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        l = (LinearLayout) findViewById(R.id.heading);
        //fl=(LinearLayout)findViewById(R.id.heading);
        // fl=(LinearLayout)findViewById(R.id.heading);
        //ln=(LinearLayout)findViewById(R.id.news);
        // newhead=(TextView)findViewById(R.id.newshead);
        //newdesc=(TextView)findViewById(R.id.newsdet);
        //listnews=(ListView)findViewById(R.id.listView1);
        new network().execute("", "", "");
    }

    class network extends AsyncTask<String, Void, String> {
        InputStream is = null;
        int id=-1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(vectornews.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
           try {
               if (pDialog.isShowing() == false)
                   is.close();
           }
           catch (Exception e)
           {
                          }
            pDialog.show();
        }
        protected String doInBackground(String... urls) {
            JSONArray jArray = null;
            String result = null;
            StringBuilder sb = null;

            List r = new ArrayList();
            title=null;
            ArrayList nameValuePairs = new ArrayList();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/vector.php?offset="+offset+"&limit="+limit);

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
                title = new String[jArray.length()];
                nid = new String[jArray.length()];

                //news=new Button[jArray.length()];
                // newsdet=new String[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    title[i] = json_data.getString("vector_title");//here "Name" is the column name in database
                    nid[i] = json_data.getString("vector_uniq_id");
                    imgloc.add(json_data.getString("vector_img"));
                    id=1;
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
            int im=offset;
            try {
                if(id==-1)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new network().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {
                                    finish();
                                }
                                break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(vectornews.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
                else if(title!=null)
                {
                    if(title[0].equals("null"))
                                 more.setVisibility(View.GONE);
                        else {
                        for (int i = 0; i < title.length; i++) {

                            if (title[i].toString().length() > 70) {
                                title[i] = title[i].substring(0, 68) + "..";
                            }
                            title[i]=" "+title[i];

                            offset++;
                            LinearLayout btn=new LinearLayout(vectornews.this);
                            btn.setTag(nid[i]);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent r = new Intent(vectornews.this, NewsDetails.class);
                                    r.putExtra("NID", v.getTag() + "");

                                    startActivity(r);
                                }
                            });
                            btn.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams p1=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            btn.setWeightSum(1.0f);
                            btn.setLayoutParams(p1);
                            ImageView imageView=new ImageView(vectornews.this);
                            //imageView.setImageBitmap(img[i]);
                            imageView.setId(k);
                            imageView.setTag(nid[i]);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent r = new Intent(vectornews.this, NewsDetails.class);
                                    r.putExtra("NID", v.getTag() + "");

                                    startActivity(r);
                                }
                            });
                            k++;
                            imageView.setPadding(2,2,2,1);
                            LinearLayout.LayoutParams p2=new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams p3=new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
                            p2.weight=0.4f;
                            p3.weight=0.6f;
                            TextView b = new TextView(vectornews.this);
                            b.setPadding(1,2,2,2);
                            TextView t1 = new TextView(vectornews.this);
                            LinearLayout lm = new LinearLayout(vectornews.this);
                            lm.setOrientation(LinearLayout.VERTICAL);
                            lm.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            lm.setMinimumHeight(120);
                            lm.setPadding(0, 0, 0, 0);
                            lm.setBackgroundColor(Color.parseColor("#ffffff"));
                            b.setText(title[i]);
                            b.setGravity(Gravity.TOP);
                            t1.setTextSize(7);
                            t1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
                            t1.setBackgroundColor(Color.parseColor("#ffd2d2d2"));
                            b.setPadding(10, 5, 5, 10);
                            b.setTextSize(18);
                            b.setBackgroundColor(Color.parseColor("#ffffff"));
                            b.setTextColor(Color.parseColor("#000000"));
                            b.setTag(nid[i]);
                            b.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.80f));
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent r = new Intent(vectornews.this, NewsDetails.class);
                                    r.putExtra("NID", v.getTag() + "");

                                    startActivity(r);
                                }
                            });
                            btn.addView(imageView, p2);
                            btn.addView(b,p3);
                            lm.addView(btn);
                            l.addView(lm);
                            l.addView(t1);
                        }
                        limit = 5;
                        new network1().execute();

                    }

                }
                else
                    Toast.makeText(getBaseContext(), "Error In Connection" ,Toast.LENGTH_LONG).show();

                //
                // TODO: check this.exception

                // TODO: do something with the feed
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "Problem", Toast.LENGTH_LONG).show();

            }
        }

    }










/*
@Override
    public void onBackPressed(){
    if(back==1)
    {
        newhead.setVisibility(View.GONE);
        newdesc.setVisibility(View.GONE);
        listnews.setVisibility(View.VISIBLE);
        back=0;


    }else {
        this.finish();
    }   }


*/




    class network1 extends AsyncTask<String, Void, String> {
       int ki=0;
        protected String doInBackground(String... urls) {

            try {
                if(k1<imgloc.size()){
                    URL url = new URL("http://sagarsmailbox.esy.es/vectorthumbnail/"+imgloc.get(k1));
                    img.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                    ki=1;
                }
                }
            catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }
            return null;

        }

        protected void onPostExecute(String feed) {

           if(k1<imgloc.size()){
            ImageView im=(ImageView)findViewById(k1);
            im.setImageBitmap(img.get(k1));
            k1++;
               new network1().execute();
            }

        }

    }


    class network2 extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            try {
                if(k1<img.size()){
                    URL url = new URL("http://sagarsmailbox.esy.es/vectorthumbnail/"+imgloc.get(k1));
                    img.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                }
            }
            catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }
            return null;

        }

        protected void onPostExecute(String feed) {

            if(k1<img.size()){
                ImageView im=(ImageView)findViewById(k1);
                im.setImageBitmap(img.get(k1));
                k1++;
                new network2().execute();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
