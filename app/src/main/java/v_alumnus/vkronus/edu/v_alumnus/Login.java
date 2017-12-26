package v_alumnus.vkronus.edu.v_alumnus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

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

import v_alumnus.vkronus.edu.v_alumnus.app.Config;
import v_alumnus.vkronus.edu.v_alumnus.gcm.GcmIntentService;
import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;
import v_alumnus.vkronus.edu.v_alumnus.util.LoginUser;

public class Login extends Activity{
	Button signup,login;
    EditText usrname,pwd;
    String usr,pswd,name;
    String token;
    RadioGroup rg;
    Intent home;
    RadioButton af;
    int allow=0;
    GlobalClass globalVariable;
    Bitmap bitmap;
    private ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.login);
        token="";
        signup=(Button)findViewById(R.id.signup);
        login=(Button)findViewById(R.id.login);
        usrname=(EditText)findViewById(R.id.eid1);
        home=new Intent(this,MainActivity.class);
        home.putExtra("type",7);
        pwd=(EditText)findViewById(R.id.password1);
        rg=(RadioGroup)findViewById(R.id.selectcat1);
        af=(RadioButton)findViewById(R.id.radio_alumni1);
        af.setSelected(true);
        bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
        globalVariable = (GlobalClass) getApplicationContext();
try {
token=GcmIntentService.tokenno;
}
catch (Exception e) {
    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

}
        // Notify UI that registration has completed, so the progress indicator can be hidden.



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usr=usrname.getText().toString();
                pswd=pwd.getText().toString();

                if(usrname.getText().toString().length()==0)
        {
            Toast.makeText(getBaseContext(),usr,Toast.LENGTH_LONG).show();
            usrname.setError("Please Enter Username");
        }
                else if(pwd.getText().toString().length()==0){
            pwd.setError("Please Enter Password");
        }
          else
        {
            switch (rg.getCheckedRadioButtonId()) {
                case R.id.radio_alumni1: {
                        globalVariable.setType("alumni");
                    // Toast.makeText(getBaseContext(),"Executed",Toast.LENGTH_LONG).show();
                    //  setContentView(R.layout.signup1);
                    new network().execute("");

                    //LoginUser lu=(LoginUser) getApplicationContext();
                    //lu.alumniLogin(usr,pswd);
                    break;
                }

                case R.id.radio_faculty1: {
                    globalVariable.setType("faculty");
                    new network1().execute("");
                    break;

                }
                default: {
                    Toast.makeText(getBaseContext(),"Please Select Valid Option(*Select Alumni Or Faculty)",Toast.LENGTH_LONG).show();
                    break;

                }
            }

        }
            }
        });

  }




    class network extends AsyncTask<String, Void, String> {
int id=-1;
String img="";
        @Override
        protected void onPreExecute() {
            try {
                super.onPreExecute();
                pDialog = new ProgressDialog(Login.this);
                pDialog.setMessage("Loading..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
                catch(Exception e)
            {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

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
                    allow=1;
                    id=Integer.parseInt(json_data.getString("alum_id"));
                    name=json_data.getString("alum_name");
                    img=json_data.getString("alumn_pic");
                    if(img.equals("null"))
                    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                    else
                    bitmap= BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + img).getContent());
                    //here "Name" is the column name in database
                }
            }
            catch(JSONException e1){
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                return e1.toString();
            }
            }
            catch(Exception e)
            {

            }

            return null;
        }

        protected void onPostExecute(String feed) {
            pDialog.cancel();

            pDialog.dismiss();
            try {
                if(id==-1)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new network1().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {

                                }
                                break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
                if(id!=0 && id!=-1)
                {
                    SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.putExtra("type", 7);
                    home.putExtra("alum_id", id);
                    startActivity(home);
                    finish();
                    globalVariable.setUsrName(usr);
                    globalVariable.setPwd(pswd);
                    globalVariable.setBitimg(bitmap);
                    globalVariable.setImgType("thumb");
                    globalVariable.setImgName(img);
                    globalVariable.setType("alumni");
                    globalVariable.setAlumId("" + id);
                    GlobalClass.setName(name);

                    mydatabase.execSQL("INSERT INTO UserLogin VALUES('" + globalVariable.getUsrName() + "','" + globalVariable.getPwd() + "','" + globalVariable.getType() + "')");
                    mydatabase.close();
                }
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
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	} 
	
	public void show(String str)
	{
	Toast.makeText(this, str, Toast.LENGTH_LONG).show();	
	}








    class network1 extends AsyncTask<String, Void, String> {
        int id1=-1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
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


            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//http post
            try {


                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    //Why to use 10.0.2.2
                    HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/faclogin.php?usr=" + usr + "&pwd=" + pswd);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
                    for (int i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);
                        id1 = Integer.parseInt(json_data.getString("alum_id"));
                        name=json_data.getString("alum_name");

                        String img = json_data.getString("alumn_pic");
                        if (img.equals("null"))
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                        else
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepicthumb/" + img).getContent());
                        //here "Name" is the column name in database
                    }
                } catch (JSONException e1) {
                    return e1.toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return e1.toString();
                }
            }
            catch(Exception e)
            {
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            pDialog.cancel();

            pDialog.dismiss();
            try {
                if(id1!=-1 && id1!=0)
                {
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.putExtra("type", 7);
                    //Toast.makeText(getBaseContext(),"Successful Logged In",Toast.LENGTH_LONG).show();
                    startActivity(home);
                    globalVariable.setUsrName(usr);
                    globalVariable.setPwd(pswd);
                    globalVariable.setBitimg(bitmap);
                    globalVariable.setAlumId("" + id1);
                    GlobalClass.setName(name);

                    globalVariable.setImgType("thumb");
                    globalVariable.setType("faculty");
                    SQLiteDatabase mydatabase = openOrCreateDatabase("VALUM", MODE_PRIVATE, null);

                    mydatabase.execSQL("INSERT INTO UserLogin VALUES('" + globalVariable.getUsrName() + "','" + globalVariable.getPwd() + "','" + globalVariable.getType() + "')");

                    // Toast.makeText(getBaseContext(),globalVariable.getType(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(id1==-1) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    new network1().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE: {

                                }
                                break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Connection Timeout \n Click Yes to Retry").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
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
