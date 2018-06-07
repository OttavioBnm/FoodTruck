/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Controleurs.Activitees
 * Classe       : MainActivity.java
 * Description  : Controleur permettant d'afficher la liste de food truck sur la page principale de l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.MainFragment;
import com.buonomo.cfpt.foodtrucktracker.R;

public class MainActivity extends AppCompatActivity {

    // Attributs de la classe
    private MainFragment mainFragment;
    private static Context instance;

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
     * Création de la vue xml de l'application
     * @param savedInstanceState données de sauvegarde de la vue
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        this.configureAndShowMainFragment();

    }

    /**
     * Gestion du menu
     * @param item element du menu qui est cliqué
     * @return false si le menu doit être fermé, true si un élément est cliqué et une action est demandée
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login:

                return true;
            case R.id.info:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Configuration du frgment MainFragment a integrer dans la MainActivity
     */
    private void configureAndShowMainFragment() {
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

        if (mainFragment == null){
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frame_layout, mainFragment).commit();

        }
    }

    /**
     * Clic sur le bouton flottant de la MainActivity
     * @param view vue xml cliquee
     */
    public void onClickFAB(View view){

    }

    /**
     * Recupere le context de la MainActivity
     * @return contexte de le controleur MainActivity
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
