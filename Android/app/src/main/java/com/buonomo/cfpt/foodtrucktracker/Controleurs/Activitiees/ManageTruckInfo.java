package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.Outils.ImageService;
import com.buonomo.cfpt.foodtrucktracker.Outils.LocationService;
import com.buonomo.cfpt.foodtrucktracker.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class ManageTruckInfo extends AppCompatActivity implements FoodTruckService.CallbackUpdateFoodTruck, LocationService.CallbacksGetLocations, ImageService.Callbacks{

    @BindView(R.id.tbxNomCamionModifier)
    TextView truckName;
    @BindView(R.id.tbxContactModifier)
    TextView truckContact;
    @BindView(R.id.imgCamionAModifier)
    ImageView truckImage;
    @BindView(R.id.spinner_locations)
    Spinner truckLocationsSpinner;

    private FoodTruck foodTruck;
    private RequestManager glide;
    private List<String> locationList;
    private String path = "";
    private Owner owner;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_truck_info);
        ButterKnife.bind(this);
        owner = (Owner) getIntent().getExtras().getSerializable("owner");
        glide = Glide.with(this);
        foodTruck = (FoodTruck) getIntent().getSerializableExtra("truck");
        getSupportActionBar().setTitle("Modifier : " + foodTruck.getNom());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        truckContact.setText(foodTruck.getContact());
        truckName.setText(foodTruck.getNom());
        this.executeGetLocationWithRetrofit();
        glide.load("http://10.134.99.39/rest/img/" + foodTruck.getImage()).apply(RequestOptions.centerCropTransform()).into(truckImage);
    }

    public void executeUpdateTruckWithRetrofit(){
        String imgName  =getNameOfImage(path);
        FoodTruckService.updateFoodTruckInfos(this, truckName.getText().toString(), imgName,truckContact.getText().toString(), foodTruck.getIdFoodTruck(), owner);
        ImageService.uploadImage(this, path);
    }

    public String getNameOfImage(String path){
        String[] splittedPath = path.split("/");
        String newName;
        if (splittedPath[splittedPath.length-1].contains("_")){
            newName = splittedPath[splittedPath.length-1].replace("_", "-");
        }else {
            newName =splittedPath[splittedPath.length-1];
        }
        return newName;
    }

    public void executeGetLocationWithRetrofit(){
        LocationService.getLocationsFoodTruck(this, foodTruck.getIdFoodTruck());
    }

    public void onClickUpdateTruck(View v){
        this.executeUpdateTruckWithRetrofit();
    }

    public void onClickUpdateLocation(View v){
        String selectedLocation = "";
        for (String s: locationList) {
            if (s.contains((String)truckLocationsSpinner.getSelectedItem())){
                selectedLocation = s;
            }
        }
        Intent i = new Intent(ManageTruckInfo.this, ManageTruckLocalisation.class);
        i.putExtra("selectedLocation", selectedLocation);
        i.putExtra("truck", foodTruck);
        i.putExtra("owner", owner);
        startActivity(i);
    }

    @Override
    public void OnResponse(Void response) {
        Intent i = new Intent(ManageTruckInfo.this, OwnerTruck.class);
        startActivity(i);
        finish();
        Log.e("OK ", "UPDATED");
    }

    @Override
    public void OnFailure() {
        Log.e("ERROR ", "NOT UPDATED");
    }

    @Override
    public void onResponse(List<String> locations) {
        locationList = locations;
        List<String> arrayList = new ArrayList<>();
        for (String s: locations) {
            String[] strSplitted = s.split(",");
            arrayList.add(strSplitted[4]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        truckLocationsSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onResponse(ResponseBody responseBody) {

    }

    @Override
    public void onFailure() {
        Log.e("ERROR ", "NOT AVAILABLE LOCATIONS");
    }

    /**
     * Démarre une instance qui permet de prendre une photo dans la gallerie
     * @param v vue
     */
    public void onClickProfilePicture(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);
    }

    /**
     * Récupération de l'image sélectionnée
     * @param reqCode Code de demande
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                truckImage.setImageBitmap(selectedImage);
                path = this.getRealPathFromURI(imageUri);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Converti l'image en chemain de fichier
     * @param contentUri chemain de l'image
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
