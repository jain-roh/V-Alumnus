package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class event extends ActionBarActivity {
private ProgressDialog pDialog;
    //LinearLayout fl,ln;

//FrameLayout ln;

    //ListView listnews;
    //TextView eventhead,eventloc,eventdate,eventdesc;
    String title[],nid[],eloc[],edate[],etime[],event[],edesc[],eventimg[];
      Bitmap evntbit[];
    int eventid[];
       LinearLayout l;
    int k1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setTitle("Upcoming Events");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));

        l=(LinearLayout)findViewById(R.id.heading);
       // backbtn=(Button)findViewById(R.id.backbtn);

        //fl=(LinearLayout)findViewById(R.id.heading);
        // fl=(LinearLayout)findViewById(R.id.heading);
        //ln=(LinearLayout)findViewById(R.id.news);
        //eventhead=(TextView)findViewById(R.id.eventhead);
       // eventloc=(TextView)findViewById(R.id.eventloc);
        //eventdate=(TextView)findViewById(R.id.eventdate);

      //  eventdesc=(TextView)findViewById(R.id.eventdesc);
        //listnews=(ListView)findViewById(R.id.listView1);
       /* backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventhead.setVisibility(View.GONE);
                eventloc.setVisibility(View.GONE);
                eventdate.setVisibility(View.GONE);

                eventdesc.setVisibility(View.GONE);
                listnews.setVisibility(View.VISIBLE);
                backbtn.setVisibility(View.GONE);
            }
        });*/
        new network().execute("","","");
    }



    class network extends AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(event.this);
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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/event.php");

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
                title=new String[jArray.length()];
                eloc=new String[jArray.length()];
                edate=new String[jArray.length()];
                nid=new String[jArray.length()];
                edesc=new String[jArray.length()];
                event=new String[jArray.length()];
                etime=new String[jArray.length()];
                eventimg=new String[jArray.length()];
                evntbit=new Bitmap[jArray.length()];
                eventid=new int[jArray.length()];
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    eventid[i]=json_data.getInt("id");
                    title[i]=json_data.getString("event_name");//here "Name" is the column name in database
                    eloc[i]=json_data.getString("event_loc");
                    edate[i]=json_data.getString("event_date");
                    eventimg[i]=json_data.getString("event_img");
                    edesc[i]=json_data.getString("event_desc");
                    etime[i]=json_data.getString("event_time");
                    /*news[i]=new Button(getBaseContext());
                    news[i].setText(title[i]);
                    news[i].setId(i);
                    news[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                       */
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
               // ArrayAdapter<String> adapter = new ArrayAdapter<String>(event.this, android.R.layout.simple_list_item_1, event);
               // listnews.setAdapter(adapter);
                //listnews.setDividerHeight(3);
                //listnews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  /*  @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listnews.setVisibility(View.GONE);
                        eventhead.setText(title[position]);
                        eventloc.setText(eloc[position]);
                        eventdate.setText(edate[position]);
                        eventdesc.setText(edesc[position]);
                        eventhead.setVisibility(View.VISIBLE);
                        eventloc.setVisibility(View.VISIBLE);
                        eventdate.setVisibility(View.VISIBLE);
                        eventdesc.setVisibility(View.VISIBLE);

                       // backbtn.setVisibility(View.VISIBLE);
                        //Toast.makeText(getBaseContext(),title[position]+"",Toast.LENGTH_LONG).show();

                    }
                });
                // listnews.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
*/
                if(title!=null) {
                    for (int i = 0; i < title.length; i++) {
                        LinearLayout ln=new LinearLayout(event.this);
                        ln.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ln.setWeightSum(1.0f);
                        ln.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout con=new LinearLayout(event.this);
                        LinearLayout.LayoutParams p2=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        p2.weight=0.4f;
                        con.setGravity(LinearLayout.VERTICAL);
                        Button head = new Button(event.this);
                        TextView t1 = new TextView(event.this);
                        TextView t2 = new TextView(event.this);
                        ImageView img=new ImageView(event.this);
                        LinearLayout.LayoutParams p3=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        p3.weight=0.6f;
                        img.setId(i);
                        img.setBackgroundColor(Color.WHITE);
                        img.setTag(eventid[i]);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent r=new Intent(event.this,EventInfo.class);
                                r.putExtra("eventid",""+view.getTag());

                                startActivity(r);
                            }
                        });
                        t1.setBackgroundColor(Color.parseColor("#BAB8B8"));
                        t1.setTextColor(Color.parseColor("#ffffff"));
                        t1.setText("\nDate & Time :" + edate[i] + "\t" + etime[i] + "\n\nLocation : " + eloc[i] + "\n\nDetails : " + edesc[i]);
                        head.setText("Event Name : " + title[i]);
                        head.setBackgroundColor(Color.parseColor("#ffffff"));
                        head.setTextColor(Color.parseColor("#000000"));
                        head.setTag(eventid[i]);
                        head.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent r=new Intent(event.this,EventInfo.class);
                                r.putExtra("eventid",""+view.getTag());

                                startActivity(r);
                            }
                        });


                       // head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        t2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                       // l.addView(head);
                        ln.addView(img,p2);
                        ln.addView(head,p3);
                        l.addView(ln);
                        l.addView(t2);
                    }
                    new network1().execute();
                }
                else
                    Toast.makeText(getBaseContext(),"Error In Connection",Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);
            try {
            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

            //
            // TODO: check this.exception

            // TODO: do something with the feed
        }


        class network1 extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... urls) {

                try {
                    if(k1<title.length){
                        URL url = new URL("http://sagarsmailbox.esy.es/eventthumbnail/"+eventimg[k1]);
                        evntbit[k1]=BitmapFactory.decodeStream(url.openConnection().getInputStream());
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

                if(k1<title.length){
                    ImageView im=(ImageView)findViewById(k1);
                    im.setImageBitmap(evntbit[k1]);
                    k1++;
                    new network1().execute();
                }

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
