package v_alumnus.vkronus.edu.v_alumnus.util;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.R;
import v_alumnus.vkronus.edu.v_alumnus.gcm.GcmIntentService;

/**
 * Created by Rohit on 2/17/2016.
 */
public class LoginUser extends Application{
     static boolean login=false;
    ProgressDialog pDialog;
    String usr,pswd,token,type;
    GlobalClass globalVariable;
    Bitmap bitmap;
    public boolean alumniLogin()
    {
        SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
        Cursor res =  mydatabase.rawQuery("select * from UserLogin", null );
        res.moveToFirst();
        globalVariable=(GlobalClass)getApplicationContext();

        while(res.isAfterLast() == false){
            usr=res.getString(res.getColumnIndex("Username"));
            pswd=res.getString(res.getColumnIndex("Password"));
            type=res.getString(res.getColumnIndex("type"));

            res.moveToNext();
        }
        try {
            token= GcmIntentService.tokenno;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        if(type.equals("alumni"))
        new network().execute();
        else
        return false;

        return login;
    }

    class network extends AsyncTask<String, Void, String> {
        int id=-1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginUser.this);
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
            try {


                try{
                    HttpClient httpclient = new DefaultHttpClient();

                    //Why to use 10.0.2.2
                    HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/alumnilogin.php");
                    nameValuePairs.add(new BasicNameValuePair("usr",usr));
                    nameValuePairs.add(new BasicNameValuePair("pwd",pswd));
                    nameValuePairs.add(new BasicNameValuePair("token",token));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

                    httpclient.getConnectionManager().closeExpiredConnections();
                    httpclient.getConnectionManager().shutdown();
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
                    for(int i=0;i<jArray.length();i++){
                        json_data = jArray.getJSONObject(i);
                        id=Integer.parseInt(json_data.getString("alum_id"));
                        String img=json_data.getString("alumn_pic");
                        if(img.equals("null"))
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                        else
                            bitmap= BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepic/" + img).getContent());
                        //here "Name" is the column name in database
                    }
                }
                catch(JSONException e1){
                    Toast.makeText(getBaseContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                    return e1.toString();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getBaseContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            try {
                pDialog.dismiss();
                if(id!=0 && id!=-1)
                {
                    SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);

                    login=true;
                    globalVariable.setUsrName(usr);
                    globalVariable.setPwd(pswd);
                    globalVariable.setBitimg(bitmap);
                    globalVariable.setType("alumni");
                    mydatabase.execSQL("INSERT INTO UserLogin VALUES('" + globalVariable.getUsrName() + "','" + globalVariable.getPwd() + "','" + globalVariable.getType() + "')");
                    mydatabase.close();
                }
                else if(id==-1)
                    Toast.makeText(getBaseContext(),"Error In Connection",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(getBaseContext(),"Wrong Username or Password",Toast.LENGTH_LONG).show();

                }

            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }

}
