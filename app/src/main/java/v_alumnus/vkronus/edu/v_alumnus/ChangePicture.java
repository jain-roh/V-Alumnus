package v_alumnus.vkronus.edu.v_alumnus;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import v_alumnus.vkronus.edu.v_alumnus.util.GlobalClass;



public class ChangePicture extends ActionBarActivity {

    private static final int MY_INTENT_CLICK=10;
    private String selectedImagePath=null;
    private ImageView img;
    Uri selectedImageUri;

    ProgressDialog pDialog;
    InputStream is;
    Bitmap imgup;
    ImageView propic;
    GlobalClass globalVariable;
            String name,type;
Button load,change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_picture);
        setTitle("Update Picture");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3A2EA5")));
        load=(Button)findViewById(R.id.buttonLoadPicture);
        change=(Button)findViewById(R.id.change);
        propic=(ImageView)findViewById(R.id.image_view);
        globalVariable=(GlobalClass) getApplicationContext();
        if(GlobalClass.getImgType().equals("thumb"))
            new network().execute();

            name=GlobalClass.getUsrName();
        type=GlobalClass.getType();
        img = (ImageView)findViewById(R.id.imgView);

        img.setImageBitmap(GlobalClass.getBitimg());
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
               // startActivityForResult(Intent.createChooser(intent, "Select File"),MY_INTENT_CLICK);
                startActivityForResult(intent,MY_INTENT_CLICK);
            }
        });
change.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(selectedImagePath==null)
            Toast.makeText(getBaseContext(),"No Image Loaded",Toast.LENGTH_SHORT).show();
        else
            new AddTask().execute();
    }
});

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       try {
           if (resultCode == RESULT_OK) {
               if (requestCode == MY_INTENT_CLICK) {
                   selectedImageUri = data.getData();
                   selectedImagePath = getPath(selectedImageUri);
                   if (selectedImagePath == null) {
                       Toast.makeText(getBaseContext(), "Image Not Loaded", Toast.LENGTH_LONG).show();
                   } else {
                       img.setImageURI(selectedImageUri);
                       imgup = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                       imgup = Bitmap.createScaledBitmap(imgup, 350, 250, false);
                   }
               }
           }
       }
       catch(Exception e)
       {

       }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    class AddTask extends AsyncTask<Void,Void, Void> {
    String yon="lk";

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ChangePicture.this);
            pDialog.setMessage("Retrieving data ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
           // Drawable drawable = new BitmapDrawable(getResources(), imgup);
               // Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), drawable);
            StringBuilder sb=null;
            JSONArray jArray=null;
            String result=null;
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            imgup.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte [] ba = bao.toByteArray();
            String ba1=Base64.encodeBytes(ba);
            ArrayList<NameValuePair> nameValuePairs = new
                    ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image",ba1));

            nameValuePairs.add(new BasicNameValuePair("name",name));
            nameValuePairs.add(new BasicNameValuePair("type",type));
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost;
                if(type.equals("alumni")) {
                    httppost = new
                            HttpPost("http://sagarsmailbox.esy.es/updatepp.php");
                }
                else {
                    httppost = new
                            HttpPost("http://sagarsmailbox.esy.es/updatepp1.php");
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

        protected void onProgressUpdate(Void... unused) {
            // grid_main.setAdapter(imgadp);
        }

        protected void onPostExecute(Void unused) {
            pDialog.dismiss();
            if(yon.equals("lk"))
                Toast.makeText(getApplicationContext(),"Error in Connection",Toast.LENGTH_SHORT).show();
            else if(yon.equals("ok")) {
                globalVariable.setBitimg(imgup);
                globalVariable.setImgType("full");
                //   propic.setImageBitmap(imgup);
                setResult(RESULT_OK, null);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(),"Error While Uploading Picture",Toast.LENGTH_SHORT).show();

        }
    }




    class network extends AsyncTask<String, Void, String> {

       ProgressDialog pDialog;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePicture.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.onBackPressed();
            pDialog.setCancelable(true);
            pDialog.show();
        }



        protected String doInBackground(String... urls) {




                try {
                   if(globalVariable.getImgName().equals("null")) {
                       bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpp);
                        globalVariable.setImgType("full");
                   }
                   else {

                       bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://sagarsmailbox.esy.es/profilepic/" + globalVariable.getImgName()).getContent());
                       globalVariable.setImgType("full");
                   }
                } catch (Exception e) {
                    return e.toString();
                }
//convert response
            return null;
        }

        protected void onPostExecute(String feed) {
            pDialog.dismiss();
            try {
                globalVariable.setBitimg(bitmap);
                img.setImageBitmap(globalVariable.getBitimg());
                propic.setImageBitmap(globalVariable.getBitimg());
            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            // TODO: check this.exception

            // TODO: do something with the feed
        }
    }




}







             /*   String sourceFileUri = selectedImagePath;
               int serverResponseCode;
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = "http://sagarsmailbox.esy.es/updatepp.php";

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        if (serverResponseCode == 200) {

                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } // End else block

*/




