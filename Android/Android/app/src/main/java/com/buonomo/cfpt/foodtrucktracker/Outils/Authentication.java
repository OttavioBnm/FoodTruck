package com.buonomo.cfpt.foodtrucktracker.Outils;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Authentication {

    private static String email;
    private static String password;

    public static OkHttpClient getCredentials(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(email, password));
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        return okHttpClient;
    }

    public static void setHeaders(String mEmail, String mPassword){
        email = mEmail;
        password = mPassword;
    }
}
