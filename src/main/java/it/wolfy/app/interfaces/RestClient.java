package it.wolfy.app.interfaces;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import it.wolfy.app.utils.Costanti;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient
{

    private static CackleInterface cackleInterface;

    public static CackleInterface getClient()
    {
        if ( cackleInterface == null )
        {
            ConfigClient();
            Gson gson = new GsonBuilder().setExclusionStrategies(new  ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Costanti.SERVER)
                    .client(okClient)
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            cackleInterface = client.create(CackleInterface.class);
        }
        return cackleInterface;
    }


    public interface CackleInterface
    {

//        @POST("conversazioni")
//        Call<List<Chat>> getConversazioni();
//        @FormUrlEncoded
//        @POST("utente/create")
//        Call<ServerResponse> createUtente(
//                @Field("api") String api,
//                @Field("nome") String nome,
//                @Field("cognome") String cognome,
//                @Field("cf") String cf,
//                @Field("email") String email,
//                @Field("psw") String psw,
//                @Field("data") String data,
//                @Field("luogo") String luogo,
//                @Field("telefono") String telefono);
//
//        @FormUrlEncoded
//        @POST("utente/login")
//        Call<Utente> loginUtente(
//                @Field("api") String api,
//                @Field("email") String email,
//                @Field("password") String psw);
//
//        @FormUrlEncoded
//        @POST("utente/updateNotificationToken")
//        Call<ServerResponse> updateDeviceToken(
//                @Field("api") String api,
//                @Field("id_user") String iduser,
//                @Field("deviceToken") String devToken,
//                @Field("piattaforma") String piattaforma);
//
//        @FormUrlEncoded
//        @POST("capsula/lista")
//        Call<List<Capsula>> listaCapsule(
//                @Field("api") String api,
//                @Field("iddestinatario") int iddestinatario);
//
//        @FormUrlEncoded
//        @POST("capsula/getCapsula")
//        Call<Capsula> getCapsula(
//                @Field("id") int idcapsula);
//
//        @FormUrlEncoded
//        @POST("capsula/getCapsuleNonLette")
//        Call<ServerResponse> getCapsuleNonLette(
//                @Field("iddestinatario") int idutente);
//
//        @FormUrlEncoded
//        @POST("capsula/create")
//        Call<Capsula> createCapsulaMessaggio(
//                @Field("idutente") String idutente,
//                @Field("iddestinatario") String iddest,
//                @Field("data_invio") String data_invio,
//                @Field("tipomessaggio") String tipomessaggio,
//                @Field("tipiAllegati") String tipoAllegato,
//                @Field("urlAllegati") String urlAllegato);
//
//        @FormUrlEncoded
//        @POST("capsula/create")
//        Call<Capsula> createCapsulaAlbum(
//                @Field("idutente") String idutente,
//                @Field("iddestinatario") String iddest,
//                @Field("data_invio") String data_invio,
//                @Field("tipomessaggio") String tipomessaggio,
//                @Field("pagine") String pagine,
//                @Field("tipiAllegati") String tipoAllegato,
//                @Field("urlAllegati") String urlAllegato);
//
//        @FormUrlEncoded
//        @POST("capsula/create")
//        Call<Capsula> createCapsulaPercorso(
//                @Field("idutente") String idutente,
//                @Field("iddestinatario") String iddest,
//                @Field("data_invio") String data_invio,
//                @Field("tipomessaggio") String tipomessaggio,
//                @Field("latitudini") String latitudini,
//                @Field("longitudini") String longitudini,
//                @Field("numAllegati") String numAllegati,
//                @Field("tipiAllegati") String tipoAllegato,
//                @Field("urlAllegati") String urlAllegato);

    }

    private static OkHttpClient okClient;

    static void ConfigClient()
    {
        okClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(chain -> {
//                    Request originalRequest = chain.request();
//
//                    // Add authorization header with updated authorization value to intercepted request
//                    if ( !UtilsString.StringIsNullOrEmpty(UtilsAuth.accessToken) )
//                    {
//                        Request authorisedRequest = originalRequest.newBuilder()
//                                .header("Authorization", UtilsAuth.accessToken)
//                                .build();
//                        return chain.proceed(authorisedRequest);
//                    }
//                    return chain.proceed(originalRequest);
//                })
//                .authenticator((route, response) -> {
//                    // UtilsAuth.accessToken = UtilsHTTP.RefreshToken();
//
//                    // Add new header to rejected request and retry it
//                    if ( response.code() == 401 )
//                    { //if unauthorized
//                        synchronized (okClient)
//                        {
//
//                            UtilsAuth.accessToken = UtilsHTTP.RefreshToken();
//                            UtilsAuth.setToken(UtilsAuth.accessToken, AppEx.getContext());
//                            return response.request().newBuilder()
//                                    .header("Authorization", UtilsAuth.accessToken)
//                                    .build();
//
//                        }
//                    }
//                    return response.request();
//                    //---TEST
//                    /*System.out.println("Authenticating for response: " + response);
//                    System.out.println("Challenges: " + response.challenges());
//                    String credential = Credentials.basic("jesse", "password1");
//                    return response.request().newBuilder()
//                            .header("Authorization", credential)
//                            .build();*/
//                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    public static Gson returnCustomParser(){
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        return gson;
    }
}

