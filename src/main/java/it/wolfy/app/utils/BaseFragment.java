package it.wolfy.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import it.wolfy.app.R;

/**
 * Created by User on 25/02/2015.
 */
public abstract class BaseFragment extends Fragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected SharedPreferences settings;
    protected SharedPreferences.Editor Editor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences(Costanti.SHAREDPREF, 0);
        Editor = settings.edit();
        // Utils.UpdateLanguage(getActivity(), Utils.getLingua(getActivity()));
        //  IDUtente = (settings.getInt(TAGIDUTENTE, -1));
        //  TipoUtente = settings.getString(TAGTIPOUTENTE, null);
        //  ApiKey = settings.getString(TAGAPI, "");
    }

    protected void SwitchTo(int resFragment, Fragment frag, String Tag)
    {
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(resFragment, frag, Tag)
                .commit();
    }

    public void HideSoftKeyboard()
    {
        if ( getActivity().getCurrentFocus() != null )
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    //protected abstract String ScreenAnalitycs();

    public void ShowMessage(int msg, boolean durataShort)
    {
        try
        {
            Toast.makeText(getActivity(), getResources().getString(msg), durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
        }
    }

    public void ShowMessage(int msg, String other, boolean durataShort)
    {
        try
        {
            Toast.makeText(getActivity(), getResources().getString(msg) + "_" + other, durataShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
        }
    }

    public void ShowSnackMessage(int msg, CoordinatorLayout layout, int coloreSfondo, int coloreTesto)
    {
        Snackbar snackbar = Snackbar.make(layout, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(coloreSfondo);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(coloreTesto);
        snackbar.show();
    }

    public void ShowSnackMessage(int msg, CoordinatorLayout layout, int coloreTesto)
    {
        Snackbar snackbar = Snackbar.make(layout, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(UtilsColor.GetColor(getActivity(), coloreTesto));
        snackbar.show();
    }

    public void LogMsg(String s)
    {
        // if ( DEBUGDEV )
        // Log.w(TAG, s);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
    }
}


