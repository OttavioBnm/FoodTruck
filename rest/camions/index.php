<?php

// Require du PDO
require "../pdo.php";

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
 * Récupère tous les food truck de la base de donnée et les transforme en JSON
 * @return array de json - tous les food trucks dans un tableau
 */
function getFoodTrucks() {
    $today = date('l');
    $db = getDB();
    $request = $db->prepare("SELECT TFOODTRUCK.idFoodTruck, Nom, Image, idProprietaire, TLOCALISATION.Latitude, TLOCALISATION.Longitude, DATE_FORMAT(TESTA.HoraireDebut, '%H:%i') AS 'HoraireDebut', DATE_FORMAT(TESTA.HoraireFin, '%H:%i') AS 'HoraireFin', TESTA.JourSemaine, TFOODTRUCK.Contact FROM TFOODTRUCK, TESTA, TLOCALISATION WHERE TESTA.JourSemaine = :jSemaine AND TESTA.idFoodTruck = TFOODTRUCK.idFoodTruck AND TESTA.idLocalisation = TLOCALISATION.idLocalisation");
    $request->bindParam('jSemaine', $today, PDO::PARAM_STR);
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
        $obj->lstProduits = getFoodTrucksProduits($obj->idFoodTruck);
        $array[] = $obj;
    }
    return json_encode($array);
}

/**
 * Fait la moyenne de tous les avis d'un food truck
 * @param int $idFoodTruck - id du food truck dont on souhaite les avis
 * @return double moyenne de tous les avis d'un food truck
 */
function getNoteFoodTruck($idFoodTruck) {
    $db = getDB();
    $request = $db->prepare("SELECT AVG(`Note`) FROM `TAVIS` WHERE `idFoodTruck` = :id");
    $request->bindParam('id', $idFoodTruck, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    foreach ($data as $value) {
        $str = $value['AVG(`Note`)'];
    }
    return $str;
}

/**
 * Récupère tous les produits vendus par un food truck
 * @param int $idFoodTruck - id du food truck dont on souhaite les produits
 * @return string les produits vendus par un food truck sépartés par une virgule
 */
function getFoodTrucksProduits($idFoodTruck) {
    $db = getDB();
    $request = $db->prepare("SELECT `TPRODUITS`.`Nom` FROM `TPRODUITS`, `TVEND` WHERE `TVEND`.`idFoodTruck` = :id GROUP BY `TPRODUITS`.`Nom`");
    $request->bindParam('id', $idFoodTruck, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    for ($i=0; $i < count($data); $i++) {
        if ($i == count($data)-1) {
            $str .= $data[$i]['Nom'];
        }
        else{
            $str .= $data[$i]['Nom'] . ", ";
        }
    }
    return $str;
}

// Affichage du Json
echo getFoodTrucks();
