package it.wolfy.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils
{

    SharedPreferences settings;
    public static volatile Location miaPos;
    public static volatile Address miaPosAddress;


    public static int dpToPx(Context context, float dp)
    {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    public static boolean CheckCodiceFiscale(String cf)
    {
        int i, s, c;
        String cf2;
        int setdisp[] = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20,
                11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23};
        if ( cf.length() == 0 ) return false;
        if ( cf.length() != 16 )
            return false;// "La lunghezza del codice fiscale non &egrave;\n" + "corretta: il codice fiscale dovrebbe essere lungo\n"+ "esattamente 16 caratteri.";
        cf2 = cf.toUpperCase();
        for (i = 0; i < 16; i++)
        {
            c = cf2.charAt(i);
            if ( !(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z') )
                return false;//"Il codice fiscale contiene dei caratteri non validi:\n" + "i soli caratteri validi sono le lettere e le cifre.";
        }
        s = 0;
        for (i = 1; i <= 13; i += 2)
        {
            c = cf2.charAt(i);
            if ( c >= '0' && c <= '9' )
                s = s + c - '0';
            else
                s = s + c - 'A';
        }
        for (i = 0; i <= 14; i += 2)
        {
            c = cf2.charAt(i);
            if ( c >= '0' && c <= '9' ) c = c - '0' + 'A';
            s = s + setdisp[c - 'A'];
        }
        if ( s % 26 + 'A' != cf2.charAt(15) )
            return false;//"Il codice fiscale non &egrave; corretto:\n" + "il codice di controllo non corrisponde.";
        return true;

    }

    public static void SetupFAB(FloatingActionButton button)
    {
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP )
        {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) button.getLayoutParams();
            p.setMargins(0, 0, -8, -24);
            button.setLayoutParams(p);
        }
    }

   /* public static String GetHashParam(String[] arr)
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < arr.length; i++)
        {
            result.append(arr[i]);
            result.append("&");
        }
        try
        {
            return Crypto.SHA256(result.toString());
        }
        catch (SignatureException e)
        {
            return "";
        }
    }*/

    public static boolean CheckInternet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean StringIsNullOrEmpty(String tmp)
    {
        return tmp == null || tmp.length() == 0;
    }

    public static String StringCapitalize(String str)
    {
        int strLen;
        if ( str == null || (strLen = str.length()) == 0 )
        {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }

    public static Date GetData(int day, int month, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date GetDataByString(String data)
    {
        if ( Utils.StringIsNullOrEmpty(data) )
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try
        {
            return sdf.parse(data);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static Date GetDataByString(String data, String formato)
    {
        if ( Utils.StringIsNullOrEmpty(data) )
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        try
        {
            return sdf.parse(data);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

 /*---------------------------------------- DATETIME JODA ----------------------------------------*/

  /*  public static String GetStringByDataJoda(Date data, String formato)
    {
        if ( data == null )
            return "";
        DateTime dt = new DateTime(data);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formato);
        return fmt.print(dt);
    }

    public static String GetStringByDataJoda(Date data)
    {
        if ( data == null )
            return "";
        DateTime dt = new DateTime(data);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        return fmt.print(dt);
    }

    public static String GetStringByDataJoda(DateTime data)
    {
        if ( data == null )
            return "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");//formato in cui verrà visualizzato
        return fmt.print(data);
    }

    public static String GetStringByDataJoda(DateTime data, String formato)
    {
        if ( data == null )
            return "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formato);//formato in cui verrà visualizzato
        return fmt.print(data);
    }

    public static DateTime GetDataByStringJoda(String data, String formato)
    {
        if ( Utils.StringIsNullOrEmpty(data) )
            return null;
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formato);
        return fmt.parseDateTime(data);
    }

    public static DateTime GetDataByStringJoda(String data)
    {
        if ( Utils.StringIsNullOrEmpty(data) )
            return null;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        return fmt.parseDateTime(data);
    }*/

    /*---------------------------------------- END DATETIME JODA ----------------------------------------*/
    public static void UpdateLanguage(Context context, String code)
    {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static byte[] BitmapToByteArray(Bitmap bmp)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private static SharedPreferences getSettings(Context cx)
    {
        return cx.getSharedPreferences(Costanti.SHAREDPREF, 0);
    }

    /*  public static String getApi(Context cx)
      {
          if ( Costanti.DEBUGDEV )
              return "12345";
          return "12345";
          // return getSettings(cx).getString(Costanti.TAGAPI, null);
      }

      public static void setApi(String api, Context cx)
      {
          if ( Costanti.DEBUGDEV )
              getSettings(cx).edit().putString(Costanti.TAGAPI, "12345").apply();
          else
              getSettings(cx).edit().putString(Costanti.TAGAPI, api).apply();

      }

      public static String getPk(Context cx)
      {
          return getSettings(cx).getString(Costanti.TAGPK, null);
      }

      public static void setPk(String pk, Context cx)
      {
          if ( Costanti.DEBUGDEV )
              getSettings(cx).edit().putString(Costanti.TAGPK, "12345").apply();
          else
              getSettings(cx).edit().putString(Costanti.TAGPK, pk).apply();
      }

      public static void setPsw(String psw, Context cx)
      {
          getSettings(cx).edit().putString(Costanti.TAGPSW, psw).apply();
      }

      public static String getPsw(Context cx)
      {
          return getSettings(cx).getString(Costanti.TAGPSW, null);
      }

      public static int getIDUtente(Context cx)
      {
          return getSettings(cx).getInt(Costanti.TAGIDUTENTE, -1);
      }

      public static void setIDUtente(int id_user, Context cx)
      {
          getSettings(cx).edit().putInt(Costanti.TAGIDUTENTE, id_user).apply();
      }

      public static void setTipoUtente(String tipoUtente, Context cx)
      {
          getSettings(cx).edit().putString(Costanti.TAGTIPOUTENTE, tipoUtente).apply();
      }

      public static String getTipoUtente(Context cx)
      {
          return getSettings(cx).getString(Costanti.TAGTIPOUTENTE, null);
      }

      public static String getToken(Context cx)
      {
          return getSettings(cx).getString(Costanti.TAGTOKEN, null);
      }

      public static void setToken(Context cx, String token)
      {
          getSettings(cx).edit().putString(Costanti.TAGTOKEN, token).commit();
      }

      public static String getLingua(Context cx)
      {
          return getSettings(cx).getString(Costanti.TAGLINGUA, "it");
      }

      public static void setLingua(String lingua, Context cx)
      {
          getSettings(cx).edit().putString(Costanti.TAGLINGUA, lingua).apply();
          Utils.UpdateLanguage(cx, lingua);
      }

      public static int getRegCompleta(Context cx)
      {
          return getSettings(cx).getInt(Costanti.TAGREGCOMPLETA, -1);
      }

      public static void setRegCompleta(int reg, Context cx)
      {
          getSettings(cx).edit().putInt(Costanti.TAGREGCOMPLETA, reg).apply();
      }

      public static void LogMsg(String s)
      {
          if ( DEBUGDEV )
              Log.w(TAG, s);
      }
  */
    public static void ShowMessage(int msg, boolean durataShort, Context cx)
    {
        Toast.makeText(cx, cx.getResources().getString(msg), durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowMessage(int msg, String other, boolean durataShort, Context cx)
    {
        Toast.makeText(cx, cx.getResources().getString(msg) + "_" + other, durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public static void ShowSnackMessage(int msg, CoordinatorLayout layout)
    {
        Snackbar.make(layout, msg, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void ChangeTabsFont(Context cx, TabLayout tabsMaterial)
    {

        ViewGroup vg = (ViewGroup) tabsMaterial.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++)
        {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++)
            {
                View tabViewChild = vgTab.getChildAt(i);
                if ( tabViewChild instanceof TextView )
                {
                    ((TextView) tabViewChild).setTypeface(FontCache.GetFont(cx, "OSRegular.ttf"), Typeface.NORMAL);
                }
            }
        }
    }

    public static String GetAbsoluteUrl(String relativeUrl)
    {
        return "";//Costanti.SERVER + relativeUrl;
    }

    public static String ParseTipoUtente(String tipo)
    {
        switch (tipo)
        {
            case "U":
                return "0";
            case "M":
                return "1";
            case "S":
                return "2";
        }
        return "0";
    }

    public static void ParseResponseError(Context cx, String tipo)
    {
       /* switch (tipo)
        {
            case "-1":
                ShowMessage(R.string.OpFail, true, cx);
                break;
            case "-2":
                ShowMessage(R.string.OpFail, true, cx);
                break;
            case "-3":
                ShowMessage(R.string.OpFail, true, cx);
                break;
            case "-4":
                ShowMessage(R.string.OpFailMail, false, cx);
                break;
            case "-5":
                ShowMessage(R.string.OpFailProfiloIncompleto, false, cx);
                break;

        }*/
    }

    public static void SetColorAlertDialog(AlertDialog dialog, int color)
    {
        try
        {
            Resources resources = dialog.getContext().getResources();

            //int color = UtilsColor.GetColor(dialog.getContext(), R.color.colore_msg_testi); // your color here

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(color); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void SetFontNormale(Context cx, TextView item)
    {
        //FONT
/*        Typeface normale = FontCache.GetFont(cx, "OSRegular.ttf");
        item.setTypeface(normale, Typeface.NORMAL);
        item.setIncludeFontPadding(false);*/
    }

    public static void SetFontNormale(Context cx, Button item)
    {
        //FONT
        Typeface normale = FontCache.GetFont(cx, "OSRegular.ttf");
        item.setTypeface(normale, Typeface.NORMAL);
        item.setIncludeFontPadding(false);
    }

    public static void SetFontLight(Context cx, TextView item)
    {
        Typeface normale = FontCache.GetFont(cx, "OSLight.ttf");
        item.setTypeface(normale, Typeface.NORMAL);
        item.setIncludeFontPadding(false);
    }

    public static int dp(float value)
    {
        return (int) Math.ceil(1 * value);
    }
}
