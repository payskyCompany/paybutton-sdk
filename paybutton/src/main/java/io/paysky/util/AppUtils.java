package io.paysky.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.example.paybutton.R;

import org.greenrobot.eventbus.EventBus;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paysky-202 on 5/10/2018.
 */

public class AppUtils {

    public static void register(Object o) {
        EventBus.getDefault().register(o);
    }

    public static void unregister(Object o) {
        EventBus.getDefault().unregister(o);
    }

    public static boolean isRegister(Object o) {
        return EventBus.getDefault().isRegistered(o);
    }

    public static void sendEvent(Object o) {
        EventBus.getDefault().post(o);
    }


    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return (connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null)
                != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static ProgressDialog createProgressDialog(Context context, @StringRes int text) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(text));
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, String text) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(text);
        return progressDialog;
    }


    public static ProgressDialog createProgressDialog(Context context) {
        return createProgressDialog(context, R.string.please_wait);
    }

    public static String getDateTimeLocalTrxn() {

        int arg1 = Calendar.getInstance().get(Calendar.YEAR);

        String arg1s = "" + arg1;
        arg1s = arg1s.substring(2, 4);

        int arg2 = Calendar.getInstance().get(Calendar.MONTH);

        int arg3 = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int arg4 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        int arg5 = Calendar.getInstance().get(Calendar.MINUTE);

        int arg6 = Calendar.getInstance().get(Calendar.SECOND);

        int arg7 = Calendar.getInstance().get(Calendar.MILLISECOND);
        arg2 = arg2 + 1;

        return "" + new StringBuilder().append(arg1s).append("")

                .append((arg2 < 10 ? "0" + arg2 : arg2)).append("").append((arg3 < 10 ? "0" + arg3 : arg3))

                .append((arg4 < 10 ? "0" + arg4 : arg4)).append((arg5 < 10 ? "0" + arg5 : arg5))

                .append((arg6 < 10 ? "0" + arg6 : arg6)).append("" + arg7);
    }

    public static String generateRandomNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        while (index < 10) {
            stringBuilder.append(1 + (int) (Math.random() * 49));
            index++;
        }

        return stringBuilder.toString();
    }


    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    public static boolean validEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validPhone(String phone) {
        return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static void openActivity(Context context, Class<? extends Activity> activity, Bundle bundle, boolean clearStack) {
        Intent i = new Intent(context, activity);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        if (clearStack) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(i);
        ((Activity) context).finish();
    }

    public static String convertToEnglishDigits(String value) {
        return value.replaceAll("٠", "0").replaceAll("١", "1")
                .replaceAll("٢", "2").replaceAll("٣", "3")
                .replaceAll("٤", "4").replaceAll("٥", "5")
                .replaceAll("٦", "6").replaceAll("٧", "7")
                .replaceAll("٨", "8").replaceAll("٩", "9");    }

    public static boolean isPaymentMachine() {
        String model = Build.MODEL;
        boolean isSq27 = model.equals("SQ27");
        boolean isI900s = model.equals("i9000S");
        return isSq27 || isI900s;
    }

    public static int formatPaymentAmountToServer(String payAmount) {
        return (int) (Double.valueOf(payAmount) * 100);
    }

    public static String getVersionNumber(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void applyFont(String font, TextView... views) {
        Typeface typeface = Typeface.createFromAsset(views[0].getContext().getAssets(), font);
        for (TextView textView : views) {
            textView.setTypeface(typeface);
        }
    }

    public static void showHtmlText(TextView textView, @StringRes int text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(textView.getContext().getString(text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(textView.getContext().getString(text)));
        }
    }

    public static void showHtmlText(TextView textView, String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    public static void playRingtone(Activity activity) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(activity, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(Double.parseDouble(amount));
    }


    public static void preventScreenshot(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }


}
