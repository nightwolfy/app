package it.wolfy.app.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class AsyncOkHTTP
{

    private static OkHttpClient client;

    public static OkHttpClient ConfigClient()
    {
        client= new OkHttpClient();
        client.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException
                    {
                        Request originalRequest = chain.request();

                        Request authorisedRequest = originalRequest.newBuilder()
                                .header("Authorization", "")
                                .build();
                        return chain.proceed(authorisedRequest);
                    }
                })
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException
                    {
                        //newAccessToken = service.refreshToken();
                        return response.request().newBuilder()
                                .addHeader("Authorization", "access")
                                .build();
                    }
                });
        return client;
    }
    public static void ConfigClient(OkHttpClient client)
    {
        client.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Request originalRequest = chain.request();

                Request authorisedRequest = originalRequest.newBuilder()
                        .header("Authorization", "")
                        .build();
                return chain.proceed(authorisedRequest);
            }
        })
        .authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException
            {
                //newAccessToken = service.refreshToken();
                return response.request().newBuilder()
                        .addHeader("Authorization", "access")
                        .build();
            }
        });
//        client.setConnectTimeout(10, TimeUnit.SECONDS);
//        client.setWriteTimeout(10, TimeUnit.SECONDS);
//        client.setReadTimeout(30, TimeUnit.SECONDS);
        // per solo debug
        /*
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
                        {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            client.setSslSocketFactory(sslSocketFactory);
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        //int cacheSize = 10 * 1024 * 1024;
        //Cache cache = new Cache(Costanti.SDDOCPATH+, cacheSize);
        //client.setCache(cache);
    }

    public void get(String url, Request request, HttpCallback cb)
    {
        call(request, cb);
    }

    public static void post(Request request, HttpCallback cb)
    {
        call(request, cb);
    }

    private static void call(Request request, final HttpCallback cb)
    {
        client = new OkHttpClient();
        ConfigClient(client);
        /*if(Costanti.DEBUGDEV)
        {
            Log.w(Costanti.TAG, request.urlString());
        }*/
        client.newCall(request).enqueue(new Callback()
        {

            @Override
            public void onFailure(Call call, IOException e)
            {
                cb.onFailure(null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                cb.onSuccess(response);
            }


        });
    }

    public interface HttpCallback
    {

        public void onFailure(Response response, IOException e);

        /**
         * contains the server response
         *
         * @param response
         */
        public void onSuccess(Response response);
    }

}
