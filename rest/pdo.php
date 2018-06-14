<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : pdo.php
 * Description  : Classe pour le connecteur à la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Constantes
DEFINE('DBHOST',"localhost");
DEFINE('DBUSER',"BUONOMOO");
DEFINE('DBPASS',"BUONOMOO");
DEFINE('DBNAME', "FoodTruckTracker");
static $pdo = null;
header("Content-Type: application/json; charset=UTF-8");

// Connexion à la base de données
function getDB(){
    if(is_null($pdo)){

    $dsn = 'mysql:dbname='.DBNAME.';host='.DBHOST;
    try{
    $pdo = new PDO($dsn,DBUSER,DBPASS);
    } catch (PDOException $e){
        echo 'Connexion echouee : '.$e->getMessage();
    }}
    return $pdo;
}
