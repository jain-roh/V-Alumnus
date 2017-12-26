package v_alumnus.vkronus.edu.v_alumnus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import v_alumnus.vkronus.edu.v_alumnus.util.FilePicker;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;

public class NewThread extends ActionBarActivity {
EditText t1,t2;
    String title,descrp;
String name,type;
    UUID uniqueKey;
    ProgressDialog pDialog;
    private static final int PICKFILE_RESULT_CODE = 1;
    Button b1,b2;
    TextView textFile;
    private static final int REQUEST_PICK_FILE = 1;
    LinearLayout ly;
    private TextView filePath;
    Button Browse;
    File selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread);
t1=(EditText)findViewById(R.id.threadname);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        t2=(EditText)findViewById(R.id.threadesc);
        b1=(Button)findViewById(R.id.submit_data);
        ly=(LinearLayout)findViewById(R.id.listfile);
       // b2=(Button)findViewById(R.id.submit_file);
       // textFile=(TextView)findViewById(R.id.filetext);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        name=globalVariable.getUsrName();
        type=globalVariable.getType();
        uniqueKey = UUID.randomUUID();
        filePath = (TextView)findViewById(R.id.file_path);
        Browse = (Button)findViewById(R.id.browse);
        Browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewThread.this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);

            }
        });
        //Toast.makeText(getBaseContext(),name+" "+type,Toast.LENGTH_LONG).show();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t1.getText().toString().equals("") || t2.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(),"Please Enter Thread Title or Thread Description",Toast.LENGTH_LONG).show();
                else {
                    try {
                        title = URLEncoder.encode(t1.getText().toString(), "utf-8");
                        descrp = URLEncoder.encode(t2.getText().toString(), "utf-8");
                        ;
                    }
                    catch(Exception e)
                    {

                    }
                    new network().execute("", "", "");
                }
            }
        });
      /*  b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              //  intent.setType("file/*");
               // startActivityForResult(intent, PICKFILE_RESULT_CODE);




               // loadFileList();
                //Intent i = new Intent(NewThread.this, SelectActivity.class);
              //  i.putExtra(SelectActivity.EX_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
              //  i.putExtra(SelectActivity.EX_STYLE, SelectMode.SELECT_FILE);
               // startActivityForResult(i, PATH_RESULT);


                // new Alumni.network2.execute();
                // loadFileList();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_new_thread, menu);
        return true;
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {

                case REQUEST_PICK_FILE:

                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        if(selectedFile.getPath().toString().equals(""))
                        {

                        }
                        else{
                            //Uri returnUri = selectedFile.getPath();
                            //String mimeType = getContentResolver().getType(selectedFile.toURI());

                            Uri selectedUri = Uri.fromFile(selectedFile);
                            String fileExtension
                                    = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                            String mimeType
                                    = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                            filePath.setText(mimeType);
                            try {
                                new AddTask().execute();
                               // postFile("","","","");
                            }
                            catch (Exception e)
                            {

                            }
                            //new AddTask().execute();
                            //Browse.setClickable(false);
                        }

                    }
                    break;
            }
        }
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








    class AddTask extends AsyncTask<Void,Void, Void> {
        String yon="lk";

        protected void onPreExecute() {
            pDialog = new ProgressDialog(NewThread.this);
            pDialog.setMessage("Retrieving data ...");
            pDialog.setIndeterminate(true);
            Browse.setClickable(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
          //  Drawable drawable = new BitmapDrawable(getResources(), imgup);
            // Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), drawable);
            StringBuilder sb=null;
            JSONArray jArray=null;
            String result=null;
            FileInputStream fis=null;
            ByteArrayOutputStream bos=null;
            ArrayList<NameValuePair> nameValuePairs=null;
            InputStream is=null;
            byte[] bytes=null;
            MultipartEntity reqEntity=null;
            //InputStreamEntity reqEntity=null;
            try {
                fis = new FileInputStream(selectedFile);
                //System.out.println(file.exists() + "!!");
                //InputStream in = resource.openStream();
                bos = new ByteArrayOutputStream();
                byte[] buf = new byte[65000];

                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    // System.out.println("read " + readNum + " bytes,");
                }
                bytes = bos.toByteArray();
               /* reqEntity = new InputStreamEntity(
                        new FileInputStream(selectedFile), -1);
                reqEntity.setContentType("binary/octet-stream");
                reqEntity.setChunked(true); // Send in multiple parts if needed*/
             /*   nameValuePairs = new
                        ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("file", Base64.encodeBytes(bytes)));
                nameValuePairs.add(new BasicNameValuePair("uniqid",uniqueKey.toString()));
                nameValuePairs.add(new BasicNameValuePair("name", selectedFile.getName()));*/

                String ext = android.webkit.MimeTypeMap.getFileExtensionFromUrl(selectedFile.getPath());
                String mimeType
                        = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

               // nameValuePairs.add(new BasicNameValuePair("type", ext));


                //reqEntity = new MultipartEntity();

               ContentBody cbFile = new FileBody(selectedFile, mimeType);
                reqEntity = new MultipartEntity();
                String datafile=bytes.toString();
                //reqEntity.addBinaryBody("file", selectedFile);
                reqEntity.addPart("file",  cbFile);
                //reqEntity.addTextBody("name", selectedFile.getName());
                //reqEntity.addPart("uniqkey",  new StringBody(uniqueKey.toString(), Charset.forName("UTF-8")));
                //reqEntity.add("type", ext);
            }
            catch (Exception e)
            {

            }
            try{
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                HttpPost httppost;
                    httppost = new
                            HttpPost("http://sagarsmailbox.esy.es/uploadfile.php");
                httppost.setEntity(reqEntity);
               // httppost.setEntity(nameValuePairs);
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                httpclient.getConnectionManager().closeExpiredConnections();
                httpclient.getConnectionManager().shutdown();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
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


                //news=new Button[jArray.length()];
                // newsdet=new String[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    yon=json_data.getString("yon");
                }
            } catch (JSONException e1) {
                //   Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                e1.printStackTrace();
                // Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
            }



            return(null);
        }



        protected void onPostExecute(Void unused) {
            pDialog.dismiss();
            if(yon.equals("lk"))
                Toast.makeText(getApplicationContext(),"Error in Connection",Toast.LENGTH_SHORT).show();
            else if(yon.equals("ok")) {
                //globalVariable.setBitimg(imgup);
                //globalVariable.setImgType("full");
                //   propic.setImageBitmap(imgup);
                setResult(RESULT_OK, null);
                Browse.setClickable(true);
                selectedFile=null;
                filePath.setText("");
            }
            else
                Toast.makeText(getApplicationContext(),"Error While Uploading Picture",Toast.LENGTH_SHORT).show();

        }
    }





    public String postFile(String fileName, String userName, String password, String macAddress) throws Exception {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://sagarsmailbox.esy.es/uploadfile.php");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

         //File file = new File(fileName);
        FileBody fb = new FileBody(selectedFile);
        String ext = android.webkit.MimeTypeMap.getFileExtensionFromUrl(selectedFile.getPath());

        builder.addPart("file", fb);
        builder.addTextBody("type", ext);
        builder.addTextBody("password", password);
        builder.addTextBody("macAddress",  macAddress);
        final HttpEntity yourEntity = builder.build();

        class ProgressiveEntity implements HttpEntity {
            @Override
            public void consumeContent() throws IOException {
                yourEntity.consumeContent();
            }
            @Override
            public InputStream getContent() throws IOException,
                    IllegalStateException {
                return yourEntity.getContent();
            }
            @Override
            public Header getContentEncoding() {
                return yourEntity.getContentEncoding();
            }
            @Override
            public long getContentLength() {
                return yourEntity.getContentLength();
            }
            @Override
            public Header getContentType() {
                return yourEntity.getContentType();
            }
            @Override
            public boolean isChunked() {
                return yourEntity.isChunked();
            }
            @Override
            public boolean isRepeatable() {
                return yourEntity.isRepeatable();
            }
            @Override
            public boolean isStreaming() {
                return yourEntity.isStreaming();
            } // CONSIDER put a _real_ delegator into here!

            @Override
            public void writeTo(OutputStream outstream) throws IOException {

                class ProxyOutputStream extends FilterOutputStream {
                    /**
                     * @author Stephen Colebourne
                     */

                    public ProxyOutputStream(OutputStream proxy) {
                        super(proxy);
                    }
                    public void write(int idx) throws IOException {
                        out.write(idx);
                    }
                    public void write(byte[] bts) throws IOException {
                        out.write(bts);
                    }
                    public void write(byte[] bts, int st, int end) throws IOException {
                        out.write(bts, st, end);
                    }
                    public void flush() throws IOException {
                        out.flush();
                    }
                    public void close() throws IOException {
                        out.close();
                    }
                } // CONSIDER import this class (and risk more Jar File Hell)

                class ProgressiveOutputStream extends ProxyOutputStream {
                    public ProgressiveOutputStream(OutputStream proxy) {
                        super(proxy);
                    }
                    public void write(byte[] bts, int st, int end) throws IOException {

                        // FIXME  Put your progress bar stuff here!

                        out.write(bts, st, end);
                    }
                }

                yourEntity.writeTo(new ProgressiveOutputStream(outstream));
            }

        };
        ProgressiveEntity myEntity = new ProgressiveEntity();

        post.setEntity(myEntity);
        HttpResponse response = client.execute(post);

        return getContent(response);

    }

    public static String getContent(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String body = "";
        String content = "";

        while ((body = rd.readLine()) != null)
        {
            content += body + "\n";
        }
        return content.trim();
    }









    class network extends AsyncTask<String, Void, String> {

int c=0;
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewThread.this);
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
                HttpPost httppost = new HttpPost("http://sagarsmailbox.esy.es/thread.php");
                nameValuePairs.add(new BasicNameValuePair("usr",name));
                nameValuePairs.add(new BasicNameValuePair("type",type));
                nameValuePairs.add(new BasicNameValuePair("title", title));
                nameValuePairs.add(new BasicNameValuePair("dcp",descrp));
                nameValuePairs.add(new BasicNameValuePair("uniq",uniqueKey.toString()));
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
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    c=json_data.getInt("check");
                   // name=json_data.getString("NAME");//here "Name" is the column name in database
                    return name;
                }
            }
            catch(JSONException e1){
                Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(getBaseContext(), e1.getMessage() ,Toast.LENGTH_LONG).show();
                return e1.toString();
            }

            return null;
        }

        protected void onPostExecute(String feed) {
            //   TextView tr = (TextView) findViewById(R.id.check);
            // tr.setText(name);
            pDialog.dismiss();
            if(c==1) {
                Toast.makeText(getBaseContext(), "Record Created", Toast.LENGTH_LONG).show();
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
              //  b2.setVisibility(View.VISIBLE);

            }
            else
                Toast.makeText(getBaseContext(),"Unexpected Error",Toast.LENGTH_LONG).show();

            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }




   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    File file = new File(data.getData().getPath());
                    //Cursor returnCursor =
                            getContentResolver().query(data.getData(), null, null, null, null);
                    //int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    textFile.setText(file.getName());
                    //textFile.setText(getContentResolver().getType());
                }
                break;

        }
    }*/



}
