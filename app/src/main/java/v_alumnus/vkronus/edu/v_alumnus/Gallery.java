package v_alumnus.vkronus.edu.v_alumnus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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


public class Gallery extends ActionBarActivity {
    String galtitle,galimg,galdesc,galcode;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private ProgressDialog pDialog;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    int c=-1,j=0,m=0;
    LinearLayout ltp;

    int offset=0;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));

        mContext = this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
        new network().execute("");
    }
    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    if(offset<c && m==j)
                    {
                        new network1().execute();
                        m++;
                    }
                    else {
                        mViewFlipper.showNext();
                        m++;
                    }
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    if(offset==0)
                    {
                        // new network1().execute();
                    }
                    else {
                        m--;
                        mViewFlipper.showPrevious();
                    }

                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }


    class network extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;



            InputStream is = null;
            List r=new ArrayList();

            j=-1;
            ArrayList nameValuePairs = new ArrayList();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/galimgcount.php");

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
                    c=json_data.getInt("img_count");
                    j=0;
                    // bitmap[i] = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/gallerytitle/" + galimg[i]).getContent());

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
                if(c==-1)
                {
                    Toast.makeText(getBaseContext(),"Connection Problem ",Toast.LENGTH_SHORT).show();
                }
                else if(c==0)
                {
                    Toast.makeText(getBaseContext(),"No Images  In Gallery ",Toast.LENGTH_SHORT).show();
                }
                else
                    new network1().execute("","","");

            }
            catch(Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG ).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }

    class network1 extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Gallery.this);
            pDialog.setMessage("Check For the Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... urls) {

            JSONArray jArray = null;

            String result = null;

            StringBuilder sb = null;


            galcode="0";
            InputStream is = null;
            List r=new ArrayList();


            ArrayList nameValuePairs = new ArrayList();
//http post
            try{
                HttpClient httpclient = new DefaultHttpClient();

                //Why to use 10.0.2.2
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/galleryt.php?offset="+offset);

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
                    galtitle=json_data.getString("gallery_name");
                    galdesc=json_data.getString("gallery_desc");
                    galcode=json_data.getString("gallery_code");
                    galimg=json_data.getString("gallery_img");
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/gallerythumbnail/" + galimg).getContent());

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
                if(galcode.equals("-1"))
                {
                    Toast.makeText(getBaseContext(),"No Data Found ",Toast.LENGTH_SHORT).show();
                }
                else if (galcode.equals("0"))
                {
                    Toast.makeText(getBaseContext(),"Connection Problem ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    offset++;
                    LinearLayout r=new LinearLayout(Gallery.this);
                    r.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    r.setOrientation(LinearLayout.VERTICAL);
                    TextView t=new TextView(Gallery.this);

                    t.setPaintFlags(t.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                    t.setTextSize(15);
                    t.setText(galtitle);
                    t.setBackgroundColor(Color.parseColor("#ffffff"));
                    t.setTextColor(Color.parseColor("#000000"));
                    TextView t1=new TextView(Gallery.this);
                    t1.setTextSize(12);
                   t1.setText(galdesc);
                    t1.setBackgroundColor(Color.parseColor("#ffffff"));
                    t.setTextColor(Color.parseColor("#000000"));
                    t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ImageView k=new ImageView(Gallery.this);
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    k.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    k.setImageBitmap(bitmap);
                    // params.gravity = Gravity.CENTER;
                    // k.setAdjustViewBounds(true);
                    // RelativeLayout lp=new RelativeLayout(Gallery.this);
                    //   lp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                    //     lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    //   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT); // or wrap_content
                    //  layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    Button b=new Button(Gallery.this);
                    b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setBackgroundColor(Color.parseColor("#4d4dff"));

                    b.setText("Go To Gallery");
                    b.setTextColor(Color.parseColor("#ffffff"));
                    b.setId(Integer.parseInt(galcode));
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent r=new Intent(Gallery.this,GalleryImage.class);
                            r.putExtra("GID",v.getId());
                            startActivity(r);
                        }
                    });
                   // r.addView(t);

                    r.addView(k);
                   // r.addView(t1);

                  //  r.addView(b);
                    mViewFlipper.addView(r);

                    // if(j!=1) {
                    mViewFlipper.showNext();
m++;
                    //   m++;

                    //}
                    //else
                    // {
                    j++;
                    //}

                }
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
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
