package it.wolfy.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.wolfy.app.R;

public abstract class BaseActivity extends AppCompatActivity
{
    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
//    protected RelativeLayout main;
//    protected LinearLayout drawer;
//    protected SharedPreferences settings;
//    protected SharedPreferences.Editor Editor;
//    protected int numMsg;
//    protected int numNotifiche;
//    protected int numMsgGruppo;
//    protected String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "HealthAroundMe/";
//    ImageView imgMenu1;
//    ImageView imgMenu2;
//    ImageView imgMenu3;
//    ImageView imgMenu4;
//    protected TextView txtNumMsg;
//    protected FrameLayout containerMsg;
//    protected TextView txtNumNot;
//    protected FrameLayout containerNot;
//    protected TextView txtNumGruppi;
//    protected FrameLayout containerGruppi;
    //public View toolbarBottom;
    /*protected int IDUtente;
    protected String TipoUtente;
    protected String ApiKey;
    protected String Pk;
    protected String Lingua;*/
    protected Context cx;
    private BroadcastReceiver receiverBroadcast;

    /* private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
     {
         @Override
         public void onReceive(Context context, Intent intent)
         {
             SetCountNotificaToolbar(intent.getStringExtra("TIPO"), intent.getIntExtra("NUM", -1),true);
         }
     };*/
//    @Override
//    protected void attachBaseContext(Context newBase)
//    {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    public void HideSoftKeyboard()
    {
        if ( getCurrentFocus() != null )
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(LayoutResourceID());
        ButterKnife.bind(this);
        cx = this;
        if ( toolbar == null ) toolbar = (Toolbar) findViewById(R.id.toolbar);
        if ( toolbar != null )
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            //getSupportActionBar().setIcon(R.drawable.logo_new);
        }
        // toolbarBottom = findViewById(R.id.toolbar_bottom);

        receiverBroadcast = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if ( intent.hasExtra("TIPO") )
                {
                    // SetCountNotificaToolbar(intent.getStringExtra("TIPO"), intent.getIntExtra("NUM", -1), true);
                }
            }
        };
        if ( BuildConfig.DEBUG )
        {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                           .detectAll()
                                           .penaltyLog()
                                           .build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                               .detectAll()
                                               .penaltyLog()
                                               .penaltyDeathOnNetwork()
                                               .build());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiverBroadcast, new IntentFilter(Costanti.NOTIFICABROADCAST));
        //TODO check all'avvio dei vari contatori dei non letti nei messaggi,gruppi ecc

    }

    @Override
    protected void onPause()
    {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverBroadcast);
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        // Unregister since the activity is about to be closed.
        super.onDestroy();
    }

    public void SetColoreToolbar(int colore)
    {
        //toolbar.setBackgroundColor(getResources().getColor(colore));
        toolbar.setBackgroundColor(ContextCompat.getColor(cx, colore));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
        //menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    protected abstract int LayoutResourceID();

    //protected abstract String ScreenAnalitycs();

    protected void SwitchTo(int resFragment, Fragment frag, String Tag)
    {
        HideSoftKeyboard();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .addToBackStack(null)
                .replace(resFragment, frag, Tag).commit();
    }

    public void ShowMessage(int msg, boolean durataShort)
    {
        Toast.makeText(this, getResources().getString(msg), durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public void ShowMessage(int msg, String other, boolean durataShort)
    {
        Toast.makeText(this, getResources().getString(msg) + "_" + other, durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public void ShowSnackMessage(int msg, CoordinatorLayout layout)
    {
        Snackbar.make(layout, msg, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void LogMsg(String s)
    {
        //if ( DEBUGDEV )
        //Log.w(TAG, s);
    }
}