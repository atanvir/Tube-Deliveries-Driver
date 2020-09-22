package com.TubeDeliveriesDriverModule.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.Model.ErrorResponseModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.ResponseBody;

public class CommonUtils extends AppCompatActivity {
    public static CustomLoader customLoader;
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String MobilePattern = "[0-9]{10}";

    public static boolean networkConnectionCheck(final Context context) {
        boolean isConnected = isOnline(context);
        return isConnected;
    }


    public static long milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    public static String getMyPrettyDate(long neededTimeMilis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(neededTimeMilis);

        if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {

            if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {

                if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                    //here return like "Tomorrow at 12:00"
                    return "Tomorrow at " + DateFormat.format("HH:mm", neededTime);

                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    //here return like "Today at 12:00"
                    return "Today at " + DateFormat.format("HH:mm", neededTime);

                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    //here return like "Yesterday at 12:00"
                    return "Yesterday at " + DateFormat.format("HH:mm", neededTime);

                } else {
                    //here return like "May 31, 12:00"
                    return DateFormat.format("MMMM d, HH:mm", neededTime).toString();
                }

            } else {
                //here return like "May 31, 12:00"
                return DateFormat.format("MMMM d, HH:mm", neededTime).toString();
            }

        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            return DateFormat.format("MMMM dd yyyy, HH:mm", neededTime).toString();
        }
    }
    public static void setToolbar(final AppCompatActivity activity,  String  TitleContent){
        Toolbar toolbar= activity.findViewById(R.id.mainToolbar);
        TextView title=activity.findViewById(R.id.tvTitle);
        ImageView menuIv=activity.findViewById(R.id.menuIv);
        ToggleButton toggleButton=activity.findViewById(R.id.toggleButton);

        activity.setSupportActionBar(toolbar);

        if(activity instanceof MainActivity)
        {
            menuIv.setVisibility(View.VISIBLE);
            toggleButton.setVisibility(View.VISIBLE);

        }else
        {
            toolbar.setNavigationIcon(R.drawable.back_button);
            menuIv.setVisibility(View.GONE);
        }
        title.setText(TitleContent);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }


    public static boolean isOnline(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting() || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }



    public static void showSnackBar(Context context,String msg)
    {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setMinimumHeight(10);
        snackBarView.setBackgroundColor(Color.parseColor("#FF0000"));
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(13);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }


    public static void showLoadingDialog(Activity activity){
        if(customLoader ==null)
            customLoader = CustomLoader.show(activity, true);

        try {
            customLoader.setCancelable(false);
            customLoader.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void dismissLoadingDialog(){
        try
        {
            if (null != customLoader && customLoader.isShowing()) {
                customLoader.dismiss();
                customLoader =null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setSpinner(Context context,List<String> data, Spinner spinner) {
        ArrayAdapter genderArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, data) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderArrayAdapter);
        spinner.performClick();
    }

    public static Uri getImageFileUri(Context context) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/TubeDeliveries/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String path = capture_dir + System.currentTimeMillis() + ".jpg";
        return FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.TubeDeliveriesDriverModule" + ".provider", new File(path));
    }

    public static void showToast(Context context, String messgae) {
        Toast toast = Toast.makeText(context, messgae, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getDate(String dt) {
        String time = "";
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd MMMM, yyyy HH:mm:ss aa").format(date);
            time=your_format;
        } catch (Exception e) {
            e.printStackTrace();
        }

            return time;
        }

    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void errorResponse(Context context, ResponseBody errorBody) {
        ErrorResponseModel responseModel=new Gson().fromJson(errorBody.charStream(),ErrorResponseModel.class);
        CommonUtils.showSnackBar(context, responseModel.getMessage());
    }

    public static void startGoogleMap(Context context, double lat, double longt) {
      Uri.Builder builder = new Uri.Builder();
      builder.scheme("https").authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("")
             .appendQueryParameter("api", "1").appendQueryParameter("destination", lat + "," + longt);
      String url = builder.build().toString();
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      context.startActivity(i);
    }

    public static Spannable getColoredString(String mString, int colorId) {
        Spannable spannable = new SpannableString(mString);
        spannable.setSpan(new ForegroundColorSpan(colorId), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.d("TAG",spannable.toString());
        return spannable;
    }


    public  void getFBKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.TubeDeliveriesDriverModule", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }




    public static boolean getDeviceToken(final Context context)
    {
        final boolean[] ret = {true};
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    ret[0]=true;
                    String auth_token = task.getResult().getToken();
                    Log.e("TOKEN",auth_token);
                    SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN,auth_token);

                }else
                {
                    ret[0] =false;
                    getDeviceToken(context);
                }

            }
        });

        return ret[0];

    }





    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                new SimpleDateFormat("ddmmyyhhmmss").format(Calendar.getInstance().getTime()), null);
        return Uri.parse(path);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 8;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public   Intent getPickIntent(Context context,Uri cameraOutputUri) {
        final List<Intent> intents = new ArrayList<Intent>();

        if (true) {
            intents.add(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }

        if (true) {
            setCameraIntents(context,intents, cameraOutputUri);
        }

        if (intents.isEmpty()) return null;
        Intent result = Intent.createChooser(intents.remove(0), null);
        if (!intents.isEmpty()) {
            result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[] {}));
        }
        return result;


    }

    public  void setCameraIntents(Context context,List<Intent> cameraIntents, Uri output) {
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            intent.putExtra("uri",output);
            cameraIntents.add(intent);
        }
    }


    public static String getTimeAgo(String dt) {
        String time = "";
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date endDate = dateFormat.parse(your_format);
            Date startDate = Calendar.getInstance().getTime();
            long differenceDate = startDate.getTime() - endDate.getTime();
            String[] completeDate = splitted[0].split("-");
            String date1 = completeDate[0];
            String month = completeDate[1];
            String year = completeDate[2];
            int days_in_months = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date1)).getActualMaximum(Calendar.DAY_OF_MONTH);


            long secounds = 1000;    // 1 secound
            long min = 60 * secounds;  // 1 min
            long hour = 3600000;      // 1 hour
            long day = 86400000;      // 1 days


            long monthdifference = differenceDate / (days_in_months * day);
            long daysDifference = differenceDate / day;
            long hourdifference = differenceDate / hour;
            long mindifference = differenceDate / min;
            long secoundsDiffer = differenceDate / secounds;

            if (monthdifference > 0) time =  monthdifference==1?""+monthdifference+" month ago":monthdifference+ " months ago";
            else if (daysDifference > 0) time = daysDifference==1?""+daysDifference+" day ago":daysDifference+ " days ago";
            else if (hourdifference > 0) time = hourdifference==1?""+hourdifference+" hour ago":hourdifference+ " hours ago";
            else if (mindifference > 0) time = mindifference==1?""+mindifference+" min ago":mindifference+ " mins ago";
            else if (secoundsDiffer > 0) time = secoundsDiffer + " secs ago";
            else time="now";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
    public static long getTime(String dt) {
        long time = 0;
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            Date endDate = dateFormat.parse(your_format);
            Date startDate = Calendar.getInstance().getTime();
            long differenceDate = startDate.getTime() - endDate.getTime();
            long secounds = 1000;    // 1 secound
            long secoundsDiffer = differenceDate / secounds;
            if (secoundsDiffer > 0) time = secoundsDiffer;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static void startActivity(Context context,Class<? extends Object> className) {
        Intent intent = new Intent(context, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static String roundOff(String originalValue){
        double tvTotalPrice = Double.valueOf(originalValue);
        double sum1 = (int) Math.round(tvTotalPrice * 100) / (double) 100;
        double finalAmount = 0.00;
        return String.valueOf(sum1);
    }

}
