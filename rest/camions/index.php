<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/index.php
 * Description  : Retourne tous les food truck présents sur la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

require '../pdo.php';
require "../produits/produitFoodTruck.php";
require "../avis/index.php";

/**
 * Classe modélisant un food truck
 */
class FoodTruck {

    public $idFoodTruck;
    public $nom;
    public $image;
    public $evaluation;
    public $latitude;
    public $heureDebut;
    public $longitude;
    public $heureFin;
    public $jourSemaine;
    public $lstProduits;
    public $contact;
}

/**
 * Récupère tous les food truck de la base de donnée et les transforme en JSON qui sont à une position le jour courant
 * @return array de json - tous les food trucks dans un tableau
 */
function getFoodTrucks($latUtilisateur, $lonUtilisateur) {
    $today = date('l');
    $db = getDB();
    $request = $db->prepare("SELECT TFOODTRUCK.idFoodTruck, Nom, Image, idProprietaire, TLOCALISATION.Latitude, TLOCALISATION.Longitude, DATE_FORMAT(TESTA.HoraireDebut, '%H:%i') AS 'HoraireDebut', DATE_FORMAT(TESTA.HoraireFin, '%H:%i') AS 'HoraireFin', TESTA.JourSemaine, TFOODTRUCK.Contact FROM TFOODTRUCK, TESTA, TLOCALISATION WHERE TESTA.JourSemaine = 'Thursday' AND TESTA.idFoodTruck = TFOODTRUCK.idFoodTruck AND TESTA.idLocalisation = TLOCALISATION.idLocalisation ORDER BY (6378137 * (PI()/2 - ASIN( SIN((PI()*:lat /180)) * SIN((PI()*TLOCALISATION.latitude/180)) + COS((PI()*:lon/180) -(PI()*TLOCALISATION.longitude/180)) * COS((PI()*TLOCALISATION.latitude/180)) *COS((PI()*:lat/180)))))");
    $request->bindParam('jSemaine', $today, PDO::PARAM_STR);
    $request->bindParam('lat', $latUtilisateur);
    $request->bindParam('lon', $lonUtilisateur);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    $array = array();
    foreach ($data as $entry) {
        $obj = new FoodTruck();
        $obj->idFoodTruck = $entry['idFoodTruck'];
        $obj->nom = $entry['Nom'];
        $obj->image = $entry['Image'];
        $obj->evaluation = getNoteFoodTruck($obj->idFoodTruck);
        $obj->latitude = $entry['Latitude'];
        $obj->longitude = $entry['Longitude'];
        $obj->heureDebut = $entry['HoraireDebut'];
        $obj->heureFin = $entry['HoraireFin'];
        $obj->jourSemaine = $entry['JourSemaine'];
        $obj->contact = $entry['Contact'];
        $obj->lstProduits = getProduitSelonIdFoodTruck($obj->idFoodTruck);
        $array[] = $obj;
    }
    return json_encode($array);
}

$latitude = filter_input(INPUT_GET, 'lat', FILTER_VALIDATE_FLOAT);
$longitude = filter_input(INPUT_GET, 'lon', FILTER_VALIDATE_FLOAT);

// Affichage du Json
echo getFoodTrucks($latitude, $longitude);
