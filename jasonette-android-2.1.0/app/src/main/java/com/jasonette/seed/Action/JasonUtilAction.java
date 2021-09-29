package com.jasonette.seed.Action;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.github.florent37.androidnosql.NoSql;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void getPermissions(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int doPerm = 0;
            String allPerm[] = new String[0];
            try {
                JSONObject options = action.getJSONObject("options");
                if (options.has("permissions")) {
                    String[] commaSeparatedArr = options.getString("permissions").split("\\s*,\\s*");
                    ArrayList<String> listItems = new ArrayList<>(Arrays.asList(commaSeparatedArr));
                    allPerm = new String[listItems.size()];
                    for (int i = 0; i < listItems.size(); i++) {
                        String newPer = new String("android.permission.") + listItems.get(i).toUpperCase();
                        if (ContextCompat.checkSelfPermission(context, newPer) != PackageManager.PERMISSION_GRANTED) {
                            allPerm[doPerm] = newPer;
                            doPerm++;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            if (doPerm >= 1) {
                JasonHelper.next("success", action, new JSONObject(), event, context);
                ActivityCompat.requestPermissions((JasonViewActivity) context, allPerm, 0);
            } else {
                JasonHelper.next("error", action, new JSONObject(), event, context);
            }
        } else {
            JasonHelper.next("error", action, new JSONObject(), event, context);
        }

    }
    public void progressNotification(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                int incr = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "rssr";
                    String description = "description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("rrsr", name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                JSONObject options = action.getJSONObject("options");
                String title = options.get("title").toString();
                String content = options.get("content").toString();
                Integer max = options.getInt("max");

                for (incr = 0; incr <= max; incr+=1) {
                    // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "rrsr")
                            .setSmallIcon(R.mipmap.ic_notification)
                            .setContentTitle(title)
                            .setContentText(content)
                            .setProgress(max,incr,false)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(1, builder.build());
                    try {
                        // Sleep for 1 second
                        Thread.sleep(1*10);
                    } catch (InterruptedException e) {
                        Log.d("TAG", "sleep failure");
                    }
                }
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
    public void idleDisable(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "unlock");
                wakeLock.acquire();
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
    public void closeApp(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                // Call closing apps
                ((JasonViewActivity)context).finish();
                System.exit(0);
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
    public void darkModeState(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                int nightModeFlags =
                        ((JasonViewActivity)context).getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        JasonHelper.next("success", action, true, event, context);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        JasonHelper.next("success", action, false, event, context);
                        break;
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        JasonHelper.next("success", action, "undefined", event, context);
                        break;
                }
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
    public void recaptcha(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                SafetyNet.getClient(context).verifyWithRecaptcha("6LdEMU8aAAAAADfKZvnbTldG2qJritMQwMsCAn-k")
                    .addOnSuccessListener(((JasonViewActivity)context) ,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                            // Indicates communication with reCAPTCHA service was
                            // successful.
                            String userResponseToken = response.getTokenResult();
                            if (!userResponseToken.isEmpty()) {
                                // Validate the user response token using the
                                // reCAPTCHA siteverify API.
                                JasonHelper.next("success", action, true, event, context);
                                Log.d("Result", ""+true);
                            }
                            }
                        })
                    .addOnFailureListener(( (JasonViewActivity)context), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.d("Error", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d("Error", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                        }
                    });
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
    public void saveData(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                NoSql noSql;
                noSql = NoSql.getInstance();
                JSONObject options = action.getJSONObject("options");
                String uri = options.get("uri").toString();
                String value = options.get("value").toString();
                noSql.put(uri, value);
                noSql.save();
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
        try {
            JasonHelper.next("success", action, true, event, context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void getByPathData(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                NoSql noSql;
                noSql = NoSql.getInstance();
                JSONObject options = action.getJSONObject("options");
                String uri = options.get("uri").toString();
                String type = options.get("type").toString();

                if(type.equals("String")== true) {
                    String result = noSql.get(uri).toString();
                    try {
                        JasonHelper.next("success", action, result, event, context);
                    } catch (Exception e) {
                        Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                    }
                }
                else {
                    int result = noSql.get(uri).integer();
                    try {
                        JasonHelper.next("success", action, ""+result, event, context);
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
    public void removeByPathData(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                NoSql noSql;
                noSql = NoSql.getInstance();
                JSONObject options = action.getJSONObject("options");
                String uri = options.get("uri").toString();
                noSql.remove(uri);

            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
        try {
            JasonHelper.next("success", action, true, event, context);
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
    public void bluetoothState(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                } else if (!mBluetoothAdapter.isEnabled()) {
                    JasonHelper.next("success", action, false, event, context);
                } else {
                    JasonHelper.next("success", action, true, event, context);
                }
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
    public void bluetoothSend(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                JSONObject options = action.getJSONObject("options");

                String path = Environment.getExternalStorageDirectory() + "/"
                        + options.get("filename").toString();

                File file = new File(path);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType(options.get("mime").toString());
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                ContextCompat.startActivity((JasonViewActivity)context,intent,null);
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
                System.out.println("" + temporary);

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
    public void facebookAuth(final JSONObject action, final JSONObject data, final JSONObject event, final Context context){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            try {
                JSONObject options = new JSONObject();
                if (action.has("options")) {
                    options = action.getJSONObject("options");
                    final String appId = options.get("APP_ID").toString();
                    final String Redirect = options.get("REDIRECT_URL").toString();

                    String APP_ID = appId;
                    String RAW_REDIRECT_URI = Redirect;
                    String REDIRECT_URI ="";
                    try {
                        REDIRECT_URI = URLEncoder.encode(RAW_REDIRECT_URI, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    // get the code or token
                    String TOKEN_REQUEST = "https://www.facebook.com/dialog/oauth?client_id="+ APP_ID +"&redirect_uri=" + RAW_REDIRECT_URI + "&display=popup&scope=email&response_type=token";
                    final Dialog custon_dialog = new Dialog(context);
                    custon_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    WebView wv = new WebView(context);
                    wv.loadUrl(TOKEN_REQUEST);
                    wv.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            // You are connected to facebook
                            if(url.contains("access_token")) {
                                custon_dialog.dismiss();
                                JasonHelper.next("success", action, true, event, context);
                            }
                            else {
                                JasonHelper.next("error", action, false, event, context);
                            }
                            return true;
                        }
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            if(url.contains("access_token")) {
                                custon_dialog.dismiss();
                                JasonHelper.next("success", action, true, event, context);
                            }
                            else {
                                JasonHelper.next("error", action, false, event, context);
                            }
                        }
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            System.out.println(""+url);
                        }
                    });

                    final EditText input = new EditText(context);
                    input.setBackgroundColor(Color.BLUE);
                    RelativeLayout layout = new RelativeLayout(context);
                    layout.addView(input); // Notice this is an add method
                    layout.addView(wv); // Notice this is an add method
                    custon_dialog.setContentView(layout);
                    custon_dialog.show();
                }
            }
            catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
            }
        });
    }
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

                    String title = "";
                    String description = "";

                    if (options.has("title")) {
                        title = options.get("title").toString();
                    }

                    if (options.has("description")) {
                        description = options.get("description").toString();
                    }

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

class MyJavaScriptInterface {
    @JavascriptInterface
    public void onUrlChange(String url) {
        Log.d("urlChanged", "onUrlChange" + url);
    }
}