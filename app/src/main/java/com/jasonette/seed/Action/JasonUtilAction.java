package com.jasonette.seed.Action;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.Helper.JasonImageHelper;
import com.google.android.material.snackbar.Snackbar;
import com.jasonette.seed.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class JasonUtilAction {
    private int counter; // general purpose counter;
    private Intent callback_intent;  // general purpose intent;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void banner(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject options = action.getJSONObject("options");
                    String title = "Notice";
                    String result = "";
                    if (options.has("title")) {
                        title = options.get("title").toString();
                    }
                    if (options.has("description")) {
                        result = title + "\n" + options.get("description").toString();
                    } else {
                        result = title;
                    }
                    Snackbar snackbar = Snackbar.make(((JasonViewActivity)context).rootLayout, result, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void toast(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject options = action.getJSONObject("options");
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, (CharSequence)options.get("text").toString(), duration);
                    toast.show();
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void flashlight(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ContextCompat.checkSelfPermission((JasonViewActivity)context,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                                (JasonViewActivity)context, Manifest.permission.CAMERA)) {
                        } else {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.CAMERA},
                                    50);
                        }
                    }

                    CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null;
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, true);   //Turn ON
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void sms(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject options = action.getJSONObject("options");
                    String number = options.get("url").toString();  // The number on which you want to send SMS
                    ContextCompat.startActivity(context,new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)),null);

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void emails(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject options = action.getJSONObject("options");

                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{   options.get("email").toString()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,  options.get("subject").toString());
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,  options.get("message").toString());

                    try {
                        ContextCompat.startActivity(context,emailIntent,null);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Log.d("Warning", ex.getStackTrace()[0].getMethodName() + " : " + ex.toString());
                    }

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void vibrate(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void dial(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    int permissionCheck = ContextCompat.checkSelfPermission((JasonViewActivity)context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                (JasonViewActivity)context,
                                new String[]{Manifest.permission.CALL_PHONE},123);
                    }
                    else{
                        JSONObject options = action.getJSONObject("options");
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+(CharSequence)options.get("url").toString()));
                        ContextCompat.startActivity((JasonViewActivity)context,callIntent,null);
                    }
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void wifiState(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = conMan.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        Log.d("WifiReceiver", "Have Wifi Connection");
                        try {
                            JasonHelper.next("success", action, true, event, context);
                        } catch (Exception e) {
                            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    } else {
                        Log.d("WifiReceiver", "Don't have Wifi Connection");
                        try {
                            JasonHelper.next("success", action, false, event, context);
                        } catch (Exception e) {
                            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
    }
    public void fileCreate(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    List<String> listPermissionsNeeded = new ArrayList<>();
                    int writepermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (writepermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions((JasonViewActivity)context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_EXTERNAL_STORAGE);
                    }

                    JSONObject options = action.getJSONObject("options");
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + (CharSequence)options.get("name").toString());
                    file.createNewFile();
                    try {
                        JasonHelper.next("success", action, "File Created", event, context);
                    } catch (Exception e) {
                        Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                    }

                    boolean temporary = (boolean)options.get("temporary");
                    if(temporary  ==  true){
                        //deleting the file
                        file.delete();
                        System.out.println();
                        Log.d("Warning", "file deleted");
                        try {
                            JasonHelper.next("success", action, "File deleted", event, context);
                        } catch (Exception e) {
                            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void fileWrite(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> listPermissionsNeeded = new ArrayList<>();
                    int writepermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (writepermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions((JasonViewActivity)context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_EXTERNAL_STORAGE);
                    }

                    JSONObject options = action.getJSONObject("options");
                    String string = options.get("text").toString();
                    File gpxfile = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + (CharSequence)options.get("file").toString());
                    FileWriter writer = new FileWriter(gpxfile,true);
                    writer.append(string+"\n\n");
                    writer.flush();
                    writer.close();

                    try {
                        JasonHelper.next("success", action, "Writing file", event, context);
                    } catch (Exception ex) {
                        Log.d("Warning", ex.getStackTrace()[0].getMethodName() + " : " + ex.toString());
                    }

                } catch (Exception e){
                    try {
                        JasonHelper.next("success", action, "Error writing file", event, context);
                    } catch (Exception ep) {
                        Log.d("Warning", ep.getStackTrace()[0].getMethodName() + " : " + ep.toString());
                    }
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void fileRead(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> listPermissionsNeeded = new ArrayList<>();
                    int writepermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (writepermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions((JasonViewActivity)context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_EXTERNAL_STORAGE);
                    }

                    JSONObject options = action.getJSONObject("options");
                    File fileEvents = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + (CharSequence)options.get("file").toString());
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(fileEvents));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text.append(line);
                        }
                        br.close();
                    }catch(IOException e){}

                    String result = text.toString();
                    data.put("resultat",result);
                    try {
                        JasonHelper.next("success", action, data, event, context);
                    } catch (Exception e) {
                        Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                    }
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
    }
    public void screenshot(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        final Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {

                    int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        ActivityCompat.requestPermissions(
                                ((JasonViewActivity)context),
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );
                    }

                    try {
                        // image naming and path  to include sd card  appending name you choose for file
                        String mPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Screenshots/" + now + ".jpg";

                        // create bitmap screen capture
                        View v1 = ((JasonViewActivity)context).getWindow().getDecorView().getRootView();
                        v1.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        v1.setDrawingCacheEnabled(false);

                        File imageFile = new File(mPath);

                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();

                    } catch (Throwable e) {
                        // Several error may come out with file handling or DOM
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, "/DCIM/Screenshots/" + now + ".jpg", event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }

    public void localNotification(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject options = action.getJSONObject("options");

                    NotificationChannel notificationChannel = new NotificationChannel("rtrr" , "rtrr", NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableLights(true);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(notificationChannel);

                    NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "rtrr")
                            .setContentTitle((CharSequence)options.get("title").toString())
                            .setContentText((CharSequence)options.get("text").toString())
                            .setSmallIcon(R.mipmap.ic_launcher);

                    notificationManager.notify(1, notification.build());

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }

    public void alert(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                try {
                    JSONObject options = new JSONObject();
                    final ArrayList<EditText> textFields = new ArrayList<EditText>();
                    if (action.has("options")) {
                        options = action.getJSONObject("options");
                        String title = options.get("title").toString();
                        String description = options.get("description").toString();
                        builder.setTitle(title);
                        builder.setMessage(description);

                        if(options.has("form"))
                        {
                            LinearLayout lay = new LinearLayout(context);
                            lay.setOrientation(LinearLayout.VERTICAL);
                            lay.setPadding(20,5,20,5);
                            JSONArray formArr =  options.getJSONArray("form");
                            for (int i=0; i<formArr.length();i++) {
                                JSONObject obj = formArr.getJSONObject(i);
                                final EditText textBox = new EditText(context);
                                if(obj.has("placeholder")){
                                    textBox.setHint(obj.getString("placeholder"));
                                }
                                if(obj.has("type") && obj.getString("type").equals("secure")){
                                    textBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                }
                                if(obj.has("value")){
                                    textBox.setText(obj.getString("value"));
                                }
                                textBox.setTag(obj.get("name"));
                                lay.addView(textBox);
                                textFields.add(textBox);
                                builder.setView(lay);
                            }
                            // Set focous on first text field
                            final EditText focousedTextField = (EditText)textFields.get(0);
                            focousedTextField.post(new Runnable() {
                                public void run() {
                                    focousedTextField.requestFocus();
                                    InputMethodManager lManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    lManager.showSoftInput(focousedTextField, 0);
                                }
                            });


                        }


                    }
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    try {
                                        if (action.has("success")) {
                                            JSONObject postObject = new JSONObject();
                                            if(action.getJSONObject("options").has("form")){
                                                for(int i = 0; i<textFields.size();i++){

                                                    EditText textField = (EditText) textFields.get(i);
                                                    postObject.put(textField.getTag().toString(),textField.getText().toString());
                                                }
                                            }
                                            JasonHelper.next("success", action, postObject, event, context);
                                        }
                                    } catch (Exception err) {

                                    }
                                }
                            });
                    builder.setNeutralButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    JasonHelper.next("error", action, new JSONObject(), event, context);
                                }
                            });
                    builder.show();
                } catch (Exception e) {
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
    }
    public void picker(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject options = action.getJSONObject("options");
                    if(options.has("items")){
                        final JSONArray items = options.getJSONArray("items");
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        if(options.has("title")){
                            String title = options.get("title").toString();
                            builder.setTitle(title);
                        }

                        ArrayList<String> listItems = new ArrayList<String>();
                        for (int i = 0; i < items.length() ; i++) {
                            JSONObject item = (JSONObject)items.get(i);
                            if(item.has("text")){
                               listItems.add(item.getString("text"));
                            } else {
                                listItems.add("");
                            }
                        }
                        final CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                        builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int val) {
                                JSONObject item;
                                try {
                                    item = items.getJSONObject(val);
                                    if(item.has("action")){
                                        Intent intent = new Intent("call");
                                        intent.putExtra("action", item.get("action").toString());
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                    } else if(item.has("href")){
                                        Intent intent = new Intent("call");
                                        JSONObject href = new JSONObject();
                                        href.put("type", "$href");
                                        href.put("options", item.get("href"));
                                        intent.putExtra("action", href.toString());
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                    }
                                } catch (Exception e){
                                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                                }
                            }
                        });
                        builder.setNeutralButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int val) {
                                }
                            }
                        );
                        builder.create().show();
                    }
                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public void datepicker(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    showTimePickerDialog(action, event, year, monthOfYear, dayOfMonth, context);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.setTitle("Select Date");
                    datePickerDialog.show();

                } catch (Exception e){
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        });
        try {
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }

    private void showTimePickerDialog(final JSONObject action, final JSONObject event, final int year, final int monthOfYear, final int dayOfMonth, final Context context) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Date date = new Date(year, monthOfYear, dayOfMonth, selectedHour, selectedMinute);

                try {
                    String val = String.valueOf(date.getTime()/1000);
                    JSONObject value = new JSONObject();
                    value.put("value", val);
                    JasonHelper.next("success", action, value, event, context);
                } catch (Exception e) {
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void addressbook(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.requestPermissions((JasonViewActivity)context,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        10);
                getContacts(action, data, event, context);
            }
        }).start();
    }
    private void getContacts(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        JSONArray contactList = new JSONArray();
        String phoneNumber = null;
        String email = null;
        ContentResolver contentResolver = ((JasonViewActivity)context).getContentResolver();
        try {
            final Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                counter = 0;
                while (cursor.moveToNext()) {
                    JSONObject contact = new JSONObject();
                    String contact_id = cursor.getString(cursor.getColumnIndex( ContactsContract.Contacts._ID ));
                    String name = cursor.getString(cursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME ));
                    try {
                        // name
                        contact.put("name", name);

                        // phone
                        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if(phoneNumber != null) {
                                contact.put("phone", phoneNumber);
                            }
                        }
                        if(!contact.has("phone")){
                            contact.put("phone", "");
                        }
                        phoneCursor.close();

                        // email
                        Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            if(email != null) {
                                contact.put("email", email);
                            }
                        }
                        if(!contact.has("email")){
                            contact.put("email", "");
                        }
                        emailCursor.close();

                        // Add to array
                        contactList.put(contact);
                    } catch (Exception e){
                        Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                    }
                }
                try {
                    JasonHelper.next("success", action, contactList, event, context);
                } catch (Exception e) {
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        } catch (SecurityException e){
            JasonHelper.permission_exception("$util.addressbook", context);
        }
    }

    public void share(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject options = action.getJSONObject("options");
                    if (options.has("items")) {
                        callback_intent = new Intent();
                        callback_intent.setAction(Intent.ACTION_SEND);

                        final JSONArray items = options.getJSONArray("items");
                        counter = 0;
                        final int l = items.length();
                        for (int i = 0; i < l; i++) {
                            JSONObject item = (JSONObject) items.get(i);
                            if (item.has("type")) {
                                String type = item.getString("type");
                                if (type.equalsIgnoreCase("text")) {
                                    callback_intent.putExtra(Intent.EXTRA_TEXT, item.get("text").toString());
                                    if (callback_intent.getType() == null) {
                                        callback_intent.setType("text/plain");
                                    }
                                    counter++;
                                    if (counter == l) {
                                        JasonHelper.next("success", action, new JSONObject(), event, context);
                                        context.startActivity(Intent.createChooser(callback_intent, "Share"));
                                    }
                                } else if (type.equalsIgnoreCase("image")) {
                                    // Fetch remote url
                                    // Turn it into Bitmap
                                    // Create a file
                                    // Share the url
                                    if (item.has("url")) {
                                        JasonImageHelper helper = new JasonImageHelper(item.getString("url"), context);
                                        helper.setListener(new JasonImageHelper.JasonImageDownloadListener() {
                                            @Override
                                            public void onLoaded(byte[] data, Uri uri) {
                                                callback_intent.putExtra(Intent.EXTRA_STREAM, uri);
                                                // override with image type if one of the items is an image
                                                callback_intent.setType("image/*");
                                                callback_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                counter++;
                                                if (counter == l) {
                                                    JasonHelper.next("success", action, new JSONObject(), event, context);
                                                    context.startActivity(Intent.createChooser(callback_intent, "Share"));
                                                }
                                            }
                                        });
                                        helper.fetch();
                                    } else if (item.has("data")) {
                                        // "data" is a byte[] stored as string
                                        // so we need to restore it back to byte[] before working with it.
                                        byte[] d = Base64.decode(item.getString("data"), Base64.DEFAULT);

                                        JasonImageHelper helper = new JasonImageHelper(d, context);
                                        helper.setListener(new JasonImageHelper.JasonImageDownloadListener() {
                                            @Override
                                            public void onLoaded(byte[] data, Uri uri) {
                                                callback_intent.putExtra(Intent.EXTRA_STREAM, uri);
                                                // override with image type if one of the items is an image
                                                callback_intent.setType("image/*");
                                                callback_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                counter++;
                                                if (counter == l) {
                                                    JasonHelper.next("success", action, new JSONObject(), event, context);
                                                    context.startActivity(Intent.createChooser(callback_intent, "Share"));
                                                }
                                            }
                                        });
                                        helper.load();
                                    }
                                } else if (type.equalsIgnoreCase("video")){
                                    if(item.has("file_url")){
                                        Uri uri = Uri.parse(item.getString("file_url"));
                                        callback_intent.putExtra(Intent.EXTRA_STREAM, uri);
                                        // override with image type if one of the items is an image
                                        callback_intent.setType("video/*");
                                        callback_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        counter++;
                                        if (counter == l) {
                                            JasonHelper.next("success", action, new JSONObject(), event, context);
                                            context.startActivity(Intent.createChooser(callback_intent, "Share"));
                                        }
                                    }
                                } else if (type.equalsIgnoreCase("audio")){
                                    if(item.has("file_url")){
                                        Uri uri = Uri.parse(item.getString("file_url"));
                                        callback_intent.putExtra(Intent.EXTRA_STREAM, uri);
                                        callback_intent.setType("audio/*");
                                        callback_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        counter++;
                                        if (counter == l) {
                                            JasonHelper.next("success", action, new JSONObject(), event, context);
                                            context.startActivity(Intent.createChooser(callback_intent, "Share"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                }
            }
        }).start();
    }
}
