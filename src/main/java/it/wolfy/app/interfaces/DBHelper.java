package it.wolfy.app.interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBHelper
{

    Realm realm;

    public DBHelper(Context cx)
    {
        byte[] key;
        // ClearSettings(this);
        if ( getApi(cx) == null )
        {
            key = new byte[64];
            new SecureRandom().nextBytes(key);

            setApi(Base64.encodeToString(key, Base64.DEFAULT), cx);
            Log.e("tag", Base64.encodeToString(key, Base64.DEFAULT));
//            Log.e("tag", (key == Base64.decode(getApi(this), Base64.DEFAULT)) ? "yeyyyyyyyyp" : "noooooooo");

        }
        key = Base64.decode(getApi(cx), Base64.DEFAULT);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(cx)
                .name("myDb2a.realm")
                .encryptionKey(key)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getInstance(realmConfig);
    }

    public Realm getRealm()
    {
        return Realm.getDefaultInstance();
    }

    public void closeDB()
    {
        this.realm.close();
    }

    public void initDB()
    {
//        getRealm().beginTransaction();
//        Persona a = getRealm().createObject(Persona.class);
//        a.id = 1;
//        a.nome = "A";
//        Persona b = getRealm().createObject(Persona.class);
//        b.id = 2;
//        b.nome = "B";
//        Chat chat = getRealm().createObject(Chat.class);
//        chat.id = 1;
//        chat.partecipanti.add(a);
//        chat.partecipanti.add(b);
//        getRealm().commitTransaction();
    }

    private static SharedPreferences getSettings(Context cx, String sharedPref)
    {
        return cx.getSharedPreferences(sharedPref, 0);
    }

    public static void ClearSettings(Context cx)
    {
        getSettings(cx, "Shared").edit().clear().apply();
    }

    public static String getApi(Context cx)
    {

        return getSettings(cx, "Shared").getString("KEY", null);
        // return getSettings(,Costanti.SHAREDPREF).getString(Costanti.TAGAPI, null);
    }

    public static void setApi(String api, Context cx)
    {
        getSettings(cx, "Shared").edit().putString("KEY", api).apply();

    }
}
