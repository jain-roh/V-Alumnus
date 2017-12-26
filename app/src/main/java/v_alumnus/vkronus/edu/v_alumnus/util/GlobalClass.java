package v_alumnus.vkronus.edu.v_alumnus.util;

/**
 * Created by ZAID on 8/10/2015.
 */import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
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

public class GlobalClass extends Application{
    private static String usrname;
    private static String usrpwd;
    private static String type;
    private static Bitmap bitimg;
    private static String imgtype;
    private static String imgnm;
    private static String name;
    private static String alumid;
    private static int chatopen;
    private static String ausr;

    public static void setChatOpen(int chto)
    {
        chatopen=chto;
    }
    public static int getChatOpen()
    {
        return chatopen;
    }

    public static void setAUsr(String aus)
    {
        ausr=aus;
    }
    public static String getAUsr()
    {
        return ausr;
    }


    public static void setAlumId(String almid)
    {
        alumid=almid;
    }
    public static String getAlumId()
    {
        return alumid;
    }

    public static void setImgType(String imagetype)
   {
       imgtype=imagetype;
   }
    public static String getImgType()
    {
        return imgtype;
    }

    public static void setName(String nm)
    {
        name=nm;
    }
    public static String getName()
    {
        return name;
    }


    public static void setImgName(String imagenm)
    {
        imgnm=imagenm;
    }
    public static String getImgName()
    {
        return imgnm;
    }

    public static String getUsrName() {
   return usrname;}

    public static void setBitimg(Bitmap bmp)
    {
        bitimg=bmp;
    }

    public static Bitmap getBitimg()
    {
        return bitimg;
    }
    public static void setUsrName(String aName) {
        usrname = aName;
        }
   public static String getPwd() {
    return usrpwd;}
   public static void setPwd(String aEmail) {
usrpwd = aEmail;
   }

    public static String getType() {
        return type;}
    public static void setType(String usrtype) {
        type = usrtype;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

}
