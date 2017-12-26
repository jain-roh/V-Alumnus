package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

public class FriendRequest extends ActionBarActivity {
    LinearLayout l;
    int offset=0;
    String title[];
    String usrid[];
    String imgpic[];
    String usrtype[];
    int k=0,k1=0;
    ProgressDialog pDialog;
    String name;
    ScrollView sc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        name=globalVariable.getUsrName();
        sc2=(ScrollView)findViewById(R.id.scrollView2fr);
        l = (LinearLayout) findViewById(R.id.headingfr);

        new network().execute();

    }

    class network extends AsyncTask<String, Void, String> {
        int id=-1;
        InputStream is = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendRequest.this);
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

            ArrayList nameValuePairs = new ArrayList();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/friendreqlist.php?usr="+name);

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
                usrid = new String[jArray.length()];
                imgpic = new String[jArray.length()];
                usrtype = new String[jArray.length()];

                //news=new Button[jArray.length()];
                // newsdet=new String[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    title[i] = json_data.getString("alum_name");//here "Name" is the column name in database
                    usrid[i] = json_data.getString("alumni_usrid");
                    imgpic[i] = json_data.getString("alumn_pic");
                    usrtype[i] = json_data.getString("alum_type");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendRequest.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
                else if (title != null ) {
                    if(title[0].equals("null")) {
                    }
                    else {
                        for (int i = 0; i < title.length; i++) {


                            LinearLayout btn = new LinearLayout(FriendRequest.this);
                            btn.setId(i);
                            btn.setTag(usrid[i]);
                            LinearLayout btn1 = new LinearLayout(FriendRequest.this);
                            if (usrtype[i].equals("alumni")) {
                                btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","alumni");
                                        startActivity(r);
                                    }
                                });
                            } else {
                                btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","faculty");

                                        startActivity(r);
                                    }
                                });
                            }btn.setOrientation(LinearLayout.HORIZONTAL);
                            btn1.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams p11 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            btn1.setLayoutParams(p11);
                            btn1.setWeightSum(1.0f);
                            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            btn.setWeightSum(1.0f);
                            btn.setLayoutParams(p1);
                            CircleImageView imageView = new CircleImageView(FriendRequest.this);
                            //imageView.setImageBitmap(img[i]);
                            imageView.setId(15000+i);
                            imageView.setTag(usrid[i]);
                            if (usrtype[i].equals("alumni")) {
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","alumni");
                                        startActivity(r);
                                    }
                                });
                            } else {
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","faculty");

                                        startActivity(r);
                                    }
                                });
                            }
                            k++;
                            imageView.setPadding(0, 2, 0, 2);

                            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            p2.weight = 0.5f;
                            p3.weight = 0.7f;





                            LinearLayout ar=new LinearLayout(FriendRequest.this);
                            ar.setOrientation(LinearLayout.HORIZONTAL);
                            ar.setWeightSum(1.0f);
                            ar.setLayoutParams(p1);

                            Button acc=new Button(FriendRequest.this);
                            acc.setText("Accept");
                            acc.setBackgroundColor(Color.GREEN);
                            acc.setTextColor(Color.WHITE);

                            acc.setTag(usrid[i] + ":" + i);
                            Button rej=new Button(FriendRequest.this);
                            rej.setText("Reject");
                            rej.setBackgroundColor(Color.RED);
                            rej.setTag(usrid[i] + ":" + i);
                            rej.setTextColor(Color.WHITE);

                            acc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new network3().execute(view.getTag() + "", "A", "");
                                }
                            });

                            acc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new network3().execute(view.getTag()+"","R","");
                                }
                            });



                            ar.addView(acc,p2);
                            ar.addView(rej,p2);
                            p2.weight=0.3f;

                            LinearLayout.LayoutParams p22 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,0);
                            p22.weight = 0.5f;

                            TextView b = new TextView(FriendRequest.this);
                            b.setPadding(2, 2, 2, 2);
                            TextView t1 = new TextView(FriendRequest.this);
                            LinearLayout lm = new LinearLayout(FriendRequest.this);
                            lm.setOrientation(LinearLayout.VERTICAL);
                            lm.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            lm.setMinimumHeight(100);
                            lm.setPadding(0, 0, 0, 0);
                            lm.setBackgroundColor(Color.parseColor("#ffffff"));

                            b.setText(title[i]);
                            b.setGravity(Gravity.TOP);
                            t1.setTextSize(25);
                            t1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                            t1.setBackgroundColor(Color.parseColor("#ffd2d2d2"));
                            b.setPadding(10, 5, 5, 10);
                            b.setTextSize(18);
                            b.setBackgroundColor(Color.parseColor("#ffffff"));
                            b.setTextColor(Color.parseColor("#000000"));
                            b.setTag(usrid[i]);
                            b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.80f));
                            if (usrtype[i].equals("alumni")) {
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","alumni");
                                        startActivity(r);
                                    }
                                });
                            } else {
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent r = new Intent(FriendRequest.this, Alumni.class);
                                        r.putExtra("AID", v.getTag() + "");
                                        r.putExtra("utype","faculty");

                                        startActivity(r);
                                    }
                                });
                            }
                            btn1.addView(b,p22);
                            btn1.addView(ar,p22);
                            btn.addView(imageView, p2);
                            btn.addView(btn1, p3);
                            lm.addView(btn);
                            l.addView(lm);
                            l.addView(t1);



                        }
                    }

                    new network1().execute();



                } else
                    Toast.makeText(getBaseContext(), "Error In Connection", Toast.LENGTH_LONG).show();

                //
                // TODO: check this.exception

                // TODO: do something with the feed
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "Problem", Toast.LENGTH_LONG).show();

            }
        }


    }


    class network3 extends AsyncTask<String, Void, String> {
        int frndval2=0;
        int linid=-1;
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendRequest.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {
            int frndval=0;

            if(urls[1].equals("A"))
                frndval=2;
            else
            frndval=1;
            linid=Integer.parseInt(urls[0].substring(urls[0].indexOf(':')+1,urls[0].length()));
            JSONArray jArray = null;
            String result = null;
            StringBuilder sb = null;
            InputStream is = null;
            List r=new ArrayList();
            ArrayList nameValuePairs = new ArrayList();
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2

                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/friendrequest.php?usrid="+urls[0].substring(0,urls[0].indexOf(':'))+"&myid="+name+"&frndval="+frndval);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();

                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();

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
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    frndval2=Integer.parseInt(json_data.getString("checkdf"));
                }
            }
            catch(JSONException e1){
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            try {                pDialog.cancel();

                if(frndval2==0){
                    Toast.makeText(getBaseContext(), "Error in Connecting Request"+frndval2, Toast.LENGTH_LONG).show();

                }
                else if(frndval2==11)
                {
                    Toast.makeText(getBaseContext(), "Connection Request Denied", Toast.LENGTH_LONG).show();
                LinearLayout layout=(LinearLayout)findViewById(linid);
                    layout.setVisibility(View.GONE);
                }
                else if(frndval2==10)
                {
                    Toast.makeText(getBaseContext(), "Error while denying the request", Toast.LENGTH_LONG).show();

                }
                else if(frndval2==21)
                {
                    Toast.makeText(getBaseContext(), "Connection Request Accepted", Toast.LENGTH_LONG).show();
                    LinearLayout layout=(LinearLayout)findViewById(linid);
                    layout.setVisibility(View.GONE);
                }
                else if(frndval2==20)
                {
                    Toast.makeText(getBaseContext(), "Error while Accepting the Request", Toast.LENGTH_LONG).show();

                }


            }

            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }

        }
    }




    class network1 extends AsyncTask<String, Void, String> {
        Bitmap img;
        protected String doInBackground(String... urls) {

            try {
                if(k1<imgpic.length){
                    if(imgpic[k1].equals("null")==false) {
                        URL url = new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + imgpic[k1]);
                        img = (BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                    }
                    else
                    {
                        // URL url = new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + imgpic[k1]);
                        img = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                    }

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

            if(k1<imgpic.length){
                CircleImageView im=(CircleImageView)findViewById(15000+k1);
                im.setImageBitmap(img);
                k1++;
                new network1().execute();
            }

        }

    }




}
