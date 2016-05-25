package it.wolfy.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import it.wolfy.app.R;
import it.wolfy.app.interfaces.DBHelper;
import it.wolfy.app.model.Misurazione;
import it.wolfy.app.model.Persona;
import it.wolfy.app.utils.BaseActivity;

public class MainActivity extends BaseActivity
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.menu)
    FloatingActionMenu menu;
    Drawer drawer;
    Persona personaAttiva = null;
    @BindView(R.id.fab_peso)
    FloatingActionButton fabPeso;
    @BindView(R.id.fab_altezza)
    FloatingActionButton fabAltezza;
    @BindView(R.id.coordMain)
    CoordinatorLayout coordMain;

    protected DBHelper dbHelper;
    Context cx;
    @BindView(R.id.relAltezza)
    RelativeLayout relAltezza;
    @BindView(R.id.relPeso)
    RelativeLayout relPeso;
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.txtValBMI)
    TextView txtValBMI;
    @BindView(R.id.txtClasseBMI)
    TextView txtClasseBMI;

    @Override
    protected int LayoutResourceID()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        cx = this;

        dbHelper = new DBHelper(this);
        dbHelper.getRealm().beginTransaction();
        personaAttiva = dbHelper.getRealm().where(Persona.class).equalTo("id", 1).findFirst();
        if ( personaAttiva == null )
        {
            personaAttiva = dbHelper.getRealm().createObject(Persona.class);
            personaAttiva.id = 1;
            personaAttiva.dataNascita = "02/08/1990";
            personaAttiva.nome = "AAAAA";
        }
        dbHelper.getRealm().commitTransaction();
        dbHelper.getRealm().close();
        if ( personaAttiva != null )
        {
            initDrawer();
        }
        fabPeso.setOnClickListener(view -> {
            menu.toggle(true);
            if ( personaAttiva != null )
            {
                CreaMisurazione("Peso");
            }
            else
            {
                //UtilsViewApp.SetupSnackbar(Snackbar.make(coordMain, "Null", Snackbar.LENGTH_SHORT)).show();
            }
        });
        fabAltezza.setOnClickListener(view -> {
            menu.toggle(true);
            if ( personaAttiva != null )
            {
                CreaMisurazione("Altezza");
            }
            else
            {
                //UtilsView.SetupSnackbar(Snackbar.make(coordMain, "Null", Snackbar.LENGTH_SHORT)).show();
            }
        });
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        chart.setTouchEnabled(true);
//        chart.getAxisLeft().setAxisMaxValue(150f);
//        chart.getAxisLeft().setAxisMinValue(0f);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        setData();
        updateBMI();
    }

    void updateBMI()
    {
        txtValBMI.setText(GetBMI() + "");
        txtClasseBMI.setText(GetValutazioneBMI(GetBMI()));
    }

    double GetBMI()
    {
        float lastPeso = 0;
        float lastAltezza = 0;
        for (int i = personaAttiva.misurazioni.size() - 1; i >= 0; i--)
        {
            Misurazione tmp = personaAttiva.misurazioni.get(i);
            if ( tmp.tipo.equals("Peso") )
            {
                if ( lastPeso == 0 ) lastPeso = Float.valueOf(tmp.misurazione);
            }
            else
            {
                if ( lastAltezza == 0 ) lastAltezza = Float.valueOf(tmp.misurazione);
            }
            if ( lastPeso != 0 && lastAltezza != 0 )
                break;
        }

        double bmi = (1.3 * lastPeso) / Math.pow(lastAltezza / 100, 2.5);
        return (double) Math.round(bmi * 100) / 100;
    }

    String GetValutazioneBMI(double bmi)
    {
        if ( bmi < 16 ) return "Grave magrezza";
        if ( bmi >= 16 && bmi < 18.49 ) return "Sottopeso";
        if ( bmi >= 18.5 && bmi < 24.99 ) return "Normopeso";
        if ( bmi >= 25 && bmi < 29.99 ) return "Sovrappeso";
        if ( bmi >= 30 && bmi < 34.99 ) return "Obesità classe 1 (lieve)";
        if ( bmi >= 35 && bmi < 39.99 ) return "Obesità classe 2 (media)";
        if ( bmi >= 40 ) return "Obesità classe 3 (grave)";
        return "";
    }

    private void CreaMisurazione(String tipo)
    {
        String[] misurazioneInserita = new String[1];
        new MaterialDialog.Builder(this)
                .title("Nuova misurazione")
                .content("Aggiungi misurazione di " + tipo)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER)
                .input("hint", "", (dialog, input) -> {
                    misurazioneInserita[0] = input.toString();
                })
                .positiveText("Ok")
                .onPositive((dialog, which) -> {
                    dbHelper.getRealm().beginTransaction();
                    int count = (int) (dbHelper.getRealm().where(Misurazione.class).count() + 1);
                    Misurazione misurazione = dbHelper.getRealm().createObject(Misurazione.class);
                    misurazione.misurazione = (dialog.getInputEditText().getText().toString());
                    misurazione.tipo = tipo;
                    misurazione.id = count;
                    personaAttiva.misurazioni.add(misurazione);
                    dbHelper.getRealm().commitTransaction();
                    dbHelper.getRealm().close();
                    setData();
                    updateBMI();
                })
                .show();
    }

    private void setData()
    {
        dbHelper.getRealm().beginTransaction();
        RealmResults<Misurazione> result = dbHelper.getRealm().where(Misurazione.class).findAll();
        dbHelper.getRealm().commitTransaction();
        dbHelper.getRealm().close();
        try
        {
            ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
            ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();
            int i = 0;
            for (Misurazione misurazione : personaAttiva.misurazioni)
            {
                if ( misurazione.tipo.equals("Peso") )
                    valsComp1.add(new Entry(Float.valueOf(misurazione.misurazione), i));
                else
                    valsComp2.add(new Entry(Float.valueOf(misurazione.misurazione), i));
                xVals.add(i + "");
                i++;
            }
            LineDataSet setComp1 = new LineDataSet(valsComp1, "Peso");
            setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
            LineDataSet setComp2 = new LineDataSet(valsComp2, "Altezza");
            setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
//            RealmLineDataSet<Misurazione> set = new RealmLineDataSet<Misurazione>(result, "misurazione", "id");
            setComp1.setDrawCubic(false);
//            setComp1.setLabel("Realm LineDataSet");
            setComp1.setDrawCircleHole(false);
            setComp1.setColor(ColorTemplate.rgb("#FF5722"));
            setComp1.setCircleColor(ColorTemplate.rgb("#FF5722"));
            setComp1.setLineWidth(1.8f);
            setComp1.setCircleSize(3.6f);

            setComp2.setDrawCubic(false);
//            setComp2.setLabel("Realm LineDataSet");
            setComp2.setDrawCircleHole(false);
            setComp2.setColor(ColorTemplate.rgb("#0F5FF2"));
            setComp2.setCircleColor(ColorTemplate.rgb("#FF5722"));
            setComp2.setLineWidth(1.8f);
            setComp2.setCircleSize(3.6f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setComp1);
            dataSets.add(setComp2);
            LineData data = new LineData(xVals, dataSets);
            // create a data object with the dataset list
//            RealmLineData data = new RealmLineData(result, "xValue", dataSets);
//        styleData(data);

            // set data
            chart.setData(data);
            chart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
            chart.invalidate();
        }
        catch (Exception ex)
        {
        }
    }

    void initDrawer()
    {
        ArrayList<IProfile> listaProfili = new ArrayList<>();
        dbHelper.getRealm().beginTransaction();
        RealmResults<Persona> listaPersone = dbHelper.getRealm().where(Persona.class).findAll();
        dbHelper.getRealm().commitTransaction();

        for (Persona tmp : listaPersone)
        {
            listaProfili.add(new ProfileDrawerItem()
                                     .withName(tmp.nome)
                                     .withEmail(tmp.dataNascita));
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.accent)//.withHeaderBackground(R.drawable.sfera)
                .withOnAccountHeaderListener((view, profile, currentProfile) -> {
                    Log.e("TAG", "click: " + profile.getName());
                    dbHelper.getRealm().beginTransaction();
                    personaAttiva = dbHelper.getRealm().where(Persona.class).equalTo("nome", profile.getName().getText()).equalTo("dataNascita", profile.getEmail().getText()).findFirst();
                    dbHelper.getRealm().commitTransaction();
                    return false;
                })
                .build();
        headerResult.setProfiles(listaProfili);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("A");
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("B");
        drawer = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName("lol")
                               )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.e("TAG", "click: " + position);
                    return true;
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu., menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings )
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dbHelper.getRealm().close();
    }

}
