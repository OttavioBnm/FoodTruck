package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.buonomo.cfpt.foodtrucktracker.Models.Product;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.Outils.ImageService;
import com.buonomo.cfpt.foodtrucktracker.Outils.ProductsService;
import com.buonomo.cfpt.foodtrucktracker.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class AddTruck extends AppCompatActivity implements ProductsService.Callbacks, FoodTruckService.CallbacksCreateFoodTruck, LocationListener, ImageService.Callbacks {

    // Déclaration des vues XML
    @BindView(R.id.tbxNomCamion)
    TextView truckName;
    @BindView(R.id.tbxAdresseCamion)
    TextView truckAddress;
    @BindView(R.id.tbxNPA)
    TextView truckPostalCode;
    @BindView(R.id.tbxLocalite)
    TextView truckCity;
    @BindView(R.id.tbxHeureDebut)
    TextView truckStartTime;
    @BindView(R.id.tbxHeureFin)
    TextView truckEndTime;
    @BindView(R.id.ratingNote)
    RatingBar truckEvaluation;
    @BindView(R.id.show_list_products)
    TextView showProducts;
    @BindView(R.id.imgCamionAAjouter)
    ImageView img;
    @BindView(R.id.progressBar_current_location)
    ProgressBar pb;
    @BindView(R.id.form_adress_new_truck)
    LinearLayout formLocation;
    @BindView(R.id.jour_semaine)
    RadioGroup radioGroup;
    @BindView(R.id.tbxContact)
    TextView truckContact;

    // Champs
    private String[] tableProducts;
    private boolean[] checkedItems;
    private ArrayList<Integer> userItems = new ArrayList<>();
    private Geocoder g;
    private List<Address> a;
    private LocationManager lm;
    private LatLng latLng;
    private String value = "Monday";
    private String path = "";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.executeRequestProducts();
        this.radioManagment();
    }

    /**
     * Gestion des clicks sur les radio boutons
     */
    public void radioManagment(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbLundi:
                        value = "Monday";
                        break;
                    case R.id.rbMardi:
                        value = "Tuesday";
                        break;
                    case R.id.rbMercredi:
                        value = "Wednesday";
                        break;
                    case R.id.rbJeudi:
                        value = "Thursday";
                        break;
                    case R.id.rbVendredi:
                        value = "Friday";
                        break;
                    case R.id.rbSamedi:
                        value = "Saturday";
                        break;
                    case R.id.rbDimanche:
                        value = "Sunday";
                        break;
                }
            }
        });
    }

    /**
     * Création de l'affichage du menu dans l'action bar de l'activitée
     * @param menu Menu à afficher
     * @return affiche le menu (oui/non)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Ajoute un nouveau food truck et vérifie les infos saisies
     */
    public void addNewFoodTruck(){
        boolean cancel = false;
        View focusView = null;
        String imgName  =getNameOfImage(path);
        if (TextUtils.isEmpty(truckName.getText())){
            cancel = true;
            truckName.setError(getString(R.string.error_field_required));
            focusView = truckName;
        }
        if (TextUtils.isEmpty(truckAddress.getText())){
            cancel = true;
            focusView = truckAddress;
            truckAddress.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(truckPostalCode.getText())){
            cancel = true;
            focusView = truckPostalCode;
            truckPostalCode.setError(getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(truckCity.getText())){
            cancel = true;
            focusView = truckCity;
            truckCity.setError(getString(R.string.error_field_required));
        }
        if (truckStartTime.getText().equals("Sélectionner l'heure de début...")){
            cancel = true;
            focusView = truckStartTime;
            truckStartTime.setError(getString(R.string.error_field_required));
        }
        if (truckEndTime.getText().equals("Sélectionner l'heure de fin...")){
            cancel = true;
            focusView = truckEndTime;
            truckEndTime.setError(getString(R.string.error_field_required));
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            pb.setVisibility(View.VISIBLE);
            formLocation.setVisibility(View.INVISIBLE);
            LatLng l = convertAddress(truckAddress.getText()+", "+ truckPostalCode+", "+ truckCity);
            this.executeFoodTruckCreation(
                    truckName.getText().toString(),
                    l.latitude,
                    l.longitude,
                    truckStartTime.getText().toString(),
                    truckEndTime.getText().toString(),
                    value,
                    -1,
                    truckContact.getText().toString(),
                    imgName,
                    Math.round(truckEvaluation.getRating()));
            ImageService.uploadImage(this, path);
            pb.setVisibility(View.INVISIBLE);
            formLocation.setVisibility(View.VISIBLE);
        }
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

    /**
     * Requête de la liste des produits
     */
    public void executeRequestProducts(){
        ProductsService.getProducts(this);
    }

    /**
     * Envoie de la requête pour l'ajout de food truck
     * @param name Nom du food truck
     * @param lat  Latitude du food truck
     * @param lon Longitude du food truck
     * @param startTime Heure de début de vente du food trucks
     * @param endTime Heure de fin de vente du food trucks
     * @param weekDay Jour de la vente
     * @param idOwner Id du propriétaire
     * @param contact Contact du food truck
     * @param image Image du food truck
     * @param rating Note du food truck
     */
    public void executeFoodTruckCreation( String name, double lat, double lon, String startTime, String endTime, String weekDay, int idOwner, String contact, String image, int rating){
        String idProducts = "";
        for (int i = 0; i< userItems.size(); i++){
            if (i == userItems.size()-1){
                idProducts += userItems.get(i)+1;
            }else{
                idProducts += userItems.get(i)+1 + ";";
            }
        }
        FoodTruckService.createFoodTruck(this, name, lat, lon, startTime, endTime, weekDay, idOwner, contact, image, rating, idProducts);
    }

    /**
     * Récupération de la liste des produits
     * @param products liste des produits
     */
    @Override
    public void onResponse(List<Product> products) {
        tableProducts = new String[products.size()];
        for (int i = 0; i <products.size(); i++) {
            tableProducts[i] = products.get(i).toString();
        }
        checkedItems = new boolean[tableProducts.length];
    }

    /**
     * Récupération du commentaire du server lors de l'ajout de food truck
     * @param ok Réponse du server
     */
    @Override
    public void onResponse(String ok) {
        Intent i = new Intent(AddTruck.this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Converti une adresse en latitude longitude
     * @param addressTruck Adresse à convertir
     * @return une latitude longitude
     */
    private LatLng convertAddress(String addressTruck){
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(addressTruck, 1);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return resLatLng;
    }

    @Override
    public void onResponse(ResponseBody responseBody) {

    }

    /**
     * Erreur de création du food truck
     */
    @Override
    public void onFailure() {
        Log.e("Creation Food Truck", "ERROR");
    }

    /**
     * Affiche le dialogue qui permet de choisir les produits vendus par le nouveau food truck
     * @param view
     */
    public void showDialog(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Produits disponibles");
        mBuilder.setMultiChoiceItems(tableProducts, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if (isChecked){
                    if (!userItems.contains(position)){
                        userItems.add(position);
                    }else {
                        userItems.remove(position);
                    }
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i = 0; i < userItems.size(); i++){
                    item = item + tableProducts[userItems.get(i)];
                    if (i != userItems.size() -1){
                        item = item + ", ";
                    }
                }
                showProducts.setText(item);
            }
        });

        mBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setNeutralButton("Tout déselectionner", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (boolean check: checkedItems) {
                    check = false;
                    userItems.clear();
                    showProducts.setText("Sélectionner un produit...");
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
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
                img.setImageBitmap(selectedImage);
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

    /**
     * Affiche le time picker pour la récupération de l'heure de début
     * @param view vue
     */
    public void showTimePickerStart(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                truckStartTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Sélectionner l'heure de début");
        mTimePicker.show();
    }

    /**
     * Affiche le time picker pour la récupération de l'heure de fin
     * @param view vue
     */
    public void showTimePickerEnd(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                truckEndTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Sélectionner l'heure de fin");
        mTimePicker.show();
    }

    /**
     * Récupération de la position de l'utilisateur
     * @param view vue
     */
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(View view) {
        final Handler h = new Handler();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        a = new ArrayList<>();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (latLng != null){
                    h.removeCallbacks(this);
                    g = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        a = g.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String[] adresse = new String[3];
                        adresse = a.get(0).getAddressLine(0).split(",");
                        String[] postalCodeCity = adresse[1].split(" ");
                        truckAddress.setText(adresse[0]);
                        truckPostalCode.setText(postalCodeCity[1]);
                        truckCity.setText(postalCodeCity[2]);
                        lm.removeUpdates(AddTruck.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    h.postDelayed(this, 100);
                }
            }
        };
        runnable.run();
    }

    /**
     * Met à jour la position lors du changement de la localisation
     * @param location localisation GPS
     */
    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Ajoute un nouveau food truck lors du clic sur le bouton
     * @param view vue
     */
    public void onClickAddNewFoodTruck(View view) {
        this.addNewFoodTruck();
    }
}
