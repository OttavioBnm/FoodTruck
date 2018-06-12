package com.buonomo.cfpt.foodtrucktracker.Outils;

import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ImageService {
    public interface Callbacks{
        void onResponse(ResponseBody responseBody);
        void onFailure();
    }

    public static void uploadImage(Callbacks callback, String filePath){
        File file = new File(filePath);
        final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "image");

        final WeakReference<ImageService.Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callback);

        ServiceAccess accesService = ServiceAccess.retrofitUploadImage.create(ServiceAccess.class);

        retrofit2.Call<okhttp3.ResponseBody> call = accesService.postImage(body, name);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.code());
                if (response.code() == 200){
                    if (callbacksWeakReference.get() != null){
                        callbacksWeakReference.get().onResponse(response.body());
                        Log.i("IMAGE ", "UPLOADED SUCCESFULLY");
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("IMAGE ", "NOT UPLOADED SUCCESFULLY");
            }
        });
    }
}
